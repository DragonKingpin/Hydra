package com.walnut.sparta.ucdn.console;

import com.pinecone.Pinecone;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNContentDelivery;


public class UCDNBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            UCDNContentDelivery ucdn = (UCDNContentDelivery) Pinecone.sys().getTaskManager().add(
                    new UCDNContentDelivery( args, Pinecone.sys() )
            );
            ucdn.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
