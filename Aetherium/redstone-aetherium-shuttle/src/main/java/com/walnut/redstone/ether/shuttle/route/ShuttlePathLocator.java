package com.walnut.redstone.ether.shuttle.route;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ShuttlePathLocator extends Pinenut {
    ShuttleLocateResult locatePath( String path );

    ShuttleLocateResult locateUri( String uri );
}
