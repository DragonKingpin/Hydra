package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.FamilyContext;

public abstract class JPlus {
    public static Object parse ( String szJsonString ) {
        return ( new JPlusCursorParser( szJsonString, new JPlusContext() ) ).nextValue();
    }

    public static Object parse ( String szJsonString, FamilyContext context ) {
        return ( new JPlusCursorParser( szJsonString, context ) ).nextValue();
    }
}
