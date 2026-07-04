package com.pinecone.hydra.umb.broadcast;

import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCHeadV1;
import com.pinecone.hydra.umc.msg.UMCProtocol;
import com.pinecone.hydra.umc.msg.UMCProtocolMagic;

public class ArchUnidirectionalMCProtocol implements UMCProtocol {

    protected String        mszVersion     = UMCHeadV1.ProtocolVersion;

    protected byte[]        signature      = UMCHeadV1.ProtocolSignature;

    protected Medium        mMessageSource ;

    public ArchUnidirectionalMCProtocol( Medium messageSource ) {
        this.mMessageSource = messageSource;
        this.applyMessageSource( messageSource );
    }

    @Override
    public UMCProtocol applyMessageSource( Medium medium ) {
        this.mMessageSource = medium;
        return this;
    }

    @Override
    public Medium getMessageSource() {
        return this.mMessageSource;
    }

    @Override
    public String getVersion(){
        return this.mszVersion;
    }

    @Override
    public byte[] getSignature() {
        return UMCProtocolMagic.copyOf( this.signature );
    }

    @Override
    public void release() {

    }
}
