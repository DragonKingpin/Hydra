package com.pinecone.hydra.orchestration.regulation;

import com.pinecone.hydra.orchestration.Exertion;

import java.util.ArrayList;
import java.util.List;

public class RuntimeNeglector implements NeglectRegulation {
    protected List<Class<?> > mNeglectExceptions;
    protected Exertion mParentExertion;

    public RuntimeNeglector( Exertion parent ) {
        this.mParentExertion    = parent;
        this.mNeglectExceptions = new ArrayList<>();
    }

    public List<Class<?> > getNeglectExceptions() {
        return this.mNeglectExceptions;
    }

    public Exertion getParentExertion() {
        return this.mParentExertion;
    }

    @Override
    public void add( Class<? > stereotype ) {
        this.getNeglectExceptions().add( stereotype );
    }

    @Override
    public boolean isNeglectException( Exception e ){
        List<Class<?> > neglectExceptions = this.getNeglectExceptions();

        for( Class<?> c : neglectExceptions ) {
            if( c.isInstance( e ) ) {
                return true;
            }
        }

        return false;
    }
}
