package com.walnuts.sparta.uofs.console;

import com.pinecone.Pinecone;
import com.walnuts.sparta.uofs.console.infrastructure.UOFSContentDelivery;

public class UOFSBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            UOFSContentDelivery uofs = (UOFSContentDelivery) Pinecone.sys().getTaskManager().add(
                    new UOFSContentDelivery( args, Pinecone.sys() )
            );
            uofs.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
