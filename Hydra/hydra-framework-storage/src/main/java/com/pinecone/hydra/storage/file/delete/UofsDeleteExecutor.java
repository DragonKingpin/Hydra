package com.pinecone.hydra.storage.file.delete;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.external.ExternalFile;
import com.pinecone.hydra.storage.file.external.ExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.external.ExternalFolder;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class UofsDeleteExecutor implements Pinenut {
    protected final KOMFileSystem fileSystem;
    protected final ExternalFileSystemInstrument externalFileSystemInstrument;
    protected final UofsDirectGuidResolver directGuidResolver;
    protected final UofsPathCacheEraser pathCacheEraser;
    protected final UofsFileDataDeleter fileDataDeleter;
    protected final UofsMetadataDeleteOperator metadataDeleteOperator;
    protected final UofsDeletePolicy policy;

    public UofsDeleteExecutor(
            KOMFileSystem fileSystem,
            ExternalFileSystemInstrument externalFileSystemInstrument,
            UofsDirectGuidResolver directGuidResolver,
            UofsPathCacheEraser pathCacheEraser,
            UofsFileDataDeleter fileDataDeleter,
            UofsMetadataDeleteOperator metadataDeleteOperator,
            UofsDeletePolicy policy
    ) {
        this.fileSystem = fileSystem;
        this.externalFileSystemInstrument = externalFileSystemInstrument;
        this.directGuidResolver = directGuidResolver;
        this.pathCacheEraser = pathCacheEraser;
        this.fileDataDeleter = fileDataDeleter;
        this.metadataDeleteOperator = metadataDeleteOperator;
        this.policy = policy == null ? UofsDeletePolicy.strict() : policy;
    }

    public void remove( String path ) {
        UofsDeletePlan plan = this.collect( path );
        if ( plan == null ) {
            if ( !this.policy.getNoopMissing() ) {
                throw new IllegalArgumentException( "Undefined UOFS path: " + path );
            }
            return;
        }
        this.execute( plan );
    }

    public void remove( GUID guid ) {
        UofsDeletePlan plan = this.collect( guid );
        if ( plan == null ) {
            if ( !this.policy.getNoopMissing() ) {
                throw new IllegalArgumentException( "Undefined UOFS node: " + guid );
            }
            return;
        }
        this.execute( plan );
    }

    protected UofsDeletePlan collect( String path ) {
        UofsDeletePlan plan = new UofsDeletePlan( null );
        plan.add( UofsDeletePlanItem.pathCache( path ) );

        GUID directGuid = this.directGuidResolver == null ? null : this.directGuidResolver.query( path );
        if ( directGuid != null ) {
            this.collect( directGuid, this.queryParentGuid( path ), plan, 0 );
            return plan;
        }

        GUID resolvedGuid = this.fileSystem.queryGUIDByPath( path );
        if ( resolvedGuid != null ) {
            this.collect( resolvedGuid, this.queryParentGuid( path ), plan, 0 );
            return plan;
        }

        ElementNode externalNode = this.externalFileSystemInstrument == null
                ? null
                : this.externalFileSystemInstrument.queryElement( path );
        if ( externalNode instanceof ExternalFile || externalNode instanceof ExternalFolder ) {
            plan.add( UofsDeletePlanItem.nativeExternalTarget( path, externalNode ) );
            return plan;
        }

        return null;
    }

    protected UofsDeletePlan collect( GUID rootGuid ) {
        if ( rootGuid == null ) {
            return null;
        }
        TreeNode root = this.fileSystem.get( rootGuid );
        if ( root == null ) {
            return null;
        }
        UofsDeletePlan plan = new UofsDeletePlan( rootGuid );
        this.collect( rootGuid, null, plan, 0 );
        return plan;
    }

    protected void collect( GUID guid, GUID parentGuid, UofsDeletePlan plan, int depth ) {
        TreeNode node = this.fileSystem.get( guid );
        if ( node == null ) {
            if ( !this.policy.getNoopMissing() && !this.policy.getIgnoreMissingMetadata() ) {
                throw new IllegalArgumentException( "Undefined UOFS node: " + guid );
            }
            return;
        }
        if ( parentGuid != null && this.hasMultipleParents( guid ) ) {
            plan.add( UofsDeletePlanItem.hardlinkEdge( parentGuid, guid, depth ) );
            return;
        }
        if ( node instanceof FileNode ) {
            plan.add( UofsDeletePlanItem.fileData( (FileNode) node, depth ) );
            plan.add( UofsDeletePlanItem.fileMetadata( (FileNode) node, depth ) );
            return;
        }
        if ( node instanceof ExternalSymbolic ) {
            plan.add( UofsDeletePlanItem.externalSymbolicMetadata( node, depth ) );
            return;
        }
        if ( node instanceof ElementNode && ( (ElementNode) node ).evinceSymbolic() != null ) {
            plan.add( UofsDeletePlanItem.internalSymbolicMetadata( node, depth ) );
            return;
        }
        if ( node instanceof Folder ) {
            this.collectFolderChildren( guid, plan, depth + 1 );
            plan.add( UofsDeletePlanItem.folderMetadata( node, depth ) );
        }
    }

    protected void collectFolderChildren( GUID folderGuid, UofsDeletePlan plan, int depth ) {
        for ( TreeNode child : this.fileSystem.getChildren( folderGuid ) ) {
            if ( child == null ) {
                continue;
            }
            this.collect( child.getGuid(), folderGuid, plan, depth );
        }
    }

    protected void execute( UofsDeletePlan plan ) {
        this.executePathCache( plan );
        this.executeFileData( plan );
        this.executeNativeExternalTargets( plan );
        this.executeHardlinkEdges( plan );
        this.executeMetadata( plan, UofsDeleteTargetType.INTERNAL_SYMBOLIC_METADATA );
        this.executeMetadata( plan, UofsDeleteTargetType.EXTERNAL_SYMBOLIC_METADATA );
        this.executeMetadata( plan, UofsDeleteTargetType.FILE_METADATA );
        this.executeMetadata( plan, UofsDeleteTargetType.FOLDER_METADATA );
    }

    protected void executePathCache( UofsDeletePlan plan ) {
        if ( this.pathCacheEraser == null ) {
            return;
        }
        for ( UofsDeletePlanItem item : plan.getItemsByTargetType( UofsDeleteTargetType.PATH_CACHE ) ) {
            if ( item.getPath() != null ) {
                this.pathCacheEraser.erase( item.getPath() );
            }
        }
    }

    protected void executeFileData( UofsDeletePlan plan ) {
        if ( this.fileDataDeleter == null ) {
            return;
        }
        for ( UofsDeletePlanItem item : plan.getItemsByTargetType( UofsDeleteTargetType.FILE_DATA ) ) {
            if ( item.getFileNode() == null ) {
                continue;
            }
            try {
                this.fileDataDeleter.delete( item.getFileNode() );
            }
            catch ( IOException | RuntimeException e ) {
                if ( !this.policy.getIgnoreMissingData() ) {
                    throw new IllegalStateException( "Failed to delete UOFS file data: " + item.getGuid(), e );
                }
            }
        }
    }

    protected void executeNativeExternalTargets( UofsDeletePlan plan ) {
        if ( this.externalFileSystemInstrument == null ) {
            return;
        }
        for ( UofsDeletePlanItem item : plan.getItemsByTargetType( UofsDeleteTargetType.NATIVE_EXTERNAL_TARGET ) ) {
            boolean removed = item.getPath() != null && this.externalFileSystemInstrument.remove( item.getPath() );
            if ( !removed && !this.policy.getIgnoreMissingNativeTarget() ) {
                throw new IllegalArgumentException( "Failed to remove UOFS native external path: " + item.getPath() );
            }
        }
    }

    protected void executeHardlinkEdges( UofsDeletePlan plan ) {
        for ( UofsDeletePlanItem item : plan.getItemsByTargetType( UofsDeleteTargetType.HARDLINK_EDGE ) ) {
            try {
                this.metadataDeleteOperator.unlink( item.getParentGuid(), item.getGuid() );
            }
            catch ( RuntimeException e ) {
                if ( !this.policy.getIgnoreMissingMetadata() ) {
                    throw e;
                }
            }
        }
    }

    protected void executeMetadata( UofsDeletePlan plan, UofsDeleteTargetType targetType ) {
        List<UofsDeletePlanItem> items = plan.getItemsByTargetType( targetType );
        items.sort( Comparator.comparingInt( UofsDeletePlanItem::getDepth ).reversed() );
        for ( UofsDeletePlanItem item : items ) {
            try {
                this.executeMetadata( item );
            }
            catch ( RuntimeException e ) {
                if ( !this.policy.getIgnoreMissingMetadata() ) {
                    throw e;
                }
            }
        }
    }

    protected void executeMetadata( UofsDeletePlanItem item ) {
        if ( item.getGuid() == null ) {
            return;
        }
        if ( item.getTargetType() == UofsDeleteTargetType.FILE_METADATA ) {
            this.metadataDeleteOperator.removeFile( item.getGuid() );
        }
        else if ( item.getTargetType() == UofsDeleteTargetType.FOLDER_METADATA ) {
            this.metadataDeleteOperator.removeFolder( item.getGuid() );
        }
        else if ( item.getTargetType() == UofsDeleteTargetType.INTERNAL_SYMBOLIC_METADATA ) {
            this.metadataDeleteOperator.removeInternalSymbolic( item.getGuid() );
        }
        else if ( item.getTargetType() == UofsDeleteTargetType.EXTERNAL_SYMBOLIC_METADATA ) {
            this.metadataDeleteOperator.removeExternalSymbolic( item.getGuid() );
        }
    }

    protected boolean hasMultipleParents( GUID guid ) {
        if ( guid == null ) {
            return false;
        }
        List<GUID> parentGuids = this.fileSystem.getMasterTrieTree().fetchParentGuids( guid );
        return parentGuids != null && parentGuids.size() > 1;
    }

    protected GUID queryParentGuid( String path ) {
        String parentPath = this.parentPath( path );
        if ( parentPath == null ) {
            return null;
        }
        return this.fileSystem.queryGUIDByPath( parentPath );
    }

    protected String parentPath( String path ) {
        if ( path == null || path.isBlank() ) {
            return null;
        }
        String separator = this.fileSystem.getConfig().getPathNameSeparator();
        String normalized = path;
        while ( normalized.length() > separator.length() && normalized.endsWith( separator ) ) {
            normalized = normalized.substring( 0, normalized.length() - separator.length() );
        }
        int index = normalized.lastIndexOf( separator );
        if ( index <= 0 ) {
            return separator;
        }
        return normalized.substring( 0, index );
    }
}
