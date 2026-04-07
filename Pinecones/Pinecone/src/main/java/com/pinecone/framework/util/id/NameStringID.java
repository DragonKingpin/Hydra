package com.pinecone.framework.util.id;

import com.pinecone.framework.util.json.JSONString;

public class NameStringID implements StringID, JSONString {

    private String name;

    public NameStringID( String name ) {
        this.name = name;
    }

    @Override
    public Identification parse( String code ) {
        this.name = code;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String toJSONString() {
        return "\"" + this.name + "\"";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !(obj instanceof NameStringID) ) {
            return false;
        }
        NameStringID that = (NameStringID) obj;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public byte[] toBytes() {
        return this.name.getBytes();
    }

    @Override
    public int compareTo( Identification that ) {
        StringID val;
        if ( that instanceof StringID ) {
            val = (StringID) that;
        }
        else {
            throw new IllegalArgumentException( "Not StringID" );
        }

        return this.name.compareTo( val.toString() );
    }

    @Override
    public int length() {
        return this.name.length();
    }
}
