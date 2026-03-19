package com.pinecone.hydra.task.kom.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericAppElement;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public class GenericElementOperatorFactory implements ElementOperatorFactory {
    protected TaskMasterManipulator taskMasterManipulator;
    protected TaskInstrument taskInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespace.class );
        this.registerDefaultMetaType( GenericTaskElement.class );
        this.registerDefaultMetaType( GenericAppElement.class );
    }

    public GenericElementOperatorFactory(TaskInstrument taskInstrument, TaskMasterManipulator taskMasterManipulator){
        this.taskInstrument = taskInstrument;
        this.taskMasterManipulator = taskMasterManipulator;

        this.registerer.put(
                ElementOperatorFactory.DefaultServiceNode,
                new TaskElementOperator( this )
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultApplicationNode,
                new AppElementOperator(this)
        );

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
    public TaskInstrument getServicesTree() {
        return this.taskInstrument;
    }

    @Override
    public TaskMasterManipulator getTaskMasterManipulator() {
        return this.taskMasterManipulator;
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
