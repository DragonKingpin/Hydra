package com.springram;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.summer.spring.Springron;
import com.sauron.radium.Radium;

class JesusChrist extends Radium {
    public JesusChrist( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public JesusChrist( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public void vitalize () throws Exception {
        Springron springron = new Springron( "Springron", this );
        springron.execute();

        Thread shutdowner = new Thread(()->{
            Debug.sleep( 5000 );
            springron.terminate();
        });
        //shutdowner.start();

        this.getTaskManager().add( springron );
        this.getTaskManager().syncWaitingTerminated();
    }
}

public class TestSpringram {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            JesusChrist jesus = (JesusChrist) Pinecone.sys().getTaskManager().add( new JesusChrist( args, Pinecone.sys() ) );
            jesus.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
