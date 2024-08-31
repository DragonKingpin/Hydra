package com.pinecone.ulf.util.id;

import com.pinecone.ulf.util.id.impl.MyDefaultUidGenerator;

public class UUIDBuilder {
    public static UidGenerator getBuilder(){
        MyDefaultUidGenerator myDefaultUidGenerator = new MyDefaultUidGenerator();
        return myDefaultUidGenerator;
    }
}
