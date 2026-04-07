package com.pinecone.hydra.proc.image.kom;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.runtime.ArchRuntimeKOMTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public class VirtualMappingExeImageInstrument extends ArchRuntimeKOMTree implements VirtualExeImageInstrument {

    public VirtualMappingExeImageInstrument( @Nullable Processum superiorProcess, String superiorPathScope, KernelObjectConfig kernelObjectConfig, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, superiorPathScope, kernelObjectConfig, guidAllocator );
    }

    public VirtualMappingExeImageInstrument( @Nullable Processum superiorProcess, String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        this( superiorProcess, superiorPathScope, kernelObjectConfig, null );
    }

    public VirtualMappingExeImageInstrument( Processum superiorProcess, String superiorPathScope ) {
        this( superiorProcess, superiorPathScope, UniformCentralSystem.evalCentralSystem(superiorProcess).fundamentalKernelObjectConfig(), null );
    }

    @Override
    public ImageElement mount( String parentPath, ExecutionImage image ) {
        ImageElement element = new GenericImageElement( image, this.guidAllocator.nextGUID() );

        this.add( parentPath + this.getConfig().getPathNameSeparator() + image.getName(), element );

        return element;
    }

    @Override
    public ImageElement queryImageElement( String path ) {
        EntityNode e = this.queryNode( path );
        if ( e instanceof ImageElement ) {
            return (ImageElement) e;
        }

        return null;
    }

    @Override
    public ExecutionImage queryImage( String path ) {
        ImageElement element = this.queryImageElement( path );
        if ( element != null ) {
            return element.getImage();
        }
        return null;
    }
}
