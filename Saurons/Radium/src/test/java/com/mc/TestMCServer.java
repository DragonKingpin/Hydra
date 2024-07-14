package com.mc;

import com.pinecone.hydra.messagram.WolfMCExpress;
import com.pinecone.hydra.umc.wolfmc.server.WolfMCServer;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.sauron.radium.messagron.Messagron;

import java.util.Set;


class Christ extends JesusChrist {
    public Christ( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Christ( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        // this.testServer();
        this.testSystemServer();
    }

    public void testServer() throws Exception {
        Messagron messagron = new Messagron( "", this, new JSONMaptron() );
        WolfMCServer wolf = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );

        WolfMCExpress express = new WolfMCExpress( messagron );
        wolf.apply( express );

        wolf.execute();

        this.getTaskManager().add( wolf );
        this.getTaskManager().syncWaitingTerminated();
    }

    public void testSystemServer() throws Exception {
//        WolfMCServer wolf   = (WolfMCServer)this.getMiddlewareManager().getMessagersManager().getMessageNodeByName( "WolfKing" );
//        wolf.execute();
//
//        this.getTaskManager().add( wolf );


        this.getTaskManager().syncWaitingTerminated();
    }
}

public class TestMCServer {
    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            Christ christ = (Christ) Pinecone.sys().getTaskManager().add( new Christ( args, Pinecone.sys() ) );
            christ.vitalize();

            return 0;
        }, (Object[]) args );
    }
}
