package com.auto.proto;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProtoMapPayload {
    protected Map<String, String>                mMetadata;
    protected Map<Long, String>                  mLongLabels;
    protected Map<Boolean, Integer>              mSwitchCounters;
    protected Map<String, ProtoMapNestedPayload> mNestedPayloads;
    protected List<ProtoMapNestedPayload>        mNestedList;

    public ProtoMapPayload() {
    }

    public Map<String, String> getMetadata() {
        return this.mMetadata;
    }

    public void setMetadata( Map<String, String> metadata ) {
        this.mMetadata = metadata;
    }

    public Map<Long, String> getLongLabels() {
        return this.mLongLabels;
    }

    public void setLongLabels( Map<Long, String> longLabels ) {
        this.mLongLabels = longLabels;
    }

    public Map<Boolean, Integer> getSwitchCounters() {
        return this.mSwitchCounters;
    }

    public void setSwitchCounters( Map<Boolean, Integer> switchCounters ) {
        this.mSwitchCounters = switchCounters;
    }

    public Map<String, ProtoMapNestedPayload> getNestedPayloads() {
        return this.mNestedPayloads;
    }

    public void setNestedPayloads( Map<String, ProtoMapNestedPayload> nestedPayloads ) {
        this.mNestedPayloads = nestedPayloads;
    }

    public List<ProtoMapNestedPayload> getNestedList() {
        return this.mNestedList;
    }

    public void setNestedList( List<ProtoMapNestedPayload> nestedList ) {
        this.mNestedList = nestedList;
    }

    public static ProtoMapPayload sample() {
        ProtoMapPayload payload = new ProtoMapPayload();

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put( "alpha", "one" );
        metadata.put( "beta", "two" );
        payload.setMetadata( metadata );

        Map<Long, String> longLabels = new LinkedHashMap<>();
        longLabels.put( 100L, "hundred" );
        longLabels.put( 200L, "two-hundred" );
        payload.setLongLabels( longLabels );

        Map<Boolean, Integer> switchCounters = new LinkedHashMap<>();
        switchCounters.put( Boolean.TRUE, 7 );
        switchCounters.put( Boolean.FALSE, 3 );
        payload.setSwitchCounters( switchCounters );

        Map<String, ProtoMapNestedPayload> nestedPayloads = new LinkedHashMap<>();
        nestedPayloads.put( "red", new ProtoMapNestedPayload( "ruby", 91 ) );
        nestedPayloads.put( "blue", new ProtoMapNestedPayload( "sapphire", 82 ) );
        payload.setNestedPayloads( nestedPayloads );

        List<ProtoMapNestedPayload> nestedList = new ArrayList<>();
        nestedList.add( new ProtoMapNestedPayload( "list-alpha", 11 ) );
        nestedList.add( new ProtoMapNestedPayload( "list-beta", 22 ) );
        payload.setNestedList( nestedList );

        return payload;
    }
}
