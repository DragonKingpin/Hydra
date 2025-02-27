package com.walnut.sparta.ucdn.console.domain.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.GUIDs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/ucdn")
@Component
@Slf4j
public class WebSocketService {
    private static Session session;

    @OnOpen
    public void onOpen(Session session){
        WebSocketService.session = session;
    }

    @OnMessage
    public void onMessage( String msg, Session session ){
        log.info(msg);
    }

    @OnError
    public void onError( Session session, Throwable error ){
        log.info("成功关闭");
    }

    public Session getSession(){
        return session;
    }

}
