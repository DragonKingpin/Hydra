package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.PineRuntimeException;
import com.pinecone.slime.chunk.ContiguousPage;

import java.lang.reflect.InvocationTargetException;

public class DirectPagePool implements PagePool {
    protected Class<? extends ContiguousPage> stereotype;

    public DirectPagePool( Class<? extends ContiguousPage> stereotype ){
        this.stereotype = stereotype;
    }

    @Override
    public int size(){
        return Integer.MAX_VALUE - 2;
    }

    @Override
    public ContiguousPage allocate( Object... args ){
        ContiguousPage page;

        try {
            page = this.stereotype.getDeclaredConstructor().newInstance();
        }
        catch ( InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e ) {
            throw new PineRuntimeException( "Failed to allocate a new page.", e );
        }

        page.apply( args );

        return page;
    }

    @Override
    public void deallocate( ContiguousPage that ){

    }
}
