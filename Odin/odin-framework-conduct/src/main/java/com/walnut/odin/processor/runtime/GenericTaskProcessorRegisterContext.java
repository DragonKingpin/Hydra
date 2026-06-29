package com.walnut.odin.processor.runtime;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.walnut.odin.processor.runtime.TaskProcessorRegisterContext;

public class GenericTaskProcessorRegisterContext implements TaskProcessorRegisterContext {

    protected String              mszNodeName;
    protected long                mnClientId;
    protected Map<String, String> mMetadata;

    public GenericTaskProcessorRegisterContext( String szNodeName, long nClientId, Map<String, String> metadata ) {
        this.mszNodeName = szNodeName;
        this.mnClientId  = nClientId;
        this.mMetadata   = metadata == null ? new LinkedHashMap<>() : new LinkedHashMap<>( metadata );
    }

    public static GenericTaskProcessorRegisterContext of( String szNodeName, long nClientId, Map<String, String> metadata ) {
        return new GenericTaskProcessorRegisterContext( szNodeName, nClientId, metadata );
    }

    @Override
    public String getNodeName() {
        return this.mszNodeName;
    }

    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap( this.mMetadata );
    }
}
