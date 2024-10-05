package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;

import java.nio.ByteOrder;
import java.util.Map;

public class UMCHead implements Pinenut {
    public static final String     ProtocolVersion   = "1.1";
    public static final String     ProtocolSignature = "UMC/" + UMCHead.ProtocolVersion;
    public static final int        HeadBlockSize     = UMCHead.ProtocolSignature.length() + 1 + Byte.BYTES + Integer.BYTES + Long.BYTES + Long.BYTES + Short.BYTES + Short.BYTES;
    public static final ByteOrder  BinByteOrder      = ByteOrder.LITTLE_ENDIAN ;// Using x86, C/C++

    protected String                 szSignature                                ;
    protected UMCMethod              method                                     ; // sizeof( UMCMethod/byte ) = 1
    protected int                    nExtraHeadLength  = 2                      ; // sizeof( int32 ) = 4
    protected long                   nBodyLength       = 0                      ; // sizeof( int64 ) = 8
    protected long                   nKeepAlive        = -1                     ; // sizeof( int64 ) = 8, [-1 for forever, 0 for off, others for millis]
    protected Status                 status            = Status.OK              ; // sizeof( Status/Short ) = 2
    protected ExtraEncode            extraEncode       = ExtraEncode.Undefined  ; // sizeof( ExtraEncode/Short ) = 2
    protected byte[]                 extraHead         = {}                     ;
    protected Map<String,Object >    joExtraHead       = new LinkedTreeMap<>()  ;

    protected ExtraHeadCoder         extraHeadCoder                             ;


    public UMCHead(  ) {
        this( UMCHead.ProtocolSignature, UMCMethod.PUT );
    }

    public UMCHead( String szSignature ) {
        this( szSignature, UMCMethod.PUT );
    }

    public UMCHead( String szSignature, UMCMethod umcMethod ) {
        this.szSignature       = szSignature;
        this.method            = umcMethod;
    }

    UMCHead( String szSignature, UMCMethod umcMethod, Map<String,Object > joEx ) {
        this( szSignature, umcMethod );
        this.joExtraHead = joEx;
    }


    void setSignature        ( String signature       ) {
        this.szSignature = signature;
    }

    void setBodyLength       ( long length            ) {
        this.nBodyLength = length;
    }

    public void setKeepAlive ( long nKeepAlive        ) {
        this.nKeepAlive = nKeepAlive;
    }

    void setMethod           ( UMCMethod umcMethod    ) {
        this.method = umcMethod;
        if ( this.method == UMCMethod.PUT ) {
            this.nBodyLength = 0;
        }
    }

    void setExtraEncode      ( ExtraEncode encode     ) {
        this.extraEncode = encode;
    }

    void setExtraHead        ( JSONObject jo          ) {
        this.joExtraHead = jo.getMap();
    }

    void setExtraHead        ( Map<String,Object > jo ) {
        this.joExtraHead = jo;
    }

    void transApplyExHead    (                        ) {
        if ( this.joExtraHead != null ) {
            this.extraHead         = this.extraHeadCoder.getEncoder().encode( this, this.joExtraHead );
            this.nExtraHeadLength  = this.extraHead .length;
        }
        else {
            if( this.extraEncode == ExtraEncode.JSONString ) {
                this.extraHead  = "{}".getBytes();
            }
            else {
                this.joExtraHead = this.extraHeadCoder.newExtraHead();
                this.extraHead   = this.extraHeadCoder.getEncoder().encode( this, this.joExtraHead );
            }
        }

        this.nExtraHeadLength  = this.extraHead .length;
    }

    void applyExtraHeadCoder ( ExtraHeadCoder coder   ) {
        this.extraHeadCoder = coder;

        if( this.extraEncode == ExtraEncode.Undefined ) {
            this.extraEncode = coder.getDefaultEncode();
        }
    }


    public void            setStatus ( Status status ) {
        this.status = status;
    }

    public ExtraHeadCoder  getExtraHeadCoder() {
        return this.extraHeadCoder;
    }

    public String          getSignature() {
        return this.szSignature;
    }

    public int             getSignatureLength() {
        return this.getSignature().length();
    }

    public UMCMethod       getMethod() {
        return this.method;
    }

    public int             getExtraHeadLength() {
        return this.nExtraHeadLength;
    }

    public long            getBodyLength() {
        return this.nBodyLength;
    }

    public long            getKeepAlive() {
        return this.nKeepAlive;
    }

    public Status          getStatus() {
        return this.status;
    }

    public ExtraEncode     getExtraEncode() {
        return this.extraEncode;
    }

    public byte[]          getExtraHeadBytes() {
        return this.extraHead ;
    }

    public Map<String,Object > getExtraHead() {
        return this.joExtraHead;
    }


    protected UMCHead applyExHead( Map<String, Object > jo      ) {
        if( this.getExtraHead() == null || this.getExtraHead().size() == 0 ) {
            this.setExtraHead( jo );
        }
        else {
            if( jo.size() > this.getExtraHead().size() ) {
                jo.putAll( this.getExtraHead() );
                this.setExtraHead( jo );
            }
            else {
                this.getExtraHead().putAll( jo );
            }
        }
        return this;
    }

    public UMCHead receiveSet( Map<String, Object > joExtraHead ) {
        this.joExtraHead = joExtraHead;
        return this;
    }

    public void release() {
        // Help GC
        this.joExtraHead = null;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "Signature"      , this.getSignature()                   ),
                new KeyValue<>( "Method"         , this.getMethod()                      ),
                new KeyValue<>( "ExtraHeadLength", this.getExtraHeadLength()             ),
                new KeyValue<>( "BodyLength"     , this.getBodyLength()                  ),
                new KeyValue<>( "KeepAlive"      , this.getKeepAlive()                   ),
                new KeyValue<>( "Status"         , this.getStatus().getName()            ),
                new KeyValue<>( "ExtraEncode"    , this.getExtraEncode().getName()       ),
                new KeyValue<>( "ExtraHead"      , JSON.stringify( this.getExtraHead() ) ),
        } );
    }
}
