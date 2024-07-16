package com;

import com.pinecone.Pinecone;

public class TestJelly {
    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{



            return 0;
        }, (Object[]) args );
    }
}