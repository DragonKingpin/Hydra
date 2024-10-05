package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericTextFile;
import com.pinecone.hydra.registry.entity.TextFile;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextFileManipulator;

public class TextValueNodeOperator extends ArchConfigNodeOperator {
    protected RegistryTextFileManipulator registryTextFileManipulator;

    public TextValueNodeOperator(RegistryOperatorFactory factory) {
        super(factory);
        this.registryTextFileManipulator = factory.getMasterManipulator().getTextFileManipulator();
    }

    @Override
    public TextFile get( GUID guid ) {
        return (TextFile) super.get( guid );
    }

    @Override
    public TextFile get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    protected TextFile getConfigNodeWideData( GUID guid ) {
        ConfigNode configNodeWideData = super.getConfigNodeWideData( guid );
        GenericTextFile textConfNode = new GenericTextFile();
        TextValue textValue = this.registryTextFileManipulator.getTextValue( guid );

        textConfNode.setTextValue       ( textValue );
        textConfNode.setConfigNodeMeta  ( configNodeWideData.getConfigNodeMeta() );
        textConfNode.setAttributes      ( configNodeWideData.getAttributes() );
        textConfNode.setGuid            ( configNodeWideData.getGuid() );
        textConfNode.setName            ( configNodeWideData.getName() );
        textConfNode.setCreateTime      ( configNodeWideData.getCreateTime() );
        textConfNode.setRegistry        ( configNodeWideData.parentRegistry() );
        textConfNode.setUpdateTime      ( configNodeWideData.getUpdateTime() );
        return textConfNode;
    }

    @Override
    public void purge( GUID guid ) {
        super.purge(guid);
        this.registryTextFileManipulator.remove(guid);
    }


}
