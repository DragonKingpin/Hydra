package com.pinecone.hydra.proc.image.kom;

import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface VirtualExeImageInstrument extends KOMInstrument {

    ImageElement mount( String parentPath, ExecutionImage image ) ;

    ImageElement queryImageElement( String path ) ;

    ExecutionImage queryImage( String path ) ;

}
