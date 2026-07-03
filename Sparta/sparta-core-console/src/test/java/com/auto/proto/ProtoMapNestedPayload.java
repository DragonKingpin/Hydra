package com.auto.proto;

public class ProtoMapNestedPayload {
    protected String mszName;
    protected int    mScore;

    public ProtoMapNestedPayload() {
    }

    public ProtoMapNestedPayload( String szName, int score ) {
        this.mszName = szName;
        this.mScore  = score;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    public int getScore() {
        return this.mScore;
    }

    public void setScore( int score ) {
        this.mScore = score;
    }
}
