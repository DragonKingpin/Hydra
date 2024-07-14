package com.major;

import com.pinecone.Pinecone;
import com.sauron.radium.Radium;

public class TestRadium {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Radium radium = (Radium) Pinecone.sys().getTaskManager().add( new Radium( args, Pinecone.sys() ) );
            radium.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
