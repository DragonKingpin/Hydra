package com.protobuf;

public class Rabbit {
    public String name;

    public byte[] bytes;

    public Monkey monkey;

    public Monkey getMonkey() {
        return this.monkey;
    }

    public void setMonkey( Monkey monkey ) {
        this.monkey = monkey;
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
}
