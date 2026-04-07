package com.pinecone.hydra.proc.image.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.image.ExecutionImage;

public class GenericImageElement implements ImageElement {
    protected ExecutionImage mImage;
    protected GUID           mGUID;

    public GenericImageElement() {

    }

    public GenericImageElement( ExecutionImage image, GUID guid ) {
        this.mImage = image;
        this.mGUID  = guid;
    }

    @Override
    public ExecutionImage getImage() {
        return this.mImage;
    }

    @Override
    public String getName() {
        return this.mImage.getName();
    }

    @Override
    public GUID getGuid() {
        return this.mGUID;
    }
}
