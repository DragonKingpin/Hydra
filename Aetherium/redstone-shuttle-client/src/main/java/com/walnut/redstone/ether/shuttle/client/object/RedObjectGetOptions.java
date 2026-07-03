package com.walnut.redstone.ether.shuttle.client.object;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectRange;

public class RedObjectGetOptions implements Pinenut {
    protected ObjectRange range;

    public static RedObjectGetOptions empty() {
        return new RedObjectGetOptions();
    }

    public ObjectRange getRange() {
        return this.range;
    }

    public void setRange( ObjectRange range ) {
        this.range = range;
    }
}
