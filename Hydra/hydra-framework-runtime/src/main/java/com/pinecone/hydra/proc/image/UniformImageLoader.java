package com.pinecone.hydra.proc.image;

public interface UniformImageLoader extends URLImageLoader {

    ImageLoader localMappingImageLoader();

    void addScope( String protocol, ImageLoader imageLoader );

    ImageLoader getScope( String protocol );

}
