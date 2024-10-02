package com.pinecone.hydra.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import org.apache.commons.collections.set.ListOrderedSet;

public interface Registry extends KernelObjectInstrument {
    RegistryConfig KernelRegistryConfig = new KernelRegistryConfig();

    RegistryConfig getRegistryConfig();


    RegistryTreeNode getNodeByPath( String path );

    Properties getProperties( String path );

    NamespaceNode getNamespaceNode( String path );

    void remove( String path );

    Collection<Property > fetchProperties( String path );

    TextValue getTextValue( String path );



    List<TreeNode > selectByName( String name );

    void moveTo( String sourcePath, String destinationPath );

    List<RegistryTreeNode> listRoot();



    NamespaceNode  affirmNamespace        ( String path );

    Properties     affirmProperties       ( String path );

    TextConfigNode affirmTextConfig       ( String path );

    Properties putProperties              ( String path, Map<String, Object > properties );

    TextConfigNode putTextValue           ( String path, String format, String value );
}
