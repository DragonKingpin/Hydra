package com.walnut.odin.atlas.graph;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.atlas.mapper.TaskLineageMapper;

public class UniformRuntimeAtlas implements RuntimeAtlasInstrument {

    private final TaskInstrument   mTaskInstrument;
    private final TaskLineageMapper mTaskLineageMapper;

    public UniformRuntimeAtlas( TaskInstrument taskInstrument, TaskLineageMapper taskLineageMapper ) {
        this.mTaskInstrument    = taskInstrument;
        this.mTaskLineageMapper = taskLineageMapper;
    }

    @Override
    public TaskInstrument taskInstrument() {
        return this.mTaskInstrument;
    }

    @Override
    public void addDependency( GUID taskGuid, GUID parentTaskGuid ) {
        this.mTaskLineageMapper.addDependency( taskGuid, parentTaskGuid );
    }

    @Override
    public void removeDependency( GUID taskGuid, GUID parentTaskGuid ) {
        this.mTaskLineageMapper.removeDependency( taskGuid, parentTaskGuid );
    }

    @Override
    public List<GUID> fetchParentTaskGuids( GUID taskGuid ) {
        return this.mTaskLineageMapper.fetchParentTaskGuids( taskGuid );
    }

    @Override
    public List<GUID> fetchChildTaskGuids( GUID taskGuid ) {
        return this.mTaskLineageMapper.fetchChildTaskGuids( taskGuid );
    }

    @Override
    public List<TaskElement> fetchParentTasks( GUID taskGuid ) {
        return this.resolveTasks( this.fetchParentTaskGuids( taskGuid ) );
    }

    @Override
    public List<TaskElement> fetchChildTasks( GUID taskGuid ) {
        return this.resolveTasks( this.fetchChildTaskGuids( taskGuid ) );
    }

    @Override
    public TaskElement queryTaskElementByGuid( GUID taskGuid ) {
        if ( taskGuid == null ) {
            return null;
        }

        TreeNode treeNode = this.mTaskInstrument.get( taskGuid );
        if ( treeNode instanceof TaskElement ) {
            return (TaskElement) treeNode;
        }
        if ( !( treeNode instanceof TaskTreeNode ) ) {
            return null;
        }

        ElementNode elementNode = ( (TaskTreeNode) treeNode ).evinceElementNode();
        if ( elementNode == null ) {
            return null;
        }
        return elementNode.evinceTaskElement();
    }

    @Override
    public long countParents( GUID taskGuid ) {
        return this.mTaskLineageMapper.countParents( taskGuid );
    }

    @Override
    public long countChildren( GUID taskGuid ) {
        return this.mTaskLineageMapper.countChildren( taskGuid );
    }

    protected List<TaskElement> resolveTasks( List<GUID> taskGuids ) {
        List<TaskElement> elements = new ArrayList<>();
        if ( taskGuids == null || taskGuids.isEmpty() ) {
            return elements;
        }

        for ( GUID taskGuid : taskGuids ) {
            TaskElement element = this.queryTaskElementByGuid( taskGuid );
            if ( element != null ) {
                elements.add( element );
            }
        }
        return elements;
    }
}
