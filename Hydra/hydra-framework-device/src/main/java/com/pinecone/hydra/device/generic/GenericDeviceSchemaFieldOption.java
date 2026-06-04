package com.pinecone.hydra.device.generic;

import com.pinecone.framework.system.prototype.Pinenut;

public class GenericDeviceSchemaFieldOption implements Pinenut {
    protected String code;
    protected String name;
    protected Object value;
    protected boolean enabled = true;

    public String getCode() {
        return this.code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue( Object value ) {
        this.value = value;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }
}
