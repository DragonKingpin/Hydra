package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.architecture.Component;

public interface ImageLoader extends Component {

    ClassLoader getClassLoader();

}
