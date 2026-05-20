package com.walnut.redstone.ether;

import com.pinecone.framework.system.regime.Instrument;
import com.walnut.redstone.ether.object.ObjectContent;
import com.walnut.redstone.ether.object.ObjectListRequest;
import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;
import com.walnut.redstone.ether.operation.OperationContext;
import com.walnut.redstone.ether.resource.ResourcePath;

public interface ObjectExpressInstrument extends Instrument {
    ObjectMetadata head( ResourcePath path, OperationContext context );

    ObjectContent get( ResourcePath path, OperationContext context );

    ObjectMetadata put( ResourcePath path, ObjectContent content, OperationContext context );

    void delete( ResourcePath path, OperationContext context );

    ObjectListResult list( ResourcePath path, ObjectListRequest request, OperationContext context );
}

