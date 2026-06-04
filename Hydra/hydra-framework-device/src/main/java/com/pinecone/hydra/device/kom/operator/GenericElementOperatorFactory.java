package com.pinecone.hydra.device.kom.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.pinecone.hydra.device.kom.entity.GenericContainerElement;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericClusterElement;
import com.pinecone.hydra.device.kom.entity.GenericNamespace;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;

public class GenericElementOperatorFactory implements ElementOperatorFactory {
    protected DeviceMasterManipulator deviceMasterManipulator;
    protected DeviceInstrument deviceInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespace.class );
        this.registerDefaultMetaType( GenericClusterElement.class );
        this.registerDefaultMetaType( GenericVirtualMachineElement.class );
        this.registerDefaultMetaType( GenericPhysicalHostElement.class );
        this.registerDefaultMetaType( GenericQueueElement.class);
        this.registerDefaultMetaType( GenericContainerElement.class);
        this.registerDefaultMetaType( GenericDeviceElement.class );
    }

    public GenericElementOperatorFactory(DeviceInstrument deviceInstrument, DeviceMasterManipulator deviceMasterManipulator){
        this.deviceInstrument = deviceInstrument;
        this.deviceMasterManipulator = deviceMasterManipulator;

        this.registerer.put(
                ElementOperatorFactory.DefaultApplicationNode,
                new ClusterElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultNamespace,
                new NamespaceOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultVirtualMachine,
                new VirtualMachineElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultPhysicalHost,
                new PhysicalHostElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultQuickElement,
                new QuickElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultContainerElement,
                new ContainerElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultGenericDeviceElement,
                new GenericDeviceElementOperator(this)
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
    public DeviceInstrument getServicesTree() {
        return this.deviceInstrument;
    }

    @Override
    public DeviceMasterManipulator getTaskMasterManipulator() {
        return this.deviceMasterManipulator;
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
