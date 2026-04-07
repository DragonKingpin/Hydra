package com.pinecone.hydra.proc.image;

import java.net.URI;
import java.net.URISyntaxException;

import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.UProcess;

public class GenericClassImage extends ArchExecutionImage implements JVMClassExecutionImage {

    static URI evalClassURI( Class<?> clazz ) throws ImageLoadProcedureException {
        try {
            return clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
        }
        catch ( URISyntaxException e ) {
            throw new ImageLoadProcedureException( e );
        }
    }

    public GenericClassImage(
            String name, EntryPointRunnable entryPoint, Class<? extends UProcess> processClassType, ImageLoader imageLoader
    ) throws ImageLoadProcedureException {
        super(
                name, entryPoint, processClassType, evalClassURI( entryPoint.getClass() ),
                imageLoader, JVMClassExecutionImage.class.getSimpleName(), ControllableLevel.Absolute
        );
    }

}
