package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.block.BlockVolume;
import com.pinecone.hydra.storage.volume.block.SimpleVolume;
import com.pinecone.hydra.storage.volume.block.SpannedVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.block.TitanBlockSimpleVolume;
import com.pinecone.hydra.storage.volume.block.TitanBlockSpannedVolume;
import com.pinecone.hydra.storage.volume.block.TitanStripedVolume;
import com.pinecone.hydra.storage.volume.object.ObjectVolume;
import com.pinecone.hydra.storage.volume.object.TitanObjectSimpleVolume;
import com.pinecone.hydra.storage.volume.object.TitanObjectSpannedVolume;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeEvent;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeExtentRole;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.core.VolumeType;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.io.LocalDirectoryPhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.LocalFilePhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.LocalObjectDirectoryPhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;
import com.pinecone.hydra.storage.volume.source.VolumeExtentManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeEventManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UniformVolumeManager implements VolumeManager {
    protected final Map<GUID, PhysicalAccessor> mPhysicalAccessors;
    protected final Map<GUID, Volume>           mVolumes;
    protected VolumeManipulator                 mVolumeManipulator;
    protected VolumePhysicalManipulator         mPhysicalManipulator;
    protected VolumeExtentManipulator           mExtentManipulator;
    protected VolumeEventManipulator            mEventManipulator;
    protected VolumeConfig                      mConfig;

    public UniformVolumeManager() {
        this.mPhysicalAccessors = new LinkedHashMap<>();
        this.mVolumes           = new LinkedHashMap<>();
        this.mConfig            = new KernelVolumeConfig();
    }

    public UniformVolumeManager( VolumeConfig config ) {
        this();
        this.mConfig = config == null ? new KernelVolumeConfig() : config;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator
    ) {
        this();
        this.mVolumeManipulator   = volumeManipulator;
        this.mPhysicalManipulator = physicalManipulator;
        this.mExtentManipulator   = extentManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeEventManipulator eventManipulator
    ) {
        this( volumeManipulator, physicalManipulator, extentManipulator );
        this.mEventManipulator = eventManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeConfig config
    ) {
        this( config );
        this.mVolumeManipulator   = volumeManipulator;
        this.mPhysicalManipulator = physicalManipulator;
        this.mExtentManipulator   = extentManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeEventManipulator eventManipulator,
            VolumeConfig config
    ) {
        this( volumeManipulator, physicalManipulator, extentManipulator, config );
        this.mEventManipulator = eventManipulator;
    }

    public UniformVolumeManager( KOIMappingDriver driver, VolumeConfig config ) {
        this( config );
        VolumeMasterManipulator masterManipulator = (VolumeMasterManipulator) driver.getMasterManipulator();
        this.mVolumeManipulator = masterManipulator.getVolumeManipulator();
        this.mPhysicalManipulator = masterManipulator.getPhysicalManipulator();
        this.mExtentManipulator = masterManipulator.getExtentManipulator();
        this.mEventManipulator = masterManipulator.getEventManipulator();
    }

    @Override
    public VolumeConfig getConfig() {
        return this.mConfig;
    }

    public void setVolumeManipulator( VolumeManipulator volumeManipulator ) {
        this.mVolumeManipulator = volumeManipulator;
    }

    public void setPhysicalManipulator( VolumePhysicalManipulator physicalManipulator ) {
        this.mPhysicalManipulator = physicalManipulator;
    }

    public void setExtentManipulator( VolumeExtentManipulator extentManipulator ) {
        this.mExtentManipulator = extentManipulator;
    }

    public void setEventManipulator( VolumeEventManipulator eventManipulator ) {
        this.mEventManipulator = eventManipulator;
    }

    @Override
    public void registerPhysical( PhysicalAccessor physicalAccessor ) {
        this.mPhysicalAccessors.put( physicalAccessor.getGuid(), physicalAccessor );
    }

    @Override
    public void persistPhysical( VolumePhysical physical ) {
        if ( this.mPhysicalManipulator == null ) {
            return;
        }
        if ( this.mPhysicalManipulator.get( physical.getGuid() ) == null ) {
            this.mPhysicalManipulator.insert( physical );
        }
        else {
            this.mPhysicalManipulator.update( physical );
        }
    }

    @Override
    public void registerVolume( Volume volume ) {
        this.mVolumes.put( volume.getGuid(), volume );
    }

    @Override
    public Optional<PhysicalAccessor> findPhysical( GUID guid ) {
        return Optional.ofNullable( this.mPhysicalAccessors.get( guid ) );
    }

    @Override
    public Optional<Volume> findVolume( GUID guid ) {
        return Optional.ofNullable( this.mVolumes.get( guid ) );
    }

    @Override
    public Volume affirmVolume( GUID guid ) {
        Volume volume = this.mVolumes.get( guid );
        if ( volume == null ) {
            throw new IllegalArgumentException( "Volume not found: " + guid );
        }
        return volume;
    }

    @Override
    public SimpleVolume createSimpleVolume( GUID guid, String name, VolumeExtent backingExtent ) {
        backingExtent.setParentGuid( guid );
        backingExtent.setRole( VolumeExtentRole.SIMPLE_BACKING );
        this.checkPhysicalExtentAvailable( backingExtent );
        SimpleVolume volume;
        if ( backingExtent.getPhysicalAccessor() instanceof LocalObjectDirectoryPhysicalAccessor ) {
            volume = new TitanObjectSimpleVolume( guid, name, backingExtent, this.mConfig );
            ( (ArchVolume) volume ).setObjectRoot(
                    Paths.get( ( (LocalObjectDirectoryPhysicalAccessor) backingExtent.getPhysicalAccessor() ).getRootPath().toString() ).toString()
            );
        }
        else {
            volume = new TitanBlockSimpleVolume( guid, name, backingExtent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public SpannedVolume createSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents ) {
        List<VolumeExtent> extentList = new ArrayList<>();
        boolean objectSpanned = false;
        for ( VolumeExtent extent : extents ) {
            extentList.add( extent );
            objectSpanned = objectSpanned || extent.getChildVolume() instanceof ObjectVolume;
        }
        SpannedVolume volume = objectSpanned
                ? new TitanObjectSpannedVolume( guid, name )
                : new TitanBlockSpannedVolume( guid, name );
        for ( VolumeExtent extent : extentList ) {
            extent.setParentGuid( guid );
            extent.setRole( VolumeExtentRole.SPANNED_EXTENT );
            volume.addExtent( extent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public StripedVolume createStripedVolume( GUID guid, String name, long stripeUnit, Iterable<VolumeExtent> members ) {
        StripedVolume volume = new TitanStripedVolume( guid, name, stripeUnit );
        for ( VolumeExtent extent : members ) {
            extent.setParentGuid( guid );
            extent.setRole( VolumeExtentRole.STRIPED_MEMBER );
            volume.addMember( extent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public int read( GUID volumeGuid, long position, ByteBuffer dst ) throws IOException {
        return this.affirmBlockVolume( volumeGuid ).read( position, dst );
    }

    @Override
    public int write( GUID volumeGuid, long position, ByteBuffer src ) throws IOException {
        BlockVolume volume = this.affirmBlockVolume( volumeGuid );
        int written = volume.write( position, src );
        this.syncCommittedBytes( volume );
        return written;
    }

    @Override
    public BlockVolume affirmBlockVolume( GUID guid ) {
        Volume volume = this.affirmVolume( guid );
        if ( !( volume instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Volume is not block-addressable: " + guid );
        }
        return (BlockVolume) volume;
    }

    @Override
    public void refreshVolumeUsage( GUID volumeGuid ) throws IOException {
        Volume volume = this.loadVolume( volumeGuid );
        long committedBytes = this.syncCommittedBytes( volume );
        this.emitVolumeEvent(
                volume,
                "VOLUME_USAGE_REFRESHED",
                "SUCCESS",
                "{\"committedBytes\":" + committedBytes + ",\"logicalSize\":" + volume.getLogicalSize() + "}"
        );
    }

    @Override
    public void flush( GUID volumeGuid ) throws IOException {
        this.affirmVolume( volumeGuid ).flush();
    }

    @Override
    public Volume loadVolume( GUID guid ) throws IOException {
        Volume cachedVolume = this.mVolumes.get( guid );
        if ( cachedVolume != null ) {
            return cachedVolume;
        }
        if ( this.mVolumeManipulator == null || this.mExtentManipulator == null ) {
            throw new IllegalStateException( "Volume persistence manipulators are not configured" );
        }
        VolumeRecord record = this.mVolumeManipulator.get( guid );
        if ( record == null ) {
            throw new IllegalArgumentException( "Volume record not found: " + guid );
        }
        VolumeType volumeType = VolumeType.valueOf( record.getVolumeType() );
        List<VolumeExtent> extents = this.mExtentManipulator.listByParentGuid( guid );
        switch ( volumeType ) {
            case SIMPLE: {
                if ( extents.isEmpty() ) {
                    throw new IllegalStateException( "Simple volume has no backing extent: " + guid );
                }
                VolumeExtent extent = extents.get( 0 );
                extent.setPhysicalAccessor( this.loadPhysicalAccessor( extent.getPhysicalGuid(), record ) );
                SimpleVolume volume = VolumeMappingMode.VOLUME_DIRECT_OBJECT.name().equals( record.getMappingMode() )
                        ? new TitanObjectSimpleVolume( record.getGuid(), record.getName(), extent, this.mConfig )
                        : new TitanBlockSimpleVolume( record.getGuid(), record.getName(), extent );
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                return volume;
            }
            case SPANNED: {
                SpannedVolume volume = VolumeMappingMode.VOLUME_DIRECT_OBJECT.name().equals( record.getMappingMode() )
                        ? new TitanObjectSpannedVolume( record.getGuid(), record.getName() )
                        : new TitanBlockSpannedVolume( record.getGuid(), record.getName() );
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                for ( VolumeExtent extent : extents ) {
                    extent.setChildVolume( this.loadVolume( extent.getChildGuid() ) );
                    volume.addExtent( extent );
                }
                return volume;
            }
            case STRIPED: {
                long stripeUnit = record.getStripeUnit() == null ? 0L : record.getStripeUnit();
                StripedVolume volume = new TitanStripedVolume( record.getGuid(), record.getName(), stripeUnit );
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                for ( VolumeExtent extent : extents ) {
                    extent.setChildVolume( this.loadVolume( extent.getChildGuid() ) );
                    volume.addMember( extent );
                }
                return volume;
            }
            default: {
                throw new IllegalArgumentException( "Unsupported volume type: " + volumeType );
            }
        }
    }

    @Override
    public List<Volume> listVolumes() throws IOException {
        if ( this.mVolumeManipulator == null ) {
            return new ArrayList<>( this.mVolumes.values() );
        }
        List<Volume> volumes = new ArrayList<>();
        for ( VolumeRecord record : this.mVolumeManipulator.listAll() ) {
            volumes.add( this.loadVolume( record.getGuid() ) );
        }
        return volumes;
    }

    protected void persistVolume( Volume volume ) {
        if ( this.mVolumeManipulator == null ) {
            return;
        }
        VolumeRecord record = VolumeRecord.fromVolume( volume );
        if ( this.mVolumeManipulator.get( volume.getGuid() ) == null ) {
            this.mVolumeManipulator.insert( record );
        }
        else {
            this.mVolumeManipulator.update( record );
        }
        this.persistExtents( volume );
    }

    protected void applyRecord( Volume volume, VolumeRecord record ) {
        if ( !( volume instanceof ArchVolume ) ) {
            return;
        }
        ArchVolume archVolume = (ArchVolume) volume;
        if ( record.getMappingMode() != null ) {
            archVolume.setMappingMode( VolumeMappingMode.valueOf( record.getMappingMode() ) );
        }
        if ( record.getAllocationMode() != null ) {
            archVolume.setAllocationMode( VolumeAllocationMode.valueOf( record.getAllocationMode() ) );
        }
        archVolume.setObjectRoot( record.getObjectRoot() );
        archVolume.setCommittedBytes( record.getCommittedBytes() );
        archVolume.setAllocationUnit( record.getAllocationUnit() );
    }

    protected long syncCommittedBytes( Volume volume ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        if ( volume instanceof SimpleVolume ) {
            return this.syncSimpleCommittedBytes( (SimpleVolume) volume );
        }
        if ( volume instanceof SpannedVolume ) {
            return this.syncCompositeCommittedBytes( volume, ( (SpannedVolume) volume ).getExtents() );
        }
        if ( volume instanceof StripedVolume ) {
            return this.syncCompositeCommittedBytes( volume, ( (StripedVolume) volume ).getMembers() );
        }
        return volume.getCommittedBytes();
    }

    protected long syncSimpleCommittedBytes( SimpleVolume volume ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        ArchVolume archVolume = (ArchVolume) volume;
        VolumeExtent backingExtent = volume.getBackingExtent();
        PhysicalAccessor physicalAccessor = backingExtent == null ? null : backingExtent.getPhysicalAccessor();
        if ( physicalAccessor == null ) {
            return archVolume.getCommittedBytes();
        }
        long committedBytes = physicalAccessor.getCommittedBytes();
        archVolume.setCommittedBytes( committedBytes );
        VolumeAllocationMode beforeMode = archVolume.getAllocationMode();
        if (
                archVolume.getAllocationMode() == VolumeAllocationMode.THIN
                        && committedBytes >= archVolume.getLogicalSize()
        ) {
            archVolume.setAllocationMode( VolumeAllocationMode.THICK );
            this.emitThinPromotionEvent( archVolume, beforeMode, committedBytes );
        }
        this.persistPhysicalUsage( backingExtent.getPhysicalGuid(), committedBytes );
        this.persistVolumeRecord( archVolume );
        return committedBytes;
    }

    protected long syncCompositeCommittedBytes( Volume volume, Iterable<VolumeExtent> extents ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        long committedBytes = 0L;
        for ( VolumeExtent extent : extents ) {
            Volume childVolume = extent.getChildVolume();
            if ( childVolume == null && extent.getChildGuid() != null ) {
                childVolume = this.loadVolume( extent.getChildGuid() );
                extent.setChildVolume( childVolume );
            }
            if ( childVolume != null ) {
                committedBytes += this.syncCommittedBytes( childVolume );
            }
        }
        ArchVolume archVolume = (ArchVolume) volume;
        archVolume.setCommittedBytes( committedBytes );
        VolumeAllocationMode beforeMode = archVolume.getAllocationMode();
        if (
                archVolume.getAllocationMode() == VolumeAllocationMode.THIN
                        && committedBytes >= archVolume.getLogicalSize()
        ) {
            archVolume.setAllocationMode( VolumeAllocationMode.THICK );
            this.emitThinPromotionEvent( archVolume, beforeMode, committedBytes );
        }
        this.persistVolumeRecord( archVolume );
        return committedBytes;
    }

    protected void persistVolumeRecord( Volume volume ) {
        if ( this.mVolumeManipulator == null ) {
            return;
        }
        VolumeRecord record = VolumeRecord.fromVolume( volume );
        if ( this.mVolumeManipulator.get( volume.getGuid() ) == null ) {
            this.mVolumeManipulator.insert( record );
        }
        else {
            this.mVolumeManipulator.update( record );
        }
    }

    protected void persistPhysicalUsage( GUID physicalGuid, long usedBytes ) {
        if ( this.mPhysicalManipulator == null || physicalGuid == null ) {
            return;
        }
        VolumePhysical physical = this.mPhysicalManipulator.get( physicalGuid );
        if ( physical == null ) {
            return;
        }
        physical.setUsedBytes( usedBytes );
        this.mPhysicalManipulator.update( physical );
    }

    protected void persistExtents( Volume volume ) {
        if ( this.mExtentManipulator == null ) {
            return;
        }
        this.mExtentManipulator.removeByParentGuid( volume.getGuid() );
        if ( volume instanceof SimpleVolume ) {
            this.mExtentManipulator.insert( ( (SimpleVolume) volume ).getBackingExtent() );
        }
        else if ( volume instanceof SpannedVolume ) {
            for ( VolumeExtent extent : ( (SpannedVolume) volume ).getExtents() ) {
                this.mExtentManipulator.insert( extent );
            }
        }
        else if ( volume instanceof StripedVolume ) {
            for ( VolumeExtent extent : ( (StripedVolume) volume ).getMembers() ) {
                this.mExtentManipulator.insert( extent );
            }
        }
    }

    protected void emitThinPromotionEvent( Volume volume, VolumeAllocationMode beforeMode, long committedBytes ) {
        if ( beforeMode != VolumeAllocationMode.THIN || volume.getAllocationMode() != VolumeAllocationMode.THICK ) {
            return;
        }
        this.emitVolumeEvent(
                volume,
                "VOLUME_ALLOCATION_PROMOTED",
                "SUCCESS",
                "{"
                        + "\"fromMode\":\"THIN\","
                        + "\"toMode\":\"THICK\","
                        + "\"committedBytes\":" + committedBytes + ","
                        + "\"logicalSize\":" + volume.getLogicalSize() + ","
                        + "\"reason\":\"COMMITTED_REACHED_LOGICAL_SIZE\""
                        + "}"
        );
    }

    protected void emitVolumeEvent( Volume volume, String eventType, String eventStatus, String payload ) {
        if ( this.mEventManipulator == null || volume == null ) {
            return;
        }
        VolumeEvent event = new VolumeEvent();
        event.setGuid( GUIDs.GUID128( UUID.randomUUID().toString() ) );
        event.setVolumeGuid( volume.getGuid() );
        event.setEventType( eventType );
        event.setEventStatus( eventStatus );
        event.setEventPayload( payload );
        this.mEventManipulator.insert( event );
    }

    protected String volumePayload( Volume volume ) {
        return "{"
                + "\"volumeType\":\"" + volume.getVolumeType() + "\","
                + "\"mappingMode\":\"" + volume.getMappingMode() + "\","
                + "\"allocationMode\":\"" + volume.getAllocationMode() + "\","
                + "\"logicalSize\":" + volume.getLogicalSize() + ","
                + "\"committedBytes\":" + volume.getCommittedBytes()
                + "}";
    }

    protected PhysicalAccessor loadPhysicalAccessor( GUID physicalGuid ) throws IOException {
        return this.loadPhysicalAccessor( physicalGuid, null );
    }

    protected PhysicalAccessor loadPhysicalAccessor( GUID physicalGuid, VolumeRecord volumeRecord ) throws IOException {
        PhysicalAccessor cachedAccessor = this.mPhysicalAccessors.get( physicalGuid );
        if ( cachedAccessor != null ) {
            return cachedAccessor;
        }
        if ( this.mPhysicalManipulator == null ) {
            throw new IllegalStateException( "Volume physical manipulator is not configured" );
        }
        VolumePhysical physical = this.mPhysicalManipulator.get( physicalGuid );
        if ( physical == null ) {
            throw new IllegalArgumentException( "Physical record not found: " + physicalGuid );
        }
        PhysicalAccessor accessor;
        VolumeAllocationMode allocationMode = volumeRecord == null || volumeRecord.getAllocationMode() == null
                ? VolumeAllocationMode.THICK
                : VolumeAllocationMode.valueOf( volumeRecord.getAllocationMode() );
        long allocationUnit = volumeRecord == null
                ? this.mConfig.getDefaultAllocationUnit()
                : volumeRecord.getAllocationUnit();
        if ( physical.getPhysicalType() == VolumePhysicalType.LOCAL_DIR ) {
            if (
                    volumeRecord != null
                            && VolumeMappingMode.VOLUME_DIRECT_OBJECT.name().equals( volumeRecord.getMappingMode() )
            ) {
                accessor = new LocalObjectDirectoryPhysicalAccessor(
                        physical.getGuid(),
                        physical.getName(),
                        Paths.get( physical.getRootPath() ),
                        physical.getCapacityBytes(),
                        this.mConfig
                );
            }
            else {
                accessor = new LocalDirectoryPhysicalAccessor(
                        physical.getGuid(),
                        physical.getName(),
                        Paths.get( physical.getRootPath() ),
                        physical.getCapacityBytes(),
                        allocationMode,
                        allocationUnit,
                        this.mConfig
                );
            }
        }
        else if ( physical.getPhysicalType() == VolumePhysicalType.LOCAL_FILE ) {
            accessor = new LocalFilePhysicalAccessor(
                    physical.getGuid(),
                    physical.getName(),
                    Paths.get( physical.getRootPath() ),
                    physical.getCapacityBytes(),
                    allocationMode,
                    allocationUnit
            );
        }
        else {
            throw new IllegalArgumentException( "Unsupported physical type: " + physical.getPhysicalType() );
        }
        this.registerPhysical( accessor );
        return accessor;
    }

    protected void checkPhysicalExtentAvailable( VolumeExtent targetExtent ) {
        if ( this.mExtentManipulator == null || targetExtent.getPhysicalGuid() == null ) {
            return;
        }
        List<VolumeExtent> extents = this.mExtentManipulator.listByPhysicalGuid( targetExtent.getPhysicalGuid() );
        extents.sort( Comparator.comparingLong( VolumeExtent::getPhysicalOffset ) );
        long targetStart = targetExtent.getPhysicalOffset();
        long targetEnd = targetStart + targetExtent.getLength();
        for ( VolumeExtent extent : extents ) {
            long start = extent.getPhysicalOffset();
            long end = start + extent.getLength();
            if ( targetStart < end && targetEnd > start ) {
                throw new IllegalStateException(
                        "Physical extent overlaps: " + targetExtent.getPhysicalGuid() + " [" + targetStart + ", " + targetEnd + ")"
                );
            }
        }
    }
}


