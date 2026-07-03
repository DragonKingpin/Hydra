package com.protobuf;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.json.homotype.GenericBeanJSONEncoder;

public class Rabbit {
    public String name;

    public byte[] bytes;

    public Monkey monkey;

    public Monkey[] monkeys;

    public Map<String, Monkey> stringMonkeyMap;

    public Map<Long, Monkey> longMonkeyMap;

    public List<Monkey> monkeyList;

    public Rabbit sub;

    public boolean bool;

    public boolean isBool() {
        return this.bool;
    }

    public void setBool( boolean bool ) {
        this.bool = bool;
    }

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

    public Map<String, Monkey> getStringMonkeyMap() {
        return this.stringMonkeyMap;
    }

    public void setStringMonkeyMap( Map<String, Monkey> stringMonkeyMap ) {
        this.stringMonkeyMap = stringMonkeyMap;
    }

    public Map<Long, Monkey> getLongMonkeyMap() {
        return this.longMonkeyMap;
    }

    public void setLongMonkeyMap( Map<Long, Monkey> longMonkeyMap ) {
        this.longMonkeyMap = longMonkeyMap;
    }

    public List<Monkey> getMonkeyList() {
        return this.monkeyList;
    }

    public void setMonkeyList( List<Monkey> monkeyList ) {
        this.monkeyList = monkeyList;
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
