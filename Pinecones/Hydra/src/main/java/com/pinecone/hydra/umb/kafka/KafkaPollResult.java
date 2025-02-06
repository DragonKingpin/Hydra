package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.PollResult;

public class KafkaPollResult implements PollResult {
    protected Object name;

    protected Object value;

    protected byte[] bytesValue;

    protected Object[] args;

    public KafkaPollResult( Object name, Object value, byte[] bytesValue, Object[] args ){
        this( name,value,bytesValue );
        this.args = args;
    }

    public KafkaPollResult( Object name, Object value, byte[] bytesValue ){
        this.name = name;
        this.value = value;
        this.bytesValue = bytesValue;
    }


    @Override
    public Object getName() {
        return this.name;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public byte[] getBytesValue() {
        return new byte[0];
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }
}
