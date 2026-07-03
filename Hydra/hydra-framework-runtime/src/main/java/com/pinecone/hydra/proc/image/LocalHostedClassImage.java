package com.pinecone.hydra.proc.image;

import com.pinecone.hydra.proc.LocalHostedProcess;
import com.pinecone.hydra.proc.ProcessManager;

public class LocalHostedClassImage extends GenericClassImage {

    public LocalHostedClassImage(
            String name, EntryPointRunnableFactory entryPointFactory, ImageLoader imageLoader
    ) throws ImageLoadProcedureException {
        super( name, entryPointFactory, LocalHostedProcess.class, imageLoader );
    }

    public LocalHostedClassImage(
            String name, EntryPointRunnableFactory entryPointFactory, ProcessManager manager
    ) throws ImageLoadProcedureException {
        this( name, entryPointFactory, manager.getImageLoader() );
    }

    public LocalHostedClassImage(
            EntryPointRunnableFactory entryPointFactory, ProcessManager manager
    ) throws ImageLoadProcedureException {
        this( "", entryPointFactory, manager.getImageLoader() );

        this.mszName = this.getClass().getName();
    }


}
