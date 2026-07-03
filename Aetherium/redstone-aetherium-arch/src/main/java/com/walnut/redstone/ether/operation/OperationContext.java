package com.walnut.redstone.ether.operation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectRange;

public class OperationContext implements Pinenut {
    protected ObjectOperation operation;
    protected ObjectRange range;
    protected Map<String, String> attributes = new LinkedHashMap<>();

    public ObjectOperation getOperation() {
        return this.operation;
    }

    public void setOperation( ObjectOperation operation ) {
        this.operation = operation;
    }

    public ObjectRange getRange() {
        return this.range;
    }

    public void setRange( ObjectRange range ) {
        this.range = range;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes( Map<String, String> attributes ) {
        this.attributes = attributes == null ? new LinkedHashMap<>() : new LinkedHashMap<>( attributes );
    }
}

