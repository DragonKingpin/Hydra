package com.pinecone.hydra.messagram;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umc.msg.*;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.express.Package;

import java.io.IOException;
import java.util.Map;

public abstract class ArchMsgDeliver implements MessageDeliver {
    protected String          mszName;
    protected Hydrarum        mSystem;
    protected MessageExpress  mExpress;
    protected ArchMessagram   mMessagram;

    public ArchMsgDeliver( String szName, MessageExpress express ) {
        this.mszName     = szName;
        this.mExpress    = express;
        this.mSystem     = this.mExpress.getSystem();
        this.mMessagram  = this.mExpress.getMessagram();
    }


    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Hydrarum getSystem() {
        return this.mSystem;
    }

    @Override
    public MessageExpress  getExpress() {
        return this.mExpress;
    }

    public ArchMessagram getMessagram(){
        return this.mMessagram;
    }

    @Override
    public abstract String getServiceKeyword() ;


    protected MessagePackage wrap( Package that ) {
        return (MessagePackage) that;
    }

    protected abstract void prepareDispatch( Package that ) throws IOException;

    protected abstract boolean sift( Package that );

    protected boolean isMyJob( Package that, String szServiceKey ) {
        return szServiceKey != null;
    }

    protected void messageDispatch( Package that ) throws IOException {
        MessagePackage msgPackage = this.wrap( that );
        UMCMessage msg            = msgPackage.getMessage();

        if( this.sift( that ) ) {
            msgPackage.getTransmit().sendPutMsg(
                    (new JSONMaptron()).put( "What", "Illegal message." ), Status.IllegalMessage
            );
            return;
        }

        UMCHead head                    = msg.getHead();
        Map<String, Object > joExHead   = head.getExtraHead();
        String szServiceKey             = (String) joExHead.get( this.getServiceKeyword() );
        if( szServiceKey == null ) {
            szServiceKey = (String) joExHead.get( "/" );
        }

        //Debug.trace( new String( msg.getStreamBody().readAllBytes() ) );
        if( this.isMyJob( that, szServiceKey ) ) {
            msgPackage.entrust( this );

            switch ( szServiceKey ) {
                case "close": {
                    msgPackage.getMessageSource().release();
                    break;
                }
                case "SystemShutdown": {
                    this.getSystem().kill();
                    break;
                }
                case "ReceiveConfirm": {
                    return;
                }
                default: {
                    try{
                        this.doMessagelet( szServiceKey, that );
                    }
                    catch ( IllegalArgumentException e ) {
                        msgPackage.getTransmit().sendPutMsg(
                                (new JSONMaptron()).put( "What", "Messagelet not found." ), Status.MappingNotFound
                        );
                    }
                }
            }
        }
    }

    protected abstract void doMessagelet( String szMessagelet, Package that ) ;

    @Override
    public void toDispatch( Package that ) throws IOException {
        this.prepareDispatch( that );
        this.messageDispatch( that );
    }

}