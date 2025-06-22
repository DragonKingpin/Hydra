package com.pinecone.hydra.proc.image;

import java.net.URI;

public interface URLImageLoader extends ImageLoader {

    ExecutionImage queryExecutionImage( URI uri );

}
