package com.sauron.shadow;

import com.pinecone.framework.system.CascadeSystem;
import com.sauron.Sauron;


public class Shadow extends Sauron {
    public Shadow( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Shadow( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    protected void traceSubsystemWelcomeInfo() {
        super.traceSubsystemWelcomeInfo();
        this.pout().print( "------------------------Shadow Subsystem-----------------------\n" );
        this.pout().print( "\u001B[31m\uD83D\uDE08 Sauron`s Shadow Subsystem \uD83D\uDE08 \u001B[0m\n" );
        this.pout().print( "\u001B[32mShadow is hungry, desiring for blood.\u001B[0m\n" );
        this.pout().print( "---------------------------------------------------------------\n" );
    }

    @Override
    public void vitalize () throws Exception {
        super.vitalize();
    }
}
