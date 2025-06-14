package com.walnut.sparta.utask.console;

import com.pinecone.Pinecone;
import com.walnut.sparta.utask.console.infrastructure.UTASKContentDelivery;

public class UTASKBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            UTASKContentDelivery utask = (UTASKContentDelivery) Pinecone.sys().getTaskManager().add(
                    new UTASKContentDelivery( args, Pinecone.sys() )
            );
            utask.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
