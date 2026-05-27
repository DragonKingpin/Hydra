package com.pinecone.hydra.task.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.TaskScheduleType;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class TaskScheduleSemanticValidator implements Pinenut {

    public void validateForPersistence( TreeNode treeNode ) {
        if ( !( treeNode instanceof TaskElement ) ) {
            return;
        }

        this.validate( (TaskElement) treeNode );
    }

    public void validate( TaskElement taskElement ) {
        if ( taskElement == null ) {
            return;
        }

        TaskScheduleType scheduleType = taskElement.getScheduleType();
        if ( scheduleType == null ) {
            throw new IllegalArgumentException( "Task schedule type is required." );
        }

        if ( scheduleType == TaskScheduleType.Cycle && taskElement.getScheduleCycle() == null ) {
            throw new IllegalArgumentException( "Task schedule cycle is required for cycle task." );
        }
    }

}
