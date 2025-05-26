package com.walnut.archcraft.ender.system;

import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.GenericClassImage;
import com.pinecone.hydra.proc.image.ImageLoadProcedureException;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.JVMClassExecutionImage;

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
                system, "SystemProcess", new EntryPointRunnable() {
                    UProcess mOwnedProcess;

                    @Override
                    public void applyOwnedProcess( UProcess process ) {
                        this.mOwnedProcess = process;
                    }

                    @Override
                    public UProcess ownedProcess() {
                        return this.mOwnedProcess;
                    }

                    @Override
                    public void run() {
                        system.start();
                    }
                },
                Hydroxy.class, system.imageLoader()
        );
    }


}
