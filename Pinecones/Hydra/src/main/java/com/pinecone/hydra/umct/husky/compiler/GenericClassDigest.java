package com.pinecone.hydra.umct.husky.compiler;

import java.util.ArrayList;
import java.util.List;

public class GenericClassDigest implements ClassDigest {
    protected String                 mszClassName;

    protected String                 mszPhyClassName;

    protected List<MethodDigest >    mMethodDigests;

    public GenericClassDigest( String szClassName, String szPhyClassName ) {
        this.mszClassName    = szClassName;
        this.mszPhyClassName = szPhyClassName;
        this.mMethodDigests  = new ArrayList<>();
    }

    public GenericClassDigest( String szClassName ) {
        this( szClassName, szClassName );
    }


    @Override
    public String getClassName() {
        return this.mszClassName;
    }

    @Override
    public String getPhyClassName() {
        return this.mszPhyClassName;
    }

    @Override
    public void addMethod( MethodDigest methodDigest ) {
        this.mMethodDigests.add( methodDigest );
    }

    @Override
    public List<MethodDigest> getMethodDigests() {
        return this.mMethodDigests;
    }
}
