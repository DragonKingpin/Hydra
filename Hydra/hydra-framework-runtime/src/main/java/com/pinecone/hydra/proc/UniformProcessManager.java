package com.pinecone.hydra.proc;

import java.util.Collection;
import java.util.HashMap;
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
import com.pinecone.hydra.proc.ns.GenericSegregationSpace;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.ulf.util.guid.GUIDs;

public class UniformProcessManager extends ArchProcessManager implements ProcessManager {

    protected long                         mnVitalizeCount      = 0;
    protected long                         mnFatalityCount      = 0;

    protected String                       mSuperiorPathScope;
    protected Namespace                    mThisNamespace;
    protected GuidAllocator                mGuidAllocator;
    protected Processum                    mSuperiorProcess;
    protected UProcess                     mRootUProcess;
    protected RuntimeSystem                mSuperiorSystem;
    protected CascadeInstrument            mParentInstrument;
    protected KernelObjectConfig           mKernelObjectConfig;
    protected DynamicFactory               mDynamicFactory;
    protected Map<GUID, UProcess>          mProcessMap;
    protected ImageLoader                  mImageLoader;
    protected ProcessEnvironmentSection    mProcessEnvironmentSection;

    public UniformProcessManager (
            Processum superiorProcess, CascadeInstrument parentInstrument, String name, String superiorPathScope,
            KernelObjectConfig config, @Nullable ImageLoader imageLoader, @Nullable GuidAllocator guidAllocator
    ) {
        this.mSuperiorPathScope         = superiorPathScope;
        this.mSuperiorProcess           = superiorProcess;
        this.mParentInstrument          = parentInstrument;
        this.mProcessMap                = new ConcurrentHashMap<>();
        this.mKernelObjectConfig        = config;
        this.mGuidAllocator             = guidAllocator;
        this.mDynamicFactory            = new GenericDynamicFactory( superiorProcess.getTaskManager().getClassLoader() );
        this.mImageLoader               = imageLoader;
        this.mProcessEnvironmentSection = new LineageProcessEnvironmentSection( this.mSuperiorProcess.parentSystem().getEnvironmentVars() );

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
    public UProcess getRootUProcess() {
        return this.mRootUProcess;
    }

    @Override
    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    @Override
    public ProcessManagerConfig getKernelObjectConfig() {
        return (ProcessManagerConfig) this.mKernelObjectConfig;
    }

    @Override
    public void applyRootUProcess( UProcess rootUProcess ) {
        this.mRootUProcess = rootUProcess;
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
    public long    processCount() {
        return this.mProcessMap.size();
    }

    @Override
    public Collection<UProcess> fetchProcesses() {
        return this.mProcessMap.values();
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
            this.expunge( that );
        }
        else {
            throw new IllegalStateException( "Process is still alive." );
        }
    }

    @Override
    protected void expunge( UProcess that ) {
        this.mProcessMap.remove( that.getPID() );
        ++this.mnFatalityCount;
        that.triggerAfterRunnableTerminationStatus();
    }

    @Override
    public boolean autopsy( UProcess that ) {
        return that.getState() == Thread.State.TERMINATED;
    }

    @Override
    public LocalUProcess createLocalHostedProcess(
            ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars
    ) {
        if ( parent == null ) {
            parent = this.mRootUProcess;
        }
        Processum hosted = new ArchProcessum( image.getName(), parent ) {};
        hosted.setThreadAffinity( new Thread( image.getEntryPoint() ) );

        if ( startupArgs == null ) {
            startupArgs = new HashMap<>();
        }
        LocalUProcess process = new LocalHostedProcess(
                hosted, parent, this, image, new GenericSegregationSpace(), startupArgs,
                this.mProcessEnvironmentSection.extendsFrom( parent, contextEnvironmentVars )
        );

        // Register the process in the entry-point-runnable for process status surveillance purpose.
        image.getEntryPoint().applyOwnedProcess( process );
        this.register( process );

        return process;
    }

    public UProcess getProcess( GUID pid ) {
        return this.mProcessMap.get( pid );
    }

    @Override
    public String getPath( GUID objectGuid ) {
        return objectGuid.toString();
        // Process PID is the path in this case.
        // /proc/${pid}
    }

    @Override
    public String querySystemKernelObjectPath( GUID objectGuid ) {
        String thisScopePath = this.getPath( objectGuid );
        if ( thisScopePath == null ) {
            return null;
        }

        return this.getSuperiorPathScope() + this.getConfig().getPathNameSeparator() + thisScopePath;
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.queryNode( path ).getGuid();
    }

    @Override
    public EntityNode queryNode( String path ) {
        String pathSeparator = this.getKernelObjectConfig().getPathNameSeparator();

        String[] split = path.split(pathSeparator);
        if( split.length == 0 ) {
            return null;
        }
        if( split.length > 1 ) {
            // 后续补齐查找逻辑
            return null;
        }
        else {
            return this.mProcessMap.get(split[0]);
        }
    }
}
