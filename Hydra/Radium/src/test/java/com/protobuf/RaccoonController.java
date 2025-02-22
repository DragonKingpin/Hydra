package com.protobuf;

import java.util.List;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
//@AddressMapping( {"/fox", "/vulpis"} )
@AddressMapping( "com.protobuf.Raccoon." )
public class RaccoonController {
    //@AddressMapping( { "/scratch", "/scratches" } )
    //@AddressMapping()
    @AddressMapping( "scratch" )
    public String scratch( String target, int time ) {
        Debug.whitef( "Raccoon invoked " + target + time  );
        return "Raccoon Scratch " + target + time;
    }

    @AddressMapping( "scratchA" )
    public String scratchA( String target, int time, Rabbit map ) {
        Debug.bluef( "Raccoon invoked " + target + time  );
        Debug.bluef( map.getName(), map.bytes.length, (Object) map.bytes, map.getMonkey().name );
        return "Raccoon Scratch " + target + time;
    }


    @AddressMapping( "scratchV" )
    public void scratchV( String target, int time ) {
        Debug.bluef( "Raccoon invoked V" + target + time  );
        //return "Raccoon Scratch " + target + time;
    }

    @AddressMapping( "scratchC" )
    public Rabbit[] scratchC( String target, int time, Rabbit[] list ) {
        Debug.bluef( "Raccoon invoked C" + target + time  );
        return list;
    }

    @AddressMapping( "scratchS" )
    public String[] scratchC(String target, int time, String[] list ) {
        Debug.bluef( "Raccoon invoked S" + target + time  );
        return list;
    }

    @AddressMapping( "scratchList" )
    public List<Rabbit> scratchList(String target, int time, List<Rabbit> list ) {
        Debug.bluef( "Raccoon invoked S" + target + time  );
        return list;
    }
}
