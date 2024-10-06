package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.util.json.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class ArchUMCMessage implements UMCMessage {
    protected UMCHead        mHead                ;

    public ArchUMCMessage( UMCHead head ) {
        this.mHead            = head;
    }

    ArchUMCMessage( Map<String,Object > joExHead, UMCMethod method ) {
        this.mHead = new UMCHead();
        this.mHead.setMethod( method );
        this.mHead.applyExHead( joExHead );
    }

    ArchUMCMessage( Object protoExHead, UMCMethod method ) {
        this.mHead = new UMCHead();
        this.mHead.setMethod( method );
        this.mHead.setExtraHead( protoExHead );
        this.mHead.setExtraEncode( ExtraEncode.Prototype );
    }

    public ArchUMCMessage( Map<String,Object > joExHead ) {
        this( joExHead, UMCMethod.PUT );
    }

    public ArchUMCMessage( Object protoExHead ) {
        this( protoExHead, UMCMethod.PUT );
    }


    @Override
    public UMCHead     getHead() {
        return this.mHead;
    }

    @Override
    public Object    getExHead() {
        return this.mHead.getExtraHead();
    }

    @Override
    public long        getMessageLength(){
        return UMCHead.HeadBlockSize + this.mHead.getExtraHeadLength() + this.mHead.getBodyLength();
    }

    @Override
    public long        queryMessageLength(){
        this.mHead.transApplyExHead();
        return this.getMessageLength();
    }

    @Override
    public void        release() {
        this.mHead        = null;
    }

    @Override
    public String      toString() {
        return this.toJSONString();
    }

    @Override
    public String      toJSONString() {
        return String.format(
                "{\"head\":%s, \"Method\":\"%s\", \"BodyLength\":%d}",
                JSON.stringify( this.getHead().getExtraHead() ),
                this.getHead().getMethod().getName(),
                this.getHead().getBodyLength()
        );
    }

}
