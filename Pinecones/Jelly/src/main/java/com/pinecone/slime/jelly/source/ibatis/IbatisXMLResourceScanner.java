package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.lang.GenericScopeNSProtocolIteratorsFactory;
import com.pinecone.framework.util.lang.NSProtocolIteratorsFactoryAdapter;
import com.pinecone.framework.util.lang.ObjectCandidateScanner;
import com.pinecone.slime.source.XMLResourceScanner;

public class IbatisXMLResourceScanner extends ObjectCandidateScanner implements XMLResourceScanner {

    public IbatisXMLResourceScanner     ( ClassScope searchScope, ClassLoader classLoader, NSProtocolIteratorsFactoryAdapter iteratorsFactory ) {
        super( searchScope, classLoader, iteratorsFactory );
    }

    public IbatisXMLResourceScanner     ( ClassScope searchScope, ClassLoader classLoader ) {
        this( searchScope, classLoader, new GenericScopeNSProtocolIteratorsFactory( classLoader, searchScope, ".xml" ) );
    }

}
