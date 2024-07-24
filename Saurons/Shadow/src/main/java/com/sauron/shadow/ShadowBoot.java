package com.sauron.shadow;

import com.pinecone.Pinecone;

public class ShadowBoot {
    public static Shadow shadow = null;

    public static void main( String[] args ) throws Exception {
        ShadowBoot.shadow = new Shadow( args, Pinecone.sys() );
        ShadowBoot.shadow.init( (Object...cfg )->{
            ShadowBoot.shadow.vitalize();

            return 0;
        }, (Object[]) args );
    }
}