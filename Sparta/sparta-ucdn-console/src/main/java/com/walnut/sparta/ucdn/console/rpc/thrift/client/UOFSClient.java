package com.walnut.sparta.ucdn.console.rpc.thrift.client;


import com.pinecone.hydra.thrift.client.GenericMultiplexedThriftClient;
import com.walnuts.sparta.uofs.thrift.UOFSIface;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

@Component
public class UOFSClient {
    private UOFSIface.Client uofsClient;

    public UOFSClient() throws TException {
        GenericMultiplexedThriftClient thriftClient = new GenericMultiplexedThriftClient("localhost", 16701);
        uofsClient = thriftClient.getClient("UOFS", UOFSIface.Client.class);
    }

    public String test( String msg ) throws TException {
        this.uofsClient.test( msg );
        return "成功";
    }
}
