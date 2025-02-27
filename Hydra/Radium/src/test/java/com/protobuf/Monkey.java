package com.protobuf;

import com.pinecone.framework.util.json.homotype.GenericBeanJSONEncoder;

public class Monkey {
    public String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toJSONString() {
        return GenericBeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
