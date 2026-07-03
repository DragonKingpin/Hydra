package com.pinecone.hydra.storage.repair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocationType;
import com.pinecone.hydra.storage.fsck.StorageFsckIssue;
import com.pinecone.hydra.storage.fsck.StorageFsckReport;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntent;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.core.VolumeType;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class TitanStorageIntegrityService implements StorageIntegrityService {
    protected static final int PAGE_SIZE = 512;

    protected static final String DomainUofs = "UOFS";
    protected static final String DomainVolume = "VOLUME";

    protected static final String SeverityError = "ERROR";
    protected static final String SeverityWarning = "WARNING";

    protected static final String ActionRefreshVolumeUsage = "REFRESH_VOLUME_USAGE";
    protected static final String ActionMoveOrphanObject = "MOVE_ORPHAN_OBJECT";
    protected static final String ActionPurgeOrphanLocation = "PURGE_ORPHAN_LOCATION";
    protected static final String ActionPurgeOrphanChunk = "PURGE_ORPHAN_CHUNK";

    protected final KOMFileSystem fileSystem;
    protected final VolumeManager volumeManager;

    public TitanStorageIntegrityService( KOMFileSystem fileSystem, VolumeManager volumeManager ) {
        this.fileSystem = fileSystem;
        this.volumeManager = volumeManager;
    }

    @Override
    public StorageFsckReport uofsFsck() {
        this.assertKernelReady();

        FatChunkInstrument fat = this.fileSystem.getFatChunkInstrument();
        StorageFsckReport report = this.newReport( DomainUofs );
        Set<GUID> fileGuids = new HashSet<>();
        Set<GUID> chunkGuids = new HashSet<>();
        Set<GUID> chunkFileGuids = new HashSet<>();

        List<TreeNode> allNodes = this.fileSystem.getAllTreeNode();
        for ( TreeNode node : allNodes ) {
            if ( node instanceof FileNode ) {
                FileNode fileNode = (FileNode) node;
                fileGuids.add( fileNode.getGuid() );
                report.setScannedFiles( report.getScannedFiles() + 1L );
            }
        }

        this.forEachChunk( fat, chunk -> {
            report.setScannedChunks( report.getScannedChunks() + 1L );
            chunkGuids.add( chunk.getGuid() );
            chunkFileGuids.add( chunk.getFileGuid() );
            if ( chunk.getFileGuid() == null || !fileGuids.contains( chunk.getFileGuid() ) ) {
                report.getIssues().add( this.issue(
                        DomainUofs,
                        SeverityError,
                        "CHUNK_WITHOUT_FILE",
                        "FAT chunk points to an undefined UOFS file node.",
                        null,
                        chunk.getFileGuid(),
                        chunk.getGuid(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        ActionPurgeOrphanChunk,
                        true
                ) );
            }
        } );

        for ( TreeNode node : allNodes ) {
            if ( node instanceof FileNode ) {
                FileNode fileNode = (FileNode) node;
                if ( fileNode.getDefinitionSize() > 0L && !chunkFileGuids.contains( fileNode.getGuid() ) ) {
                    report.getIssues().add( this.issue(
                            DomainUofs,
                            SeverityError,
                            "FILE_WITHOUT_CHUNK",
                            "UOFS file node has positive size but no FAT chunk.",
                            fileNode.getGuid(),
                            fileNode.getGuid(),
                            null,
                            null,
                            null,
                            null,
                            this.safePath( fileNode ),
                            String.valueOf( fileNode.getDefinitionSize() ),
                            "chunk >= 1",
                            null,
                            false
                    ) );
                }
            }
        }

        this.forEachLocation( fat, location -> {
            report.setScannedLocations( report.getScannedLocations() + 1L );
            if ( location.getChunkGuid() == null || !chunkGuids.contains( location.getChunkGuid() ) ) {
                report.getIssues().add( this.issue(
                        DomainUofs,
                        SeverityError,
                        "LOCATION_WITHOUT_CHUNK",
                        "FAT location points to an undefined chunk.",
                        null,
                        null,
                        location.getChunkGuid(),
                        location.getGuid(),
                        location.getVolumeGuid(),
                        location.getObjectKey(),
                        null,
                        null,
                        null,
                        ActionPurgeOrphanLocation,
                        true
                ) );
            }
        } );

        this.finishReport( report );
        return report;
    }

    @Override
    public StorageFsckReport volumeFsck() {
        this.assertKernelReady();

        FatChunkInstrument fat = this.fileSystem.getFatChunkInstrument();
        StorageFsckReport report = this.newReport( DomainVolume );
        Map<GUID, Volume> volumes = this.loadVolumes( report );
        Map<GUID, List<FileChunkLocation>> locationsByVolume = this.loadLocationsByVolume( fat, report );
        Set<String> referencedObjects = new HashSet<>();

        for ( List<FileChunkLocation> locations : locationsByVolume.values() ) {
            for ( FileChunkLocation location : locations ) {
                this.inspectLocationVolume( report, volumes, location );
                if ( this.isObjectLocation( volumes.get( location.getVolumeGuid() ), location ) ) {
                    this.inspectObjectLocation( report, volumes.get( location.getVolumeGuid() ), location );
                    if ( location.getObjectKey() != null ) {
                        referencedObjects.add( this.objectRef( location.getVolumeGuid(), location.getObjectKey() ) );
                    }
                }
            }
        }

        for ( Volume volume : volumes.values() ) {
            this.inspectVolumeUsage( report, volume, locationsByVolume.get( volume.getGuid() ) );
            this.inspectOrphanObjects( report, volume, referencedObjects );
        }

        this.inspectFreeIntents( report, volumes.keySet() );
        this.finishReport( report );
        return report;
    }

    @Override
    public StorageRepairReport dryRun( StorageRepairPolicy policy ) {
        return this.repair( policy, true );
    }

    @Override
    public StorageRepairReport apply( StorageRepairPolicy policy ) {
        return this.repair( policy, false );
    }

    protected StorageRepairReport repair( StorageRepairPolicy policy, boolean dryRun ) {
        StorageRepairPolicy effectivePolicy = policy == null ? new StorageRepairPolicy() : policy;
        StorageRepairReport report = new StorageRepairReport();
        report.setDryRun( dryRun );
        report.setGeneratedAt( LocalDateTime.now() );
        report.getReports().add( this.uofsFsck() );
        report.getReports().add( this.volumeFsck() );

        for ( StorageFsckReport fsckReport : report.getReports() ) {
            for ( StorageFsckIssue issue : fsckReport.getIssues() ) {
                StorageRepairAction action = this.planAction( effectivePolicy, issue );
                if ( action == null ) {
                    continue;
                }
                if ( dryRun ) {
                    action.setStatus( "DRY_RUN" );
                }
                else {
                    this.applyAction( action, issue );
                }
                report.getActions().add( action );
            }
        }
        return report;
    }

    protected StorageRepairAction planAction( StorageRepairPolicy policy, StorageFsckIssue issue ) {
        String actionType = issue.getActionType();
        if ( actionType == null || actionType.isBlank() ) {
            return null;
        }
        StorageRepairAction action = new StorageRepairAction();
        action.setActionType( actionType );
        action.setIssueCode( issue.getCode() );
        action.setTargetGuid( this.firstGuid( issue.getLocationGuid(), issue.getChunkGuid(), issue.getGuid() ) );
        action.setVolumeGuid( issue.getVolumeGuid() );
        action.setObjectKey( issue.getObjectKey() );
        action.setDangerous( issue.isDangerous() );

        if ( ActionRefreshVolumeUsage.equals( actionType ) && !this.enabled( policy.getRefreshVolumeUsage(), true ) ) {
            return this.skipped( action, "Volume usage refresh is disabled." );
        }
        if ( ActionMoveOrphanObject.equals( actionType ) && !this.enabled( policy.getMoveOrphanObjects(), false ) ) {
            return this.skipped( action, "Moving orphan object is disabled." );
        }
        if (
                ( ActionPurgeOrphanLocation.equals( actionType ) || ActionPurgeOrphanChunk.equals( actionType ) )
                        && !this.enabled( policy.getPurgeOrphanFat(), false )
        ) {
            return this.skipped( action, "Purging orphan FAT metadata is disabled." );
        }
        action.setStatus( "PLANNED" );
        action.setMessage( "Repair action is planned." );
        return action;
    }

    protected StorageRepairAction skipped( StorageRepairAction action, String message ) {
        action.setStatus( "SKIPPED" );
        action.setMessage( message );
        return action;
    }

    protected void applyAction( StorageRepairAction action, StorageFsckIssue issue ) {
        try {
            if ( ActionRefreshVolumeUsage.equals( action.getActionType() ) ) {
                this.volumeManager.refreshVolumeUsage( issue.getVolumeGuid() );
            }
            else if ( ActionMoveOrphanObject.equals( action.getActionType() ) ) {
                this.moveOrphanObject( issue );
            }
            else if ( ActionPurgeOrphanLocation.equals( action.getActionType() ) ) {
                this.fileSystem.getFatChunkInstrument().deleteLocation( issue.getLocationGuid() );
            }
            else if ( ActionPurgeOrphanChunk.equals( action.getActionType() ) ) {
                this.purgeOrphanChunk( issue );
            }
            action.setStatus( "APPLIED" );
            action.setMessage( "Repair action applied." );
        }
        catch ( Exception e ) {
            action.setStatus( "FAILED" );
            action.setMessage( e.getMessage() );
        }
    }

    protected void purgeOrphanChunk( StorageFsckIssue issue ) throws IOException {
        GUID chunkGuid = issue.getChunkGuid();
        FatChunkInstrument fat = this.fileSystem.getFatChunkInstrument();
        for ( FileChunkLocation location : fat.fetchLocations( chunkGuid ) ) {
            this.volumeManager.release( location );
        }
        fat.deleteChunk( chunkGuid );
    }

    protected void moveOrphanObject( StorageFsckIssue issue ) throws IOException {
        Path source = Paths.get( issue.getPath() );
        if ( !Files.exists( source ) ) {
            return;
        }
        Volume volume = this.volumeManager.loadVolume( issue.getVolumeGuid() );
        Path root = Paths.get( volume.getObjectRoot() ).normalize();
        Path relative = root.relativize( source.normalize() );
        Path target = root.resolve( ".titan" )
                .resolve( "lost+found" )
                .resolve( LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyyMMddHHmmss" ) ) )
                .resolve( relative )
                .normalize();
        Files.createDirectories( target.getParent() );
        Files.move( source, target, StandardCopyOption.REPLACE_EXISTING );
    }

    protected void inspectLocationVolume(
            StorageFsckReport report,
            Map<GUID, Volume> volumes,
            FileChunkLocation location
    ) {
        if ( location.getVolumeGuid() == null || !volumes.containsKey( location.getVolumeGuid() ) ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityError,
                    "LOCATION_WITHOUT_VOLUME",
                    "FAT location points to an undefined volume.",
                    null,
                    null,
                    location.getChunkGuid(),
                    location.getGuid(),
                    location.getVolumeGuid(),
                    location.getObjectKey(),
                    null,
                    null,
                    null,
                    null,
                    false
            ) );
        }
    }

    protected void inspectObjectLocation( StorageFsckReport report, Volume volume, FileChunkLocation location ) {
        if ( volume == null ) {
            return;
        }
        if ( location.getObjectKey() == null || location.getObjectKey().isBlank() ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityError,
                    "OBJECT_LOCATION_WITHOUT_KEY",
                    "Direct object location has no object key.",
                    null,
                    null,
                    location.getChunkGuid(),
                    location.getGuid(),
                    volume.getGuid(),
                    null,
                    null,
                    null,
                    "objectKey",
                    null,
                    false
            ) );
            return;
        }
        try {
            Path objectPath = Paths.get( volume.getObjectRoot() ).resolve( location.getObjectKey() ).normalize();
            if ( !Files.exists( objectPath ) ) {
                report.getIssues().add( this.issue(
                        DomainVolume,
                        SeverityError,
                        "LOCATION_OBJECT_MISSING_ON_DISK",
                        "Direct object location points to a missing disk object.",
                        null,
                        null,
                        location.getChunkGuid(),
                        location.getGuid(),
                        volume.getGuid(),
                        location.getObjectKey(),
                        objectPath.toString(),
                        "missing",
                        "exists",
                        null,
                        false
                ) );
            }
        }
        catch ( RuntimeException e ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityError,
                    "OBJECT_LOCATION_PATH_INVALID",
                    e.getMessage(),
                    null,
                    null,
                    location.getChunkGuid(),
                    location.getGuid(),
                    volume.getGuid(),
                    location.getObjectKey(),
                    null,
                    null,
                    null,
                    null,
                    false
            ) );
        }
    }

    protected void inspectVolumeUsage(
            StorageFsckReport report,
            Volume volume,
            List<FileChunkLocation> locations
    ) {
        if ( volume == null || volume.getVolumeType() != VolumeType.SIMPLE ) {
            return;
        }
        long expected = this.expectedCommittedBytes( volume, locations == null ? List.of() : locations );
        if ( expected >= 0L && expected != volume.getCommittedBytes() ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityWarning,
                    "VOLUME_USAGE_DRIFT",
                    "Volume committed bytes drift from current storage observation.",
                    volume.getGuid(),
                    null,
                    null,
                    null,
                    volume.getGuid(),
                    null,
                    null,
                    String.valueOf( volume.getCommittedBytes() ),
                    String.valueOf( expected ),
                    ActionRefreshVolumeUsage,
                    false
            ) );
        }
    }

    protected long expectedCommittedBytes( Volume volume, List<FileChunkLocation> locations ) {
        if ( volume.getMappingMode() == VolumeMappingMode.VOLUME_DIRECT_OBJECT ) {
            return this.objectRootSize( volume );
        }
        long maxEnd = 0L;
        for ( FileChunkLocation location : locations ) {
            maxEnd = Math.max( maxEnd, location.getVolumeOffset() + location.getLengthBytes() );
        }
        return maxEnd;
    }

    protected long objectRootSize( Volume volume ) {
        if ( volume.getObjectRoot() == null || volume.getObjectRoot().isBlank() ) {
            return -1L;
        }
        Path root = Paths.get( volume.getObjectRoot() );
        if ( !Files.exists( root ) ) {
            return 0L;
        }
        try {
            final long[] size = new long[] { 0L };
            try ( java.util.stream.Stream<Path> stream = Files.walk( root ) ) {
                stream.filter( Files::isRegularFile )
                        .filter( path -> !this.isLostFoundPath( root, path ) )
                        .forEach( path -> {
                            try {
                                size[0] += Files.size( path );
                            }
                            catch ( IOException ignored ) {
                            }
                        } );
            }
            return size[0];
        }
        catch ( IOException e ) {
            return -1L;
        }
    }

    protected void inspectOrphanObjects(
            StorageFsckReport report,
            Volume volume,
            Set<String> referencedObjects
    ) {
        if ( volume == null || volume.getMappingMode() != VolumeMappingMode.VOLUME_DIRECT_OBJECT ) {
            return;
        }
        if ( volume.getObjectRoot() == null || volume.getObjectRoot().isBlank() ) {
            return;
        }
        Path root = Paths.get( volume.getObjectRoot() ).normalize();
        if ( !Files.exists( root ) ) {
            return;
        }
        try ( java.util.stream.Stream<Path> stream = Files.walk( root ) ) {
            stream.filter( Files::isRegularFile ).forEach( path -> {
                if ( this.isLostFoundPath( root, path ) ) {
                    return;
                }
                String objectKey = root.relativize( path.normalize() ).toString().replace( '\\', '/' );
                if ( !referencedObjects.contains( this.objectRef( volume.getGuid(), objectKey ) ) ) {
                    report.getIssues().add( this.issue(
                            DomainVolume,
                            SeverityWarning,
                            "ORPHAN_DISK_OBJECT",
                            "Object file exists on disk but no FAT location references it.",
                            null,
                            null,
                            null,
                            null,
                            volume.getGuid(),
                            objectKey,
                            path.toString(),
                            "unreferenced",
                            "referenced",
                            ActionMoveOrphanObject,
                            true
                    ) );
                }
            } );
        }
        catch ( IOException e ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityWarning,
                    "OBJECT_ROOT_SCAN_FAILED",
                    e.getMessage(),
                    volume.getGuid(),
                    null,
                    null,
                    null,
                    volume.getGuid(),
                    null,
                    root.toString(),
                    null,
                    null,
                    null,
                    false
            ) );
        }
    }

    protected void inspectFreeIntents( StorageFsckReport report, Set<GUID> volumeGuids ) {
        Map<GUID, GUID> sourceIntentGuids = new HashMap<>();
        int offset = 0;
        while ( true ) {
            List<VolumeFreeIntent> intents = this.volumeManager.listVolumeFreeIntentPage( offset, PAGE_SIZE );
            if ( intents.isEmpty() ) {
                break;
            }
            report.setScannedFreeIntents( report.getScannedFreeIntents() + intents.size() );
            for ( VolumeFreeIntent intent : intents ) {
                if ( intent.getVolumeGuid() == null || !volumeGuids.contains( intent.getVolumeGuid() ) ) {
                    report.getIssues().add( this.issue(
                            DomainVolume,
                            SeverityWarning,
                            "FREE_INTENT_WITHOUT_VOLUME",
                            "Volume free intent points to an undefined volume.",
                            intent.getGuid(),
                            null,
                            null,
                            intent.getSourceLocationGuid(),
                            intent.getVolumeGuid(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            false
                    ) );
                }
                if ( intent.getSourceLocationGuid() != null ) {
                    GUID oldIntentGuid = sourceIntentGuids.putIfAbsent( intent.getSourceLocationGuid(), intent.getGuid() );
                    if ( oldIntentGuid != null ) {
                        report.getIssues().add( this.issue(
                                DomainVolume,
                                SeverityWarning,
                                "DUPLICATE_FREE_INTENT_SOURCE",
                                "Multiple free intents share one source location.",
                                intent.getGuid(),
                                null,
                                null,
                                intent.getSourceLocationGuid(),
                                intent.getVolumeGuid(),
                                null,
                                null,
                                String.valueOf( oldIntentGuid ),
                                String.valueOf( intent.getGuid() ),
                                null,
                                false
                        ) );
                    }
                }
            }
            if ( intents.size() < PAGE_SIZE ) {
                break;
            }
            offset += intents.size();
        }
    }

    protected Map<GUID, Volume> loadVolumes( StorageFsckReport report ) {
        Map<GUID, Volume> ret = new HashMap<>();
        try {
            for ( Volume volume : this.volumeManager.listVolumes() ) {
                ret.put( volume.getGuid(), volume );
                report.setScannedVolumes( report.getScannedVolumes() + 1L );
            }
        }
        catch ( IOException e ) {
            report.getIssues().add( this.issue(
                    DomainVolume,
                    SeverityError,
                    "VOLUME_LIST_FAILED",
                    e.getMessage(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    false
            ) );
        }
        return ret;
    }

    protected Map<GUID, List<FileChunkLocation>> loadLocationsByVolume(
            FatChunkInstrument fat,
            StorageFsckReport report
    ) {
        Map<GUID, List<FileChunkLocation>> ret = new HashMap<>();
        this.forEachLocation( fat, location -> {
            report.setScannedLocations( report.getScannedLocations() + 1L );
            if ( location.getVolumeGuid() != null ) {
                ret.computeIfAbsent( location.getVolumeGuid(), key -> new ArrayList<>() ).add( location );
            }
        } );
        return ret;
    }

    protected boolean isObjectLocation( Volume volume, FileChunkLocation location ) {
        if ( location.getLocationType() == FileChunkLocationType.VOLUME_DIRECT_OBJECT ) {
            return true;
        }
        return location.getLocationType() == null
                && volume != null
                && volume.getMappingMode() == VolumeMappingMode.VOLUME_DIRECT_OBJECT;
    }

    protected boolean isLostFoundPath( Path root, Path path ) {
        Path relative = root.normalize().relativize( path.normalize() );
        return relative.toString().replace( '\\', '/' ).startsWith( ".titan/lost+found/" );
    }

    protected String objectRef( GUID volumeGuid, String objectKey ) {
        return String.valueOf( volumeGuid ) + ":" + objectKey.replace( '\\', '/' );
    }

    protected void forEachChunk( FatChunkInstrument fat, ChunkConsumer consumer ) {
        int offset = 0;
        while ( true ) {
            List<FileChunk> chunks = fat.fetchChunkPage( offset, PAGE_SIZE );
            if ( chunks.isEmpty() ) {
                break;
            }
            for ( FileChunk chunk : chunks ) {
                consumer.accept( chunk );
            }
            if ( chunks.size() < PAGE_SIZE ) {
                break;
            }
            offset += chunks.size();
        }
    }

    protected void forEachLocation( FatChunkInstrument fat, LocationConsumer consumer ) {
        int offset = 0;
        while ( true ) {
            List<FileChunkLocation> locations = fat.fetchLocationPage( offset, PAGE_SIZE );
            if ( locations.isEmpty() ) {
                break;
            }
            for ( FileChunkLocation location : locations ) {
                consumer.accept( location );
            }
            if ( locations.size() < PAGE_SIZE ) {
                break;
            }
            offset += locations.size();
        }
    }

    protected StorageFsckReport newReport( String domain ) {
        StorageFsckReport report = new StorageFsckReport();
        report.setDomain( domain );
        report.setGeneratedAt( LocalDateTime.now() );
        return report;
    }

    protected void finishReport( StorageFsckReport report ) {
        long errors = 0L;
        long warnings = 0L;
        for ( StorageFsckIssue issue : report.getIssues() ) {
            if ( SeverityError.equals( issue.getSeverity() ) ) {
                errors++;
            }
            else if ( SeverityWarning.equals( issue.getSeverity() ) ) {
                warnings++;
            }
        }
        report.setTotalIssues( report.getIssues().size() );
        report.setErrorIssues( errors );
        report.setWarningIssues( warnings );
    }

    protected StorageFsckIssue issue(
            String domain,
            String severity,
            String code,
            String message,
            GUID guid,
            GUID fileGuid,
            GUID chunkGuid,
            GUID locationGuid,
            GUID volumeGuid,
            String objectKey,
            String path,
            String actualValue,
            String expectedValue,
            String actionType,
            boolean dangerous
    ) {
        StorageFsckIssue issue = new StorageFsckIssue();
        issue.setDomain( domain );
        issue.setSeverity( severity );
        issue.setCode( code );
        issue.setMessage( message );
        issue.setGuid( guid );
        issue.setFileGuid( fileGuid );
        issue.setChunkGuid( chunkGuid );
        issue.setLocationGuid( locationGuid );
        issue.setVolumeGuid( volumeGuid );
        issue.setObjectKey( objectKey );
        issue.setPath( path );
        issue.setActualValue( actualValue );
        issue.setExpectedValue( expectedValue );
        issue.setActionType( actionType );
        issue.setDangerous( dangerous );
        return issue;
    }

    protected String safePath( FileNode fileNode ) {
        if ( fileNode.getPath() != null && !fileNode.getPath().isBlank() ) {
            return fileNode.getPath();
        }
        return this.fileSystem.getPath( fileNode.getGuid() );
    }

    protected GUID firstGuid( GUID ...values ) {
        for ( GUID value : values ) {
            if ( value != null ) {
                return value;
            }
        }
        return null;
    }

    protected boolean enabled( Boolean value, boolean defaultValue ) {
        return value == null ? defaultValue : value;
    }

    protected void assertKernelReady() {
        if ( this.fileSystem == null ) {
            throw new IllegalStateException( "Titan storage file kernel is not ready." );
        }
        if ( this.volumeManager == null ) {
            throw new IllegalStateException( "Titan storage volume manager is not ready." );
        }
    }

    protected interface ChunkConsumer {
        void accept( FileChunk chunk );
    }

    protected interface LocationConsumer {
        void accept( FileChunkLocation location );
    }
}
