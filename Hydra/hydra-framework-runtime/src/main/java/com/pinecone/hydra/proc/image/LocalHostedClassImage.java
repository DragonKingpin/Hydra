package com.pinecone.hydra.proc.image;

import com.pinecone.hydra.proc.LocalHostedProcess;
import com.pinecone.hydra.proc.ProcessManager;

public class LocalHostedClassImage extends GenericClassImage {

    public LocalHostedClassImage(
            String name, EntryPointRunnable entryPoint, ImageLoader imageLoader
    ) throws ImageLoadProcedureException {
        super( name, entryPoint, LocalHostedProcess.class, imageLoader );
    }

    public LocalHostedClassImage(
            String name, EntryPointRunnable entryPoint, ProcessManager manager
    ) throws ImageLoadProcedureException {
        this( name, entryPoint, manager.getImageLoader() );
    }

    public LocalHostedClassImage(
            EntryPointRunnable entryPoint, ProcessManager manager
    ) throws ImageLoadProcedureException {
        this( "", entryPoint, manager.getImageLoader() );

        this.mszName = this.getClass().getName();
    }


}
