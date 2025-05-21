package com.pinecone.hydra.orchestration;

import com.pinecone.hydra.orchestration.regulation.NeglectRegulation;
import com.pinecone.hydra.system.flow.Stage;

public interface Transaction extends Exertion, Stage {
    void add( Exertion exertion );

    void addFirst( Exertion exertion );

    NeglectRegulation getSeqExceptionNeglector();

    void setSeqExceptionNeglector( NeglectRegulation neglector ) ;

    void registerExertionStartCallback( ExertionEventCallback callback );

    void registerExertionEndCallback( ExertionEventCallback callback );
}
