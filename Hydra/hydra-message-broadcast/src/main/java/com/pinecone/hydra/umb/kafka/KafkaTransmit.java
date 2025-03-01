package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.ArchUnidirectionalMCProtocol;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.Status;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.UMCTransmit;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaTransmit extends ArchUnidirectionalMCProtocol implements UMCTransmit {
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    public KafkaTransmit( Medium messageSource ){
        super(messageSource);
    }

    @Override
    public void sendInformMsg( Object msg ) throws IOException {

    }

    @Override
    public void sendInformMsg( Object msg, Status status ) throws IOException {
        if ( status != Status.OK ) {
            this.logger.warn( "IllegalTransmitResponse for broadcast message nodes. what => {}, {}", msg, status );
        }
    }

    @Override
    public void sendTransferMsg( Object msg, byte[] bytes ) throws IOException {

    }

    @Override
    public void sendTransferMsg( Object msg, byte[] bytes, Status status ) throws IOException {
        if ( status != Status.OK ) {
            this.logger.warn( "IllegalTransmitResponse for broadcast message nodes. what => {}, {}", msg, status );
        }
    }

    @Override
    public void sendTransferMsg( Object msg, InputStream is ) throws IOException {

    }

    @Override
    public void sendMsg( UMCMessage msg, boolean bNoneBuffered ) throws IOException {
        if ( msg.getHead().getStatus() != Status.OK ) {
            this.logger.warn( "IllegalTransmitResponse for broadcast message nodes. what => {}", msg );
        }
    }
}
