package com.pinecone.ulf.util.id;

import com.pinecone.ulf.util.id.impl.myDefaultUidGenerator;

public class UUIDBuilder {
    public static UidGenerator getBuilder(){
        myDefaultUidGenerator myDefaultUidGenerator = new myDefaultUidGenerator();
        return myDefaultUidGenerator;
    }
}
