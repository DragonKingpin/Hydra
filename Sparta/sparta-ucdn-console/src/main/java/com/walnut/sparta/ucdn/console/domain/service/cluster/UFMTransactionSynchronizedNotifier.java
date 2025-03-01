package com.walnut.sparta.ucdn.console.domain.service.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint( value = "/websocket/ucdn/monitor/nodes/transactionSynchronized" )
@Component
public class UFMTransactionSynchronizedNotifier {
    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private static Session session;

    @OnOpen
    public void onOpen( Session session ){
        UFMTransactionSynchronizedNotifier.session = session;
    }

    @OnMessage
    public void onMessage( String msg, Session session ){
        this.log.info(msg);
    }

    @OnClose
    public void onClose(){
        this.log.info( "TransactionSynchronized notifier has been successfully shutdown." );
    }

    @OnError
    public void onError( Session session, Throwable error ){
        this.log.error( "TransactionSynchronized error: ", error );
    }

    public Session getSession(){
        return session;
    }

}
