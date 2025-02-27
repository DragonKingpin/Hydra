package com.walnut.sparta.ucdn.service;

import com.pinecone.Pinecone;
import com.walnut.sparta.ucdn.service.infrastructure.UOFSContentDelivery;


public class UCDNBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            UOFSContentDelivery ucdn = (UOFSContentDelivery) Pinecone.sys().getTaskManager().add(
                    new UOFSContentDelivery( args, Pinecone.sys() )
            );
            ucdn.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
