package com.sauron.shadow;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.sauron.radium.Radium;


public class Shadow extends Radium {
    public Shadow( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Shadow( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    protected void traceSubsystemWelcomeInfo() {
        this.pout().print( "------------------------Shadow Subsystem-----------------------\n" );
        this.pout().print( "\u001B[31m\uD83D\uDE08 Sauron`s Shadow Subsystem \uD83D\uDE08 \u001B[0m\n" );
        this.pout().print( "\u001B[32mShadow is hungry, desiring for blood.\u001B[0m\n" );

        super.traceSubsystemWelcomeInfo();
    }

    @Override
    public void vitalize () throws Exception {
        super.vitalize();
    }
}
