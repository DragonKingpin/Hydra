package com.acorn.redqueen.service;

import com.pinecone.hydra.service.Application;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;

public interface RedApplication extends Application {

    ApplicationElement getApplicationElement();

}
