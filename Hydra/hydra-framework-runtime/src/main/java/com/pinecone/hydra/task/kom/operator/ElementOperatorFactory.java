package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TasksInstrument;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
public interface ElementOperatorFactory extends OperatorFactory {
    String DefaultTaskNode     =  TaskElement.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    /*String DefaultApplicationNode =  ApplicationElement.class.getSimpleName();*/

    void register( String typeName, TreeNodeOperator functionalNodeOperation );

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    ElementOperator getOperator(String typeName );

    TasksInstrument getTasksTree();

    TaskMasterManipulator getTaskMasterManipulator();

}
