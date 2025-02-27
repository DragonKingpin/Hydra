package com.sauron;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.radium.Radium;
import com.sauron.system.SauronKingdom;

public class Sauron extends Radium implements SauronKingdom {
    public Sauron( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Sauron( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }


    @Override
    protected void traceSubsystemWelcomeInfo() {
        this.pout().print( "------------------------Sauron Framework-----------------------\n" );
        this.pout().print( "\u001B[31m\uD83D\uDE08 Bean Sauron Engine, Project.`Manhattan, the Grand Design` \uD83D\uDE08 \u001B[0m\n" );
        this.pout().print( "\u001B[32mCthulhu Data-Platform of Bean Nuts Digital IDC \u001B[0m\n" );
        this.pout().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        this.pout().print( "---------------------------------------------------------------\n" );
        this.pout().print( "Greeting! My name is Sauron, I insight omniscience.\n" );
        this.pout().print( "Salve! Nomen Sauron est, omnia perspicio.\n" );
        this.pout().print( "----------------------Kernel Information-----------------------\n" );
        this.pout().print( "PineconeVer  : Bean Nuts Pinecone Ursus " + Pinecone.VERSION + "\n" );
        this.pout().print( "PineconeAlly : Bean Nuts Acorn Hydra\n" );
        this.pout().print( "PineconeAlly : Bean Nuts Walnut Ulfhedinn (Pinecone Framework Edition)\n"   );
        this.pout().print( "PineconeAlly : Bean Nuts Walnut Sparta\n" );
        this.pout().print( "PineconeAlly : Bean Nuts Acorn Summer (Pinecone Framework Edition)\n" );
        this.pout().print( "PineconeAlly : Bean Nuts Hazelnut Slime\n"   );
        this.pout().print( "PineconeAlly : Bean Nuts Hazelnut Hydra Radium\n"   );
        this.pout().print( "PineconeAlly : Bean Nuts Acorn Summer Springron (org.springframework.boot 2.4.1)\n"   );
    }
}
