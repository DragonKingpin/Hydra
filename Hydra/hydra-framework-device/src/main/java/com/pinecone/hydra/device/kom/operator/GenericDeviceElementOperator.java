package com.pinecone.hydra.device.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class GenericDeviceElementOperator extends ArchElementOperator implements ElementOperator {
    protected GenericDeviceManipulator genericDeviceManipulator;

    public GenericDeviceElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(), factory.getServicesTree() );
        this.factory = factory;
    }

    public GenericDeviceElementOperator( DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument ) {
        super( masterManipulator, deviceInstrument );
        this.genericDeviceManipulator = masterManipulator.getGenericDeviceManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericDeviceElement genericDeviceElement = (GenericDeviceElement) treeNode;
        GUID guid = this.affirmGuid( genericDeviceElement );
        genericDeviceElement.setGuid( guid );
        this.genericDeviceManipulator.insert( genericDeviceElement );

        GUIDImperialTrieNode node = this.newKernelNode( treeNode, guid );
        this.imperialTree.insert( node );
        return guid;
    }

    @Override
    public void purge( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.genericDeviceManipulator.remove( node.getGuid() );
    }

    @Override
    public GenericDeviceElement get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        GenericDeviceElement genericDeviceElement = this.genericDeviceManipulator.getGenericDeviceElement( guid, this.deviceInstrument );
        if ( genericDeviceElement == null ) {
            return null;
        }
        this.applyTreeNode( genericDeviceElement, node );
        return genericDeviceElement;
    }

    @Override
    public GenericDeviceElement get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public GenericDeviceElement getAsRootDepth( GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        GenericDeviceElement genericDeviceElement = (GenericDeviceElement) nodeWideData;
        this.touchForUpdate( genericDeviceElement );
        this.genericDeviceManipulator.update( genericDeviceElement );
        this.imperialTree.removeCachePath( genericDeviceElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }
}
