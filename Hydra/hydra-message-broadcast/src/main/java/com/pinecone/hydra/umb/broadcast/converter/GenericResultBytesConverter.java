package com.pinecone.hydra.umb.broadcast.converter;

import com.pinecone.framework.util.Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GenericResultBytesConverter<V > implements ResultBytesConverter<V> {
    @Override
    public byte[] convert( V value ) {
        if ( value == null ) {
            return Bytes.Empty;
        }
        else if ( value instanceof byte[] ) {
            return (byte[]) value;
        }
        else if ( value instanceof String ) {
            return ( (String) value ).getBytes();
        }


        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();

        }
        catch ( IOException e ) {
            return Bytes.Empty;
        }
    }
}
