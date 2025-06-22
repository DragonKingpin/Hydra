package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.architecture.Component;

public interface ImageLoader extends Component {

    ClassLoader getClassLoader();

    ExecutionImage queryExecutionImage( String path );

    void registerLocalScopeExecutionImage ( String dirPath, ExecutionImage image );

}
