package com.walnut.redstone.ether.object;

import java.io.IOException;
import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ObjectContentSource extends Pinenut {
    InputStream openStream() throws IOException;
}
