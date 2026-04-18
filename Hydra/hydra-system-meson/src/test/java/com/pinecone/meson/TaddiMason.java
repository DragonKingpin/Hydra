package com.pinecone.meson;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;

public class TaddiMason extends Meson {

    public TaddiMason( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public TaddiMason( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public void vitalize () throws Exception {
        Debug.greenfs( "test" );
    }

    public static void main( String[] args ) throws Exception {
        String[] as = new String[]{  };
        Pinecone.init( (Object...cfg )->{
            TaddiMason mason = new TaddiMason( as, Pinecone.sys() );
            mason.vitalize();
            return 0;
        }, (Object[]) as );
    }

}
