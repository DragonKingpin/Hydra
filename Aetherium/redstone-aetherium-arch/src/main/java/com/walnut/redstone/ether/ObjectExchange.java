package com.walnut.redstone.ether;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectContent;
import com.walnut.redstone.ether.object.ObjectListRequest;
import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;
import com.walnut.redstone.ether.operation.OperationContext;
import com.walnut.redstone.ether.resource.ResourcePath;

public interface ObjectExchange extends Pinenut {
    ObjectMetadata head( ObjectExpressInstrument instrument, ResourcePath path, OperationContext context );

    ObjectContent get( ObjectExpressInstrument instrument, ResourcePath path, OperationContext context );

    ObjectMetadata put( ObjectExpressInstrument instrument, ResourcePath path, ObjectContent content, OperationContext context );

    void delete( ObjectExpressInstrument instrument, ResourcePath path, OperationContext context );

    ObjectListResult list( ObjectExpressInstrument instrument, ResourcePath path, ObjectListRequest request, OperationContext context );
}

