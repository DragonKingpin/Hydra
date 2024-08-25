package com.pinecone.framework.util.uoi;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UniformObjectLoader extends Pinenut {
    Class<? > toClass( UOI uoi ) ;

    Object newInstance( UOI uoi, Class<? >[] paramTypes, Object... args );

    Object newInstance( UOI uoi, Object... args );
}
