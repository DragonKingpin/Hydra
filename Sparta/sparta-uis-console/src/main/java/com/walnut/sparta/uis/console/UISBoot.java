package com.walnut.sparta.uis.console;

import com.pinecone.Pinecone;
import com.walnut.sparta.uis.console.infrastructure.UISContentDelivery;

public class UISBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            UISContentDelivery uofs = (UISContentDelivery) Pinecone.sys().getTaskManager().add(
                    new UISContentDelivery( args, Pinecone.sys() )
            );
            uofs.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
