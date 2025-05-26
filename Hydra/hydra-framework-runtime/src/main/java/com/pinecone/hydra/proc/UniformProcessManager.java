package com.pinecone.hydra.proc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.UniformImageLoader;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.ulf.util.guid.GUIDs;

public class UniformProcessManager implements ProcessManager {

    protected long                   mnVitalizeCount      = 0;
    protected long                   mnFatalityCount      = 0;

    protected String                 mSuperiorPathScope;
    protected Namespace              mThisNamespace;
    protected GuidAllocator          mGuidAllocator;
    protected Processum              mSuperiorProcess;
    protected RuntimeSystem          mSuperiorSystem;
    protected CascadeInstrument      mParentInstrument;
    protected KernelObjectConfig     mKernelObjectConfig;
    protected DynamicFactory         mDynamicFactory;
    protected Map<GUID, EntityNode>  mProcessMap;
    protected ImageLoader            mImageLoader;

    public UniformProcessManager (
            Processum superiorProcess, CascadeInstrument parentInstrument, String name, String superiorPathScope,
            KernelObjectConfig config, @Nullable ImageLoader imageLoader, @Nullable GuidAllocator guidAllocator
    ) {
        this.mSuperiorPathScope   = superiorPathScope;
        this.mSuperiorProcess     = superiorProcess;
        this.mParentInstrument    = parentInstrument;
        this.mProcessMap          = new ConcurrentHashMap<>();
        this.mKernelObjectConfig  = config;
        this.mGuidAllocator       = guidAllocator;
        this.mDynamicFactory      = new GenericDynamicFactory( superiorProcess.getTaskManager().getClassLoader() );
        this.mImageLoader         = imageLoader;

        if ( this.mSuperiorProcess instanceof RuntimeSystem ) {
            this.mSuperiorSystem = (RuntimeSystem) this.mSuperiorProcess;
        }
        else {
            this.mSuperiorSystem = this.mSuperiorProcess.parentSystem();
        }

        this.setTargetingName( name );


        if ( this.mSuperiorProcess != null ) {
            if ( this.mSuperiorProcess instanceof RuntimeSystem ) {
                this.mSuperiorSystem = (RuntimeSystem) this.mSuperiorProcess;
            }
            else  {
                this.mSuperiorSystem = this.mSuperiorProcess.parentSystem();
            }
            if ( this.mSuperiorSystem instanceof UniformCentralSystem ) {
                UniformCentralSystem system = (UniformCentralSystem) this.mSuperiorSystem;
                if ( this.mGuidAllocator == null ) {
                    this.mGuidAllocator = system.getSystemGuidAllocator();
                }
                if ( this.mImageLoader == null ) {
                    this.mImageLoader = (ImageLoader) system.imageLoader();
                }
            }
        }

        if ( this.mGuidAllocator == null ) {
            this.mGuidAllocator = GUIDs.newGuidAllocator();
        }

        if ( this.mImageLoader == null ) {
            this.mImageLoader = new UniformImageLoader( (Hydrogen) this.superiorSystem() );
        }
    }

    public UniformProcessManager (
            Processum superiorProcess, CascadeInstrument parentInstrument, String name, String superiorPathScope,
            KernelObjectConfig config
    ) {
        this( superiorProcess, parentInstrument, name, superiorPathScope, config, null, null );
    }


    @Override
    public void applyGuidAllocator( GuidAllocator guidAllocator ) {
        this.mGuidAllocator = guidAllocator;
    }

    @Override
    public Processum superiorProcess() {
        return this.mSuperiorProcess;
    }

    @Override
    public RuntimeSystem superiorSystem() {
        return this.mSuperiorSystem;
    }

    @Override
    public CascadeInstrument parent() {
        return this.mParentInstrument;
    }

    @Override
    public void setParent( CascadeInstrument parent ) {
        this.mParentInstrument = parent;
    }

    @Override
    public Namespace getTargetingName() {
        return this.mThisNamespace;
    }

    @Override
    public void setTargetingName( Namespace name ) {
        this.mThisNamespace = name;
    }

    @Override
    public String getSuperiorPathScope() {
        return this.mSuperiorPathScope;
    }

    @Override
    public void applySuperiorPathScope( String superiorPathScope ) {
        this.mSuperiorPathScope = superiorPathScope;
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.mKernelObjectConfig;
    }




    @Override
    public long    getVitalizeCount() {
        return this.mnVitalizeCount;
    }

    @Override
    public long    getFatalityCount() {
        return this.mnFatalityCount;
    }

    @Override
    public void register( UProcess that ) {
        if( !this.autopsy( that ) ) {
            this.mProcessMap.put( that.getPID(), that );
            ++this.mnVitalizeCount;
        }
        else {
            throw new IllegalStateException( "Process is dead." );
        }
    }

    @Override
    public void erase( UProcess that ) {
        if( this.autopsy( that ) ) {
            this.mProcessMap.remove( that.getPID() );
            ++this.mnFatalityCount;
            that.triggerUpdateTerminationStatus();
        }
        else {
            throw new IllegalStateException( "Process is still alive." );
        }
    }

    @Override
    public boolean autopsy( UProcess that ) {
        return that.getState() == Thread.State.TERMINATED;
    }
//
//    public LocalUProcess createLocalHostedProcess( ExecutionImage image, UProcess parent ) {
//        Processum hosted = new ArchProcessum( image.getName(), parent ) {};
//        hosted.setThreadAffinity( new Thread( image.getEntryPoint() ) );
//
//        LocalUProcess process = new LocalHostedProcess( hosted )
//    }
}
