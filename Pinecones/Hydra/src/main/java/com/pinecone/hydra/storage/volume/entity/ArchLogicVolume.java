package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ArchLogicVolume extends ArchVolume implements LogicVolume{

    protected List<LogicVolume>            children;
    protected VolumeCapacity          volumeCapacity;

    public ArchLogicVolume(VolumeTree volumeTree) {
        super(volumeTree);
    }

    public ArchLogicVolume(){}



    @Override
    public List<LogicVolume> getChildren() {
        if ( this.children == null || this.children.isEmpty() ){
            ArrayList<LogicVolume> logicVolumes = new ArrayList<>();
            List<TreeNode> nodes = this.volumeTree.getChildren( this.guid );
            for( TreeNode node : nodes ){
                logicVolumes.add( (LogicVolume) node);
            }
            this.children = logicVolumes;
        }
        return this.children;
    }

    @Override
    public void setChildren(List<LogicVolume> children) {
        this.children = children;
    }

    @Override
    public VolumeCapacity getVolumeCapacity() {
        return this.volumeCapacity;
    }

    @Override
    public void setVolumeCapacity(VolumeCapacity volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }


}
