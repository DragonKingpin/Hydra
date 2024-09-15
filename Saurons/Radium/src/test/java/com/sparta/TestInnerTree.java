package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.sauron.radium.Radium;
import com.walnut.sparta.Sparta;

class LadyGaga extends Radium {
    public LadyGaga( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public LadyGaga( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        Sparta sparta = new Sparta( "Sparta", this );
        sparta.execute();

        Thread shutdowner = new Thread(()->{
            Debug.sleep( 5000 );
            sparta.terminate();
        });
        //shutdowner.start();

        this.getTaskManager().add( sparta );
        this.getTaskManager().syncWaitingTerminated();
    }
}


public class TestInnerTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            LadyGaga ladyGaga = (LadyGaga) Pinecone.sys().getTaskManager().add( new LadyGaga( args, Pinecone.sys() ) );
            ladyGaga.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
