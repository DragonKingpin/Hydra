package com.walnut.redstone.ether.red;

import com.pinecone.framework.system.prototype.Pinenut;

public final class RedProtocol implements Pinenut {
    public static final String Scheme = RedSchemes.Red;
    public static final String ReservedSegmentPrefix = ReservedLabels.Prefix;

    private RedProtocol() {
    }
}
