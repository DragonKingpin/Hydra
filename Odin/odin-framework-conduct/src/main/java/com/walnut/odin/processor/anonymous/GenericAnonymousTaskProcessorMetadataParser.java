package com.walnut.odin.processor.anonymous;

import java.util.Collection;
import java.util.Map;

import com.walnut.odin.dispatch.ExecutionArchitects;
import com.walnut.odin.processor.metadata.TaskProcessorRegisterMetadataSpec;

public class GenericAnonymousTaskProcessorMetadataParser implements AnonymousTaskProcessorMetadataParser {

    @Override
    public boolean isAnonymous( Map<String, String> metadata ) {
        String szEstablishment = this.value( metadata, TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT );
        return TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_ANONYMOUS.equalsIgnoreCase( szEstablishment );
    }

    @Override
    public String alias( Map<String, String> metadata ) {
        return this.value( metadata, TaskProcessorRegisterMetadataSpec.KEY_ALIAS );
    }

    @Override
    public String bizPath( Map<String, String> metadata ) {
        return this.value( metadata, TaskProcessorRegisterMetadataSpec.KEY_BIZ_PATH );
    }

    @Override
    public String runtime( Map<String, String> metadata ) {
        return this.value( metadata, TaskProcessorRegisterMetadataSpec.KEY_RUNTIME );
    }

    @Override
    public Collection<String> execCaps( Map<String, String> metadata ) {
        return ExecutionArchitects.parseExecCaps( this.value( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS ) );
    }

    protected String value( Map<String, String> metadata, String szKey ) {
        if ( metadata == null || szKey == null ) {
            return null;
        }
        String value = metadata.get( szKey );
        if ( value == null ) {
            return null;
        }
        value = value.trim();
        return value.isEmpty() ? null : value;
    }
}
