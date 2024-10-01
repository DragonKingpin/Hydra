package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.registry.Registry;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface ConfigOperatorFactory extends Pinenut {
    String DefaultNamespaceNodeKey          =   NamespaceNode.class.getSimpleName();
    String DefaultConfigNodeKey             =   ConfigNode.class.getSimpleName();
    String DefaultPropertyConfigNodeKey     =   Properties.class.getSimpleName();
    String DefaultTextConfigNode            =   TextConfigNode.class.getSimpleName();

    void register( String typeName, TreeNodeOperator functionalNodeOperation );

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    RegistryNodeOperator getOperator( String typeName );

    Registry getRegistry();

    RegistryMasterManipulator getMasterManipulator();
}
