package com.pinecone.hydra.task.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.FolderElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public abstract class ArchElementOperator implements ElementOperator {
    protected TaskInstrument                taskInstrument;
    protected ImperialTree                  imperialTree;
    protected TaskMasterManipulator         taskMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(), factory.taskInstrument() );
        this.factory = factory;
    }
    public ArchElementOperator( TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument){
        this.imperialTree = taskInstrument.getMasterTrieTree();
        this.taskInstrument = taskInstrument;
        this.taskMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected void purgeByNodeType( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ElementOperator operator = this.resolveOperator( node );
        if ( operator == this ) {
            this.purge( guid );
            return;
        }
        operator.purge( guid );
    }

    protected boolean isAssignedToThisOperator( GUIDImperialTrieNode node ) {
        return this.resolveOperator( node ) == this;
    }

    protected ElementOperator resolveOperator( GUIDImperialTrieNode node ) {
        UOI uoi = node.getType();
        String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
        if( metaType == null ) {
            TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ TaskInstrument.class }, this.taskInstrument);
            metaType = newInstance.getMetaType();
        }

        ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
        if ( operator == null ) {
            throw new IllegalStateException( "No task element operator for meta type: " + metaType );
        }
        return operator;
    }

    protected boolean isFolderElement( GUIDImperialTrieNode node ) {
        try {
            return FolderElement.class.isAssignableFrom( Class.forName( node.getType().getObjectName() ) );
        } catch ( ClassNotFoundException e ) {
            throw new IllegalStateException( "Task node class is not accessible: " + node.getType().getObjectName(), e );
        }
    }

}
