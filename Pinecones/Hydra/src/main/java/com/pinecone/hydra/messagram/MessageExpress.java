package com.pinecone.hydra.messagram;

import com.pinecone.hydra.express.Deliver;
import com.pinecone.hydra.express.Express;

public interface MessageExpress extends Express {

    String getName();

    ArchMessagram getMessagram();

    MessageDeliver  recruit ( String szName );

    MessageExpress  register( Deliver deliver );

    MessageExpress  fired   ( Deliver deliver );



}
