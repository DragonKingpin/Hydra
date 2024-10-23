package com.pinecone.hydra.storage.volume.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

import java.time.LocalDateTime;

public abstract class ArchVolumeOperator implements VolumeOperator{
    protected VolumeTree                    volumeTree;
    protected VolumeOperatorFactory         factory;
    protected DistributedTrieTree           distributedTrieTree;
    protected VolumeMasterManipulator       volumeMasterManipulator;
    protected VolumeCapacityManipulator     volumeCapacityManipulator;

    public ArchVolumeOperator( VolumeMasterManipulator masterManipulator, VolumeTree volumeTree ){
        this.distributedTrieTree       =  volumeTree.getMasterTrieTree();
        this.volumeTree                =  volumeTree;
        this.volumeMasterManipulator   =  masterManipulator;
        this.volumeCapacityManipulator =  masterManipulator.getVolumeCapacityManipulator();
    }

    protected DistributedTreeNode affirmPreinsertionInitialize( LogicVolume volume ){
        GUID guid = volume.getGuid();
        volume.setUpdateTime( LocalDateTime.now() );
        DistributedTreeNode distributedTreeNode = new GUIDDistributedTrieNode();
        distributedTreeNode.setGuid( guid );
        distributedTreeNode.setType( UOIUtils.createLocalJavaClass( volume.getClass().getName() ) );

        return distributedTreeNode;
    }

    public VolumeOperatorFactory  getVolumeOperatorFactory(){
        return this.factory;
    }
}
