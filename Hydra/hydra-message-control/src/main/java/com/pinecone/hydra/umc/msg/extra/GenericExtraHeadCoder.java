package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.umc.msg.ExtraEncode;

import java.util.Map;

public class GenericExtraHeadCoder implements ExtraHeadCoder {
    protected ExtraHeadEncoder encoder;
    protected ExtraHeadDecoder decoder;
    protected ExtraEncode      extraEncode;

    public GenericExtraHeadCoder () {
        this( new GenericExtraHeadEncoder(), new GenericExtraHeadDecoder() );
    }

    public GenericExtraHeadCoder ( ExtraHeadEncoder encoder, ExtraHeadDecoder decoder ) {
        this( encoder, decoder, ExtraEncode.JSONString );
    }

    public GenericExtraHeadCoder ( ExtraHeadEncoder encoder, ExtraHeadDecoder decoder, ExtraEncode extraEncode ) {
        this.encoder     = encoder;
        this.decoder     = decoder;
        this.extraEncode = extraEncode;
    }

    @Override
    public ExtraHeadEncoder getEncoder() {
        return this.encoder;
    }

    @Override
    public ExtraHeadDecoder getDecoder() {
        return this.decoder;
    }

    @Override
    public ExtraEncode getDefaultEncode() {
        return this.extraEncode;
    }

    @Override
    public void setDefaultEncode( ExtraEncode encode ) {
        this.extraEncode = encode;
    }

    @Override
    public Map<String, Object > newExtraHead() {
        return new JSONMaptron();
    }
}
