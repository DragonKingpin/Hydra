package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.GenericStartupCommandParser;

import java.util.Map;


public class TestParser {
    public static void testGenericStratupCommandParser() throws Exception{
        GenericStartupCommandParser parser = new GenericStartupCommandParser();
        Map<String, String[]> result = parser.parse(new String[]{"--key1=val1,val2", "-key2:val3;val4", "/key3=val5|val6", "--key4=1234"});

        for ( Map.Entry<String, String[]> entry : result.entrySet() ) {
            Debug.trace( entry.getKey(), (Object) entry.getValue() );
        }
    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{

            TestParser.testGenericStratupCommandParser();

            return 0;
        }, (Object[]) args );
    }
}
