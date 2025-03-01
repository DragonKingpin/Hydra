package com.walnuts.sparta.uofs.console.rpc.thrift.server;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.thrift.server.MultiplexedServer;
import com.walnuts.sparta.uofs.thrift.UOFSIface;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class UOFSRPCService {
    @Resource
    private UOFSIfaceImpl uofsIface;

    @PostConstruct
    public void init(){
        new Thread(() -> {
            MultiplexedServer multiplexedServer = new MultiplexedServer(
                    new JSONMaptron("{host: \"0.0.0.0\",\n" +
                            "port: 16701, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}")
            );
            multiplexedServer.registerProcessor( "UOFS", new UOFSIface.Processor<>(this.uofsIface) );
            multiplexedServer.start();
        }).start();

    }

}
