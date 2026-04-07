package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface ElementOperatorFactory extends OperatorFactory {
    String DefaultServiceNode     =  TaskElement.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    String DefaultApplicationNode =  AppElement.class.getSimpleName();

    void register(String typeName, TreeNodeOperator functionalNodeOperation);

    void registerMetaType(Class<?> clazz, String metaType);

    void registerMetaType(String classFullName, String metaType);

    String getMetaType(String classFullName);

    ElementOperator getOperator(String typeName);

    TaskInstrument taskInstrument();

    TaskMasterManipulator getTaskMasterManipulator();

}
