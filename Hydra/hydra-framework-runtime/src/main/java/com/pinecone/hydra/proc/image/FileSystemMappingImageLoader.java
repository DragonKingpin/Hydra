package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.proc.image.kom.ImageElement;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.proc.image.path.ImageInstrumentPath;
import com.pinecone.hydra.proc.image.path.ImagePosixPath;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.system.imperium.KernelObjectRootMountPoint;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public class FileSystemMappingImageLoader extends ArchImageLoader implements ImageLoader {

    protected VirtualExeImageInstrument mVirtualExeImageInstrument;

    public FileSystemMappingImageLoader( Namespace name, Hydrogen system, HyComponent parent, ClassLoader classLoader, @Nullable VirtualExeImageInstrument virtualExeImageInstrument ) {
        super( name, system, parent, classLoader );

        this.mVirtualExeImageInstrument = virtualExeImageInstrument;

        if ( virtualExeImageInstrument == null ) {
            if ( system instanceof UniformCentralSystem) {
                KOMInstrument e = ((UniformCentralSystem) system).imperiumPrivy().getExpressInstrument().getMountedInstrument( KernelObjectRootMountPoint.SysImages.getMountPoint() );
                if ( e instanceof VirtualExeImageInstrument ) {
                    this.mVirtualExeImageInstrument = (VirtualExeImageInstrument) e;
                }
            }
        }
    }

    public FileSystemMappingImageLoader( Hydrogen system, HyComponent parent, @Nullable VirtualExeImageInstrument virtualExeImageInstrument ) {
        this( null, system, parent, system.getGlobalClassLoader(), virtualExeImageInstrument );
    }

    public FileSystemMappingImageLoader( Hydrogen system, @Nullable VirtualExeImageInstrument virtualExeImageInstrument ) {
        this( system, null, virtualExeImageInstrument );
    }


    @Override
    public ExecutionImage queryExecutionImage( String path ) {
        String szPath = ImagePosixPath.normalize( path );
        String szInstrumentPath = ImageInstrumentPath.normalize( szPath );
        ExecutionImage image = this.mVirtualExeImageInstrument.queryImage( szInstrumentPath );
        if ( image != null ) {
            return image;
        }

        if ( this.getSystem() instanceof UniformCentralSystem ) {
            EntityNode e = ((UniformCentralSystem) this.getSystem()).imperiumPrivy().getExpressInstrument().queryNode( szInstrumentPath );
            if ( e instanceof ImageElement) {
                return ((ImageElement) e).getImage();
            }
        }

        return null;
    }

    @Override
    public void registerLocalScopeExecutionImage( String dirPath, ExecutionImage image ) {
        this.mVirtualExeImageInstrument.mount( ImageInstrumentPath.normalize( dirPath ), image );
    }
}
