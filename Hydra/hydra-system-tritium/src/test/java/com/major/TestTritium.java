package com.major;

import com.pinecone.Pinecone;
import com.pinecone.tritium.Tritium;

public class TestTritium {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Tritium tritium = (Tritium) Pinecone.sys().getTaskManager().add( new Tritium( args, Pinecone.sys() ) );
            tritium.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
