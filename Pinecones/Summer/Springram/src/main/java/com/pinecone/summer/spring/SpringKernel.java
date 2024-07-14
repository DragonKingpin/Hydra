package com.pinecone.summer.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringKernel {
    private ConfigurableApplicationContext mContext;

    public void execute( String... args ) {
        this.mContext = SpringApplication.run( SpringKernel.class, args );
    }

    public void terminate() {
        if ( this.mContext != null ) {
            this.mContext.close();
        }
    }

    public ConfigurableApplicationContext getContext() {
        return this.mContext;
    }
}
