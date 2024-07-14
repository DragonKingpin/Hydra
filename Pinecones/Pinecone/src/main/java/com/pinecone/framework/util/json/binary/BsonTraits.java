package com.pinecone.framework.util.json.binary;

import com.pinecone.framework.util.ReflectionUtils;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BsonTraits {
    public static String FUN_TO_BSON_BYTES_NAME          = "toBsonBytes";
    public static String FUN_BSON_SERIALIZE_NAME         = "bsonSerialize";

    public static byte[] invokeToBsonBytes    ( Object that ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method fnToBsonBytes = that.getClass().getMethod( BsonTraits.FUN_TO_BSON_BYTES_NAME );
        ReflectionUtils.makeAccessible( fnToBsonBytes );
        return (byte[]) fnToBsonBytes.invoke( that );
    }

    public static void invokeBsonSerialize    ( Object that, OutputStream os ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method fnBsonSerialize = that.getClass().getMethod( BsonTraits.FUN_BSON_SERIALIZE_NAME, OutputStream.class );
        ReflectionUtils.makeAccessible( fnBsonSerialize );
        fnBsonSerialize.invoke( that, os );
    }
}
