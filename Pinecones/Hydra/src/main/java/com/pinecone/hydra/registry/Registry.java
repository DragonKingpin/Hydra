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
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface Registry extends KernelObjectInstrument {
    RegistryConfig KernelRegistryConfig = new KernelRegistryConfig();

    RegistryConfig getRegistryConfig();


    RegistryTreeNode queryTreeNode( String path );

    Properties getProperties( String path );

    NamespaceNode getNamespaceNode( String path );

    void remove( String path );

    Collection<Property > fetchProperties( String path );

    TextValue getTextValue( String path );



    /** Normal Tree Node or ReparseLinkNode**/
    EntityNode queryNode( String path );

    ReparseLinkNode queryReparseLink( String path );

    List<TreeNode > selectByName( String name );

    void moveTo( String sourcePath, String destinationPath );

    /** Ensure destination path existed.*/
    void move( String sourcePath, String destinationPath );

    List<RegistryTreeNode> listRoot();



    NamespaceNode  affirmNamespace        ( String path );

    Properties     affirmProperties       ( String path );

    TextConfigNode affirmTextConfig       ( String path );

    Properties putProperties              ( String path, Map<String, Object > properties );

    TextConfigNode putTextValue           ( String path, String format, String value );
}
