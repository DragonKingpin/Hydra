package com.auto.proto;

import java.util.Map;

public class ProtoObjectMapPayload {
    protected Map<Object, Object> mObjects;

    public Map<Object, Object> getObjects() {
        return this.mObjects;
    }

    public void setObjects( Map<Object, Object> objects ) {
        this.mObjects = objects;
    }
}
