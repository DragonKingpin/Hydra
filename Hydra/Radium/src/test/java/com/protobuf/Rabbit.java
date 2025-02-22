package com.protobuf;

import com.pinecone.framework.util.json.homotype.GenericBeanJSONEncoder;

public class Rabbit {
    public String name;

    public byte[] bytes;

    public Monkey monkey;

    public Monkey[] monkeys;

    public Rabbit sub;

    public Rabbit getSub() {
        return this.sub;
    }

    public void setSub( Rabbit sub ) {
        this.sub = sub;
    }

    public Monkey getMonkey() {
        return this.monkey;
    }

    public Monkey[] getMonkeys() {
        return this.monkeys;
    }

    public void setMonkey( Monkey monkey ) {
        this.monkey = monkey;
    }

    public void setMonkeys( Monkey[] monkeys ) {
        this.monkeys = monkeys;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

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
