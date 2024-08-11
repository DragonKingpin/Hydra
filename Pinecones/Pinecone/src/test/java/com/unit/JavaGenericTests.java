package com.unit;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;

public class JavaGenericTests {
    public static void testBasic() {
        Map<String, Integer > instance = new LinkedHashMap<>();

        Class<?> clazz = instance.getClass();

        Type genericSuperclass = clazz.getGenericSuperclass();
        if ( genericSuperclass instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            for ( Type type : actualTypeArguments ) {
                Debug.trace( type.getTypeName() );
            }
        }
    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{

            JavaGenericTests.testBasic();

            return 0;
        }, (Object[]) args );
    }
}
