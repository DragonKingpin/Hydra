package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TasksInstrument;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;

import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GenericElementOperatorFactory implements ElementOperatorFactory{
    protected TaskMasterManipulator serviceMasterManipulator;
    protected TasksInstrument tasksInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();
    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespace.class );
        this.registerDefaultMetaType( GenericTaskElement.class );
        /*this.registerDefaultMetaType( GenericApplicationElement.class );*/
    }

    public GenericElementOperatorFactory(TasksInstrument tasksInstrument, TaskMasterManipulator serviceMasterManipulator ){
        this.tasksInstrument = tasksInstrument;
        this.serviceMasterManipulator = serviceMasterManipulator;

        this.registerer.put(
                ElementOperatorFactory.DefaultTaskNode,
                new TaskElementOperator( this )
        );
/*

        this.registerer.put(
                ElementOperatorFactory.DefaultApplicationNode,
                new ApplicationElementOperator(this)
        );
*/

        this.registerer.put(
                ElementOperatorFactory.DefaultNamespace,
                new NamespaceOperator(this)
        );

        this.registerDefaultMetaTypes();
    }
    @Override
    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public void registerMetaType( Class<?> clazz, String metaType ){
        this.registerMetaType( clazz.getName(), metaType );
    }

    @Override
    public void registerMetaType( String classFullName, String metaType ){
        this.metaTypeMap.put( classFullName, metaType );
    }

    @Override
    public TasksInstrument getTasksTree() {
        return this.tasksInstrument;
    }

    @Override
    public TaskMasterManipulator getTaskMasterManipulator() {
        return this.serviceMasterManipulator;
    }

    @Override
    public String getMetaType( String classFullName ) {
        return this.metaTypeMap.get( classFullName );
    }

    @Override
    public ElementOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (ElementOperator) this.registerer.get( typeName );
    }
}
