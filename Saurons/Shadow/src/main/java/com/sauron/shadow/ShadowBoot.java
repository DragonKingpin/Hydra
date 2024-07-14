package com.sauron.shadow;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ShadowBoot {
    public static Shadow shadow = null;

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Shadow shadow = (Shadow) Pinecone.sys().getTaskManager().add( new Shadow( args, Pinecone.sys() ) );

            if( shadow.getMasterQuery() ) {
                ShadowBoot.shadow = shadow;
                SpringApplication.run( ShadowBoot.class, args );
            }
            else {
                shadow.vitalize();
            }
            return 0;
        }, (Object[]) args );
    }

    @PostConstruct
    public void init() {
        try {
            ShadowBoot.shadow.vitalize();
        }
        catch ( Exception e ) {
            ShadowBoot.shadow.console().cerr( e.getMessage() );
        }
    }
}