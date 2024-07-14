package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.extra.GenericExtraHeadCoder;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;
import com.pinecone.hydra.umc.msg.handler.GenericErrorMessageAudit;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class WolfMCNode extends WolfNettyServgram implements UlfMessageNode {
    protected ExtraHeadCoder        mExtraHeadCoder     ;
    protected final ReentrantLock   mMajorIOLock        = new ReentrantLock();
    protected ErrorMessageAudit     mErrorMessageAudit  ;

    public WolfMCNode( String szName, Hydrarum parent, Map<String, Object> joConf, @Nullable ExtraHeadCoder extraHeadCoder ) {
        super( szName, parent, joConf );

        this.mExtraHeadCoder    = extraHeadCoder;
        this.mErrorMessageAudit = new GenericErrorMessageAudit( this );
    }

    @Override
    public ExtraHeadCoder getExtraHeadCoder() {
        return this.mExtraHeadCoder;
    }

    public ReentrantLock getMajorIOLock() {
        return this.mMajorIOLock;
    }

    public WolfMCNode apply(JSONObject joConf ) {
        this.mjoSectionConf = joConf;

        try{
            if( this.mExtraHeadCoder == null ) {
                String szExtraHeadCoder   = joConf.optString( "ExtraHeadCoder" );
                if( StringUtils.isEmpty( szExtraHeadCoder ) ) {
                    this.mExtraHeadCoder  = new GenericExtraHeadCoder() ;
                }
                else {
                    this.mExtraHeadCoder  = (ExtraHeadCoder) DynamicFactory.DefaultFactory.loadInstance( szExtraHeadCoder, null, null );
                }

                String szDefaultExtraEncode   = joConf.optString( "DefaultExtraEncode" );
                if( StringUtils.isEmpty( szDefaultExtraEncode ) ) {
                    this.mExtraHeadCoder.setDefaultEncode( ExtraEncode.JSONString );
                }
                else {
                    this.mExtraHeadCoder.setDefaultEncode( ExtraEncode.valueOf( szDefaultExtraEncode ) );
                }
            }
        }
        catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            throw new ProxyProvokeHandleException( e );
        }

        return this;
    }

    public abstract WolfMCNode apply( UlfAsyncMsgHandleAdapter fnRecipientMsgHandler );

    @Override
    public ErrorMessageAudit getErrorMessageAudit() {
        return this.mErrorMessageAudit;
    }

    @Override
    public void setErrorMessageAudit( ErrorMessageAudit audit ) {
        this.mErrorMessageAudit = audit;
    }
}
