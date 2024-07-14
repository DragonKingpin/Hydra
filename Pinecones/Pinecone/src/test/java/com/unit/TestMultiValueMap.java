package com.unit;

import com.pinecone.Pinecone;
import com.pinecone.framework.unit.LinkedMultiValueMap;
import com.pinecone.framework.util.Debug;

public class TestMultiValueMap {
    public static void testBasic() {
        LinkedMultiValueMap<Integer, String > multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add( 1, "fuck1" );
        multiValueMap.add( 2, "fuck2" );
        multiValueMap.add( 1, "fuck1_1" );

        Debug.trace( multiValueMap );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            TestMultiValueMap.testBasic();

            return 0;
        }, (Object[]) args );
    }
}
