package com.pinecone.framework.util.lang;

public class ClassCandidateScanner extends ObjectCandidateScanner {

    public ClassCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader, NSProtocolIteratorsFactoryAdapter iteratorsFactory ) {
        super( searchScope, classLoader, iteratorsFactory );
    }

}
