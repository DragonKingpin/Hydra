package com.pinecone.hydra.umc.wolf;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regimentation.CascadeNodus;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.event.ChannelDataInterceptor;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.extra.GenericExtraHeadCoder;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;
import com.pinecone.hydra.umc.msg.handler.GenericErrorMessageAudit;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umct.UMCTExpressHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelHandlerContext;


public abstract class WolfMCNode extends WolfNettyServgram implements UlfMessageNode {
    protected ExtraHeadCoder               mExtraHeadCoder          ;
    protected final ReentrantLock          mMajorIOLock             = new ReentrantLock();
    protected ErrorMessageAudit            mErrorMessageAudit       ;
    protected UlfMessageNode               mParentNode              ;
    protected Namespace                    mNodeNamespace           ;
    protected long                         mnMessageNodeId          ;

    protected List<ChannelInactiveHandler> mChannelInactiveHandlers ;
    protected List<ChannelDataInterceptor> mArrivedDataInterceptors ;

    public WolfMCNode( long nodeId, String szName, Processum parentProcess, UlfMessageNode parent, Map<String, Object> joConf, @Nullable ExtraHeadCoder extraHeadCoder ) {
        super( szName, parentProcess, joConf );

        this.mExtraHeadCoder           = extraHeadCoder;
        this.mErrorMessageAudit        = new GenericErrorMessageAudit( this );
        this.mParentNode               = parent;
        this.mnMessageNodeId           = nodeId;
        this.mChannelInactiveHandlers  = new ArrayList<>();
        this.mArrivedDataInterceptors  = new ArrayList<>();
        this.setTargetingName( szName );
    }

    public WolfMCNode( long nodeId, String szName, Hydrarum system, Map<String, Object> joConf, @Nullable ExtraHeadCoder extraHeadCoder ) {
        this( nodeId, szName, system, null, joConf, extraHeadCoder );
    }

    protected void checkDeRegisterHandlerStatus() throws IllegalStateException  {
        if ( !this.isShutdown() ) {
            throw new IllegalStateException( "Service is already running." );
        }
    }

    @Override
    public UlfMessageNode registerChannelInactiveHandler( ChannelInactiveHandler handler ) throws IllegalStateException {
        this.checkDeRegisterHandlerStatus();
        this.mChannelInactiveHandlers.add( handler );
        return this;
    }

    @Override
    public UlfMessageNode deregisterChannelInactiveHandler( ChannelInactiveHandler handler ) throws IllegalStateException {
        this.checkDeRegisterHandlerStatus();
        this.mChannelInactiveHandlers.remove( handler );
        return this;
    }

    @Override
    public UlfMessageNode registerArrivedDataInterceptor( ChannelDataInterceptor handler ) throws IllegalStateException {
        this.checkDeRegisterHandlerStatus();
        this.mArrivedDataInterceptors.add( handler );
        return this;
    }

    @Override
    public UlfMessageNode deregisterArrivedDataInterceptor( ChannelDataInterceptor handler ) throws IllegalStateException {
        this.checkDeRegisterHandlerStatus();
        this.mArrivedDataInterceptors.remove( handler );
        return this;
    }

    protected boolean tryInvokeOrInterceptArrivedData( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) throws ChannelHandleException {
        for( ChannelDataInterceptor h : this.mArrivedDataInterceptors ) {
            if ( h.interceptAfterDataArrived( medium, block, msg, ctx, rawMsg ) ){
                return true;
            }
        }

        return false;
    }



    @Override
    public CascadeNodus parent() {
        return this.mParentNode;
    }

    @Override
    public Namespace getTargetingName() {
        return this.mNodeNamespace;
    }

    @Override
    public void setTargetingName( Namespace name ) {
        this.mNodeNamespace = name;
    }

    @Override
    public ExtraHeadCoder getExtraHeadCoder() {
        return this.mExtraHeadCoder;
    }

    @Override
    public long getMessageNodeId() {
        return this.mnMessageNodeId;
    }

    public ReentrantLock getMajorIOLock() {
        return this.mMajorIOLock;
    }

    public WolfMCNode apply( Map<String, Object> joConf ) {
        this.setConfig( joConf );

        try{
            if( this.mExtraHeadCoder == null ) {
                String szExtraHeadCoder   = (String) joConf.get( "ExtraHeadCoder" );
                if( StringUtils.isEmpty( szExtraHeadCoder ) ) {
                    this.mExtraHeadCoder  = new GenericExtraHeadCoder() ;
                }
                else {
                    this.mExtraHeadCoder  = (ExtraHeadCoder) DynamicFactory.DefaultFactory.loadInstance( szExtraHeadCoder, null, null );
                }

                String szDefaultExtraEncode = (String) joConf.get( "DefaultExtraEncode" );
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

    public WolfMCNode apply( UMCTExpressHandler handler ){
        this.apply( UlfAsyncMsgHandleAdapter.wrap( handler ) );
        return this;
    }

    @Override
    public ErrorMessageAudit getErrorMessageAudit() {
        return this.mErrorMessageAudit;
    }

    @Override
    public void setErrorMessageAudit( ErrorMessageAudit audit ) {
        this.mErrorMessageAudit = audit;
    }
}
