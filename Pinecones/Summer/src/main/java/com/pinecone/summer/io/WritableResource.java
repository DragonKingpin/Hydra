package com.pinecone.summer.io;

import java.io.IOException;
import java.io.OutputStream;

public interface WritableResource extends Resource {
    boolean isWritable();

    OutputStream getOutputStream() throws IOException;
}