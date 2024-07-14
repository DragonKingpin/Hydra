package com.sauron.radium.messagron;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.messagram.ArchMessagram;
import com.pinecone.hydra.messagram.MessagePackage;

public class HeistMessage extends Messageletson {
    public HeistMessage( MessagePackage msgPackage, ArchMessagram servtron ) {
        super( msgPackage, servtron );
    }

    @Override
    public void dispatch() {
        Debug.trace( this.$_MSG() );
    }

    @Override
    public void terminate(){

    }
}
