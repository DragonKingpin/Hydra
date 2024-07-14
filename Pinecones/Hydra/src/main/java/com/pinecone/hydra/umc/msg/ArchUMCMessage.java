package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.util.json.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class ArchUMCMessage implements UMCMessage {
    protected UMCHead        mHead                ;
    protected byte[]         msBytesBody   = null ;
    protected InputStream    mIStreamBody  = null ;

    public ArchUMCMessage( UMCHead head ) {
        this.mHead            = head;
    }

    public ArchUMCMessage( UMCHead head, byte[] sBytesBody   ) {
        this( head );
        this.setBody( sBytesBody );
    }

    public ArchUMCMessage( UMCHead head, String szStringBody ) {
        this( head, szStringBody.getBytes() );
    }

    public ArchUMCMessage( UMCHead head, InputStream inStream ) {
        this( head );
        this.setBody( inStream );
    }


    ArchUMCMessage( Map<String,Object > joExHead, UMCMethod method ) {
        this.mHead = new UMCHead();
        this.mHead.setMethod( method );
        this.mHead.applyExHead( joExHead );
    }

    public ArchUMCMessage( Map<String,Object > joExHead ) {
        this( joExHead, UMCMethod.PUT );
    }

    public ArchUMCMessage( Map<String,Object > joExHead, byte[] sBytesBody ) {
        this( joExHead, UMCMethod.POST );
        this.setBody( sBytesBody );
    }

    public ArchUMCMessage( Map<String,Object > joExHead, String szStringBody ) {
        this( joExHead, szStringBody.getBytes() );
    }

    public ArchUMCMessage( Map<String,Object > joExHead, InputStream inStream ) {
        this( joExHead, UMCMethod.POST );
        this.setBody( inStream );
    }

    void setBody( byte[] sBytesBody ) {
        this.msBytesBody = sBytesBody;
        this.mHead.setBodyLength( this.msBytesBody.length );
    }

    void setBody( InputStream inStream ) {
        this.mIStreamBody = inStream;
        try{
            this.mHead.setBodyLength( this.mIStreamBody.available() );
        }
        catch ( IOException e ) {
            this.mHead.setBodyLength( 0 );
        }
    }


    @Override
    public UMCHead     getHead() {
        return this.mHead;
    }

    @Override
    public Map<String,Object > getExHead() {
        return this.mHead.joExtraHead;
    }

    @Override
    public byte[]      getBytesBody() {
        return this.msBytesBody;
    }

    @Override
    public InputStream getStreamBody() {
        return this.mIStreamBody;
    }

    @Override
    public Object      getBody() {
        if( this.msBytesBody != null ) {
            return this.getBytesBody();
        }
        else if( this.mIStreamBody != null ) {
            return this.getStreamBody();
        }

        return null;
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
        this.msBytesBody  = null;
        this.mIStreamBody = null;
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
