package com.pinecone.framework.util.json.binary;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface Bsonut extends Pinenut {
    default byte[] toBsonBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try{
            this.bsonSerialize( os );
            os.flush();
            return os.toByteArray();
        }
        catch ( IOException e ) {
            return null;
        }
    }

    void bsonSerialize( OutputStream os ) throws IOException;
}
