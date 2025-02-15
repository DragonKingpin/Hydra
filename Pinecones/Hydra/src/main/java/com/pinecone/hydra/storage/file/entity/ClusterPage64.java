package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.source.LocalClusterManipulator;
import com.pinecone.hydra.storage.file.source.RemoteClusterManipulator;

import java.util.List;

public class ClusterPage64 implements ClusterPage {
    protected RemoteClusterManipulator remoteClusterManipulator;

    protected LocalClusterManipulator localClusterManipulator;

    protected List<Cluster > mCurrClusterPage;

    protected int mnPageSize;

    protected int mnPageSum;

    protected int mnCurrPageAt;

    protected long mnClusters;

    protected GUID fileGuid;

    public ClusterPage64(
            RemoteClusterManipulator remoteClusterManipulator, LocalClusterManipulator localClusterManipulator,
            GUID fileGuid, int pageSize
    ) {
        this.fileGuid                 = fileGuid;
        this.localClusterManipulator  = localClusterManipulator;
        this.remoteClusterManipulator = remoteClusterManipulator;
        this.mnPageSize               = pageSize;
        this.mnClusters               = remoteClusterManipulator.countFileClusters( fileGuid );
        this.mnCurrPageAt             = 0;
        this.mnPageSum                = (int) Math.ceil( (double) this.mnClusters / this.mnPageSize );
        this.mCurrClusterPage         = this.loadClusterPage(0);
    }

    public ClusterPage64( RemoteClusterManipulator remoteClusterManipulator, LocalClusterManipulator localClusterManipulator, GUID fileGuid ) {
        this( remoteClusterManipulator, localClusterManipulator, fileGuid, 10 );
    }

    @SuppressWarnings( "unchecked" )
    protected List<Cluster> loadClusterPage( int pageIndex ) {
        return (List) this.remoteClusterManipulator.fetchRemoteClusterByFileGuid(
                this.fileGuid, (long) pageIndex * this.mnPageSize, this.mnPageSize
        );
    }


    @Override
    public int getPageSize() {
        return this.mnPageSize;
    }

    @Override
    public int getPageSum() {
        return this.mnPageSum;
    }

    @Override
    public long getCurrentPage() {
        return this.mnCurrPageAt;
    }

    @Override
    public long getClusters() {
        return this.mnClusters;
    }

    @Override
    public Cluster getCluster( long segId ) {
        if ( segId >= this.mnClusters ) {
            return null;
        }

        if ( !this.isInCurrentPage( segId ) ) {
            this.mnCurrPageAt     = this.calculatePageIndex( segId );
            this.mCurrClusterPage = this.loadClusterPage(this.mnCurrPageAt);
        }

        return this.findClusterInPage(segId);
    }


    @Override
    public LocalCluster getLocalCluster( long segId ) {
        Cluster cluster = this.getCluster( segId );
        if ( cluster instanceof LocalCluster ) {
            return (LocalCluster) cluster;
        }
        else if ( cluster instanceof RemoteCluster ) {
            RemoteCluster remoteCluster = (RemoteCluster) cluster;
            if( remoteCluster.getDeviceGuid().equals( StorageConstants.LocalhostGUID )) {
                return this.localClusterManipulator.getLocalClusterByGuid( remoteCluster.getSegGuid() );
            }
        }

        return null;
    }

    protected boolean isInCurrentPage( long segId ) {
        return segId >= this.mCurrClusterPage.get(0).getSegId() && segId <= this.mCurrClusterPage.get(this.mCurrClusterPage.size() - 1).getSegId();
    }

    protected Cluster findClusterInPage( long segId ) {
        int offset = (int) (segId % this.mnPageSize);

        if ( offset < 0 || offset >= this.mCurrClusterPage.size() ) {
            return null;
        }

        return this.mCurrClusterPage.get( offset );
    }

    protected int calculatePageIndex( long segId ) {
        // pageIndex = segId / pageSize (向下取整)
        return (int) (segId / this.mnPageSize);
    }


}
