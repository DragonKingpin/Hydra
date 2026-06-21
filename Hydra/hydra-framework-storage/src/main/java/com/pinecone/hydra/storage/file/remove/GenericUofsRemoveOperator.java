package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.Nullable;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.external.ExternalFile;
import com.pinecone.hydra.storage.file.external.ExternalFolder;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class GenericUofsRemoveOperator implements UofsRemoveOperator {
    protected final KOMFileSystem mFileSystem;
    protected final VolumeManager mVolumeManager;

    public GenericUofsRemoveOperator( KOMFileSystem fileSystem, VolumeManager volumeManager ) {
        this.mFileSystem = fileSystem;
        this.mVolumeManager = volumeManager;
    }

    @Override
    public UofsRemovePlan plan( UofsRemoveRequest request ) {
        List<String> paths = this.normalizePaths( request );
        UofsRemovePlan plan = new UofsRemovePlan();
        plan.setSourcePaths( paths );
        long totalCount = 0L;
        boolean dangerous = false;
        for ( String path : paths ) {
            ElementNode node = this.mFileSystem.queryElement( path );
            if ( node == null ) {
                throw new IllegalArgumentException( "UOFS remove source not found: " + path );
            }
            totalCount += this.estimateCount( node );
            dangerous = dangerous || this.isDangerous( node );
        }
        plan.setTotalCount( totalCount );
        plan.setDangerous( dangerous );
        return plan;
    }

    @Override
    public UofsRemoveReport remove( UofsRemoveRequest request, @Nullable UofsRemoveProgressListener listener ) {
        UofsRemovePlan plan = this.plan( request );
        long doneCount = 0L;
        for ( String path : plan.getSourcePaths() ) {
            long currentCount = this.estimatePathCount( path );
            this.notifyProgress( listener, plan.getTotalCount(), doneCount, path, "Removing UOFS path." );
            this.mFileSystem.remove( path, this.mVolumeManager );
            doneCount += Math.max( currentCount, 1L );
            this.notifyProgress( listener, plan.getTotalCount(), doneCount, path, "UOFS path removed." );
        }
        UofsRemoveReport report = new UofsRemoveReport();
        report.setTotalCount( plan.getTotalCount() );
        report.setDoneCount( doneCount );
        report.setFailed( false );
        report.setMessage( "UOFS remove done." );
        return report;
    }

    protected List<String> normalizePaths( UofsRemoveRequest request ) {
        if ( request == null || request.getSourcePaths().isEmpty() ) {
            throw new IllegalArgumentException( "UOFS remove source paths should not be empty." );
        }
        LinkedHashSet<String> dedup = new LinkedHashSet<>();
        for ( String path : request.getSourcePaths() ) {
            String normalized = this.normalizePath( path );
            if ( "/".equals( normalized ) ) {
                throw new IllegalArgumentException( "UOFS remove root path is not allowed." );
            }
            dedup.add( normalized );
        }
        return new ArrayList<>( dedup );
    }

    protected String normalizePath( String path ) {
        if ( path == null || path.isBlank() ) {
            throw new IllegalArgumentException( "UOFS remove path should not be blank." );
        }
        String separator = this.mFileSystem.getConfig().getPathNameSeparator();
        String value = path.trim().replace( "\\", separator );
        while ( value.contains( separator + separator ) ) {
            value = value.replace( separator + separator, separator );
        }
        if ( !value.startsWith( separator ) ) {
            value = separator + value;
        }
        while ( value.length() > separator.length() && value.endsWith( separator ) ) {
            value = value.substring( 0, value.length() - separator.length() );
        }
        return value;
    }

    protected long estimatePathCount( String path ) {
        ElementNode node = this.mFileSystem.queryElement( path );
        return node == null ? 1L : this.estimateCount( node );
    }

    protected long estimateCount( ElementNode node ) {
        if ( node == null ) {
            return 0L;
        }
        if ( node instanceof ExternalFile || node instanceof ExternalFolder ) {
            return 1L;
        }
        if ( node instanceof FileNode ) {
            return 1L;
        }
        if ( !( node instanceof Folder ) ) {
            return 1L;
        }
        long count = 1L;
        for ( TreeNode child : this.mFileSystem.getChildren( node.getGuid() ) ) {
            if ( child instanceof ElementNode ) {
                count += this.estimateCount( (ElementNode) child );
            }
            else if ( child instanceof FileTreeNode ) {
                count += 1L;
            }
        }
        return count;
    }

    protected boolean isDangerous( ElementNode node ) {
        return node instanceof ExternalFile || node instanceof ExternalFolder;
    }

    protected void notifyProgress(
            @Nullable UofsRemoveProgressListener listener,
            long totalCount,
            long doneCount,
            String currentPath,
            String message
    ) {
        if ( listener != null ) {
            listener.onProgress( totalCount, doneCount, currentPath, message );
        }
    }
}
