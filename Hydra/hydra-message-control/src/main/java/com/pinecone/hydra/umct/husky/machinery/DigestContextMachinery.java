package com.pinecone.hydra.umct.husky.machinery;

import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.lang.ScopedPackage;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.mapping.ControllerInspector;


/**
 *  Pinecone Ursus For Java Hydra Ulfar, DigestContextMachinery
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 */
public class DigestContextMachinery extends DigestTransformer implements MCTContextMachinery {
    protected DynamicFactory       mIfaceFactory;
    protected MultiMappingLoader   mMultiMappingLoader;

    public DigestContextMachinery( InterfacialCompiler compiler, ControllerInspector controllerInspector ) {
        super( compiler, controllerInspector );

        this.mIfaceFactory       = new GenericDynamicFactory( controllerInspector.getClassLoader() );
        this.mMultiMappingLoader = new DigestMappingLoader( this.mIfaceFactory, this );
    }

    @Override
    public MultiMappingLoader getMultiMappingLoader() {
        return this.mMultiMappingLoader;
    }

    @Override
    public MCTContextMachinery addScope ( String szPackageName ) {
        this.mIfaceFactory.getClassScope().addScope( szPackageName );
        return this;
    }

    @Override
    public MCTContextMachinery addScope ( ScopedPackage scope ) {
        this.mIfaceFactory.getClassScope().addScope( scope );
        return this;
    }
}
