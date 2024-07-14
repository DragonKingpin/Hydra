package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Iterator;

public interface FamilyIterator<V > extends Iterator<UnitFamilyNode<?, V > >, Pinenut {
    @Override
    UnitFamilyNode<Object, V > next();
}
