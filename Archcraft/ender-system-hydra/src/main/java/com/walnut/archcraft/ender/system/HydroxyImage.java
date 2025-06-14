package com.walnut.archcraft.ender.system;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.GenericClassImage;
import com.pinecone.hydra.proc.image.ImageLoadProcedureException;
import com.pinecone.hydra.proc.image.ImageLoader;

public class HydroxyImage extends GenericClassImage {

    public HydroxyImage(
            HydraEmpire system, String name, EntryPointRunnable entryPoint, Class<? extends UProcess> processClassType, ImageLoader imageLoader
    ) throws ImageLoadProcedureException {
        super(
                name, entryPoint, processClassType, imageLoader
        );
    }

    public HydroxyImage( HydraEmpire system ) throws ImageLoadProcedureException {
        this(
                system, "SystemProcess", new ArchEntryPointRunnable() {
                    @Override
                    public void execute() {
                        system.start();
                    }
                },
                Hydroxy.class, system.imageLoader()
        );
    }


}
