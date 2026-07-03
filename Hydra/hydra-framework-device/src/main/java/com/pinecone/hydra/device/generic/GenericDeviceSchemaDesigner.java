package com.pinecone.hydra.device.generic;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class GenericDeviceSchemaDesigner implements Pinenut {
    protected String version = "1.0.0";
    protected List<GenericDeviceSchemaField> fields = new ArrayList<>();

    public String getVersion() {
        return this.version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    public List<GenericDeviceSchemaField> getFields() {
        return this.fields;
    }

    public void setFields( List<GenericDeviceSchemaField> fields ) {
        this.fields = fields;
    }
}
