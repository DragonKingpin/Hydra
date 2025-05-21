package com.ender;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.deploy.ibatis.hydranium.DeployMappingDriver;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.registry.GenericKOMRegistry;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMappingDriver;
import com.pinecone.hydra.reign.UnixInstitutionalizedMetaImperiumPrivy;
import com.pinecone.hydra.storage.mfs.MappingFileSystem;
import com.pinecone.hydra.storage.file.external.ExternalFolder;
import com.pinecone.hydra.storage.mfs.NativeMFile;
import com.pinecone.hydra.storage.mfs.NativeMappingFileSystem;
import com.pinecone.hydra.system.imperium.KernelObjectRootMountPoint;
import com.pinecone.hydra.system.imperium.KernelRootMountPoint;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.kom.ExpressInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

class Floki extends EnderHydra {
    public Floki( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Floki( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        UnixInstitutionalizedMetaImperiumPrivy privy = new UnixInstitutionalizedMetaImperiumPrivy( this, null );

        ExpressInstrument instrument = privy.getExpressInstrument();


        //this.prepareKOMTrees( instrument );

        //MappingFileSystem mappingFileSystem = new NativeMappingFileSystem( "E:/" );
        MappingFileSystem mappingFileSystem = new NativeMappingFileSystem( "/" );
        instrument.directMount( KernelRootMountPoint.Mount.getMountPoint() + "/volE", mappingFileSystem);


        this.testSimple( instrument );
    }

    private void prepareKOMTrees( ExpressInstrument instrument ) {
        OdinUniformTaskMappingDriver categoryMappingDriver = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        RavenTaskInstrument ravenTaskInstrument = new RavenTaskInstrument( categoryMappingDriver );


        KOIMappingDriver koiMappingDriver = new RegistryMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        KOMRegistry registry = new GenericKOMRegistry( koiMappingDriver );

        DeployMappingDriver deployMappingDriver = new DeployMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        UniformDeployInstrument deployInstrument = new UniformDeployInstrument( deployMappingDriver );

        instrument.mount( KernelObjectRootMountPoint.TaskMeta.getMountPoint(), ravenTaskInstrument );
        instrument.mount( KernelObjectRootMountPoint.Registry.getMountPoint(), registry );
        instrument.mount( KernelObjectRootMountPoint.DeployMeta.getMountPoint(), deployInstrument );
    }

    private void testSimple( ExpressInstrument instrument ) {
//        EntityNode entityNode = instrument.queryNode( "meta/task/test/job/task" );
//
//        //Debug.fmp( 2, entityNode );
//        Debug.fmp( 2, instrument.querySystemKernelObjectPath( entityNode.getGuid() ) );
//
//        Debug.fmp( 2, instrument.getMountedInstrument( "meta/task" ) );
//
//        Debug.greenfs( instrument.fetchOwnMappingPath() );
//
//        Debug.fmp( 2, instrument.queryNode( "conf/registry/game3a/witcher/people/s4/urge" ) );
//        Debug.fmp( 2, instrument.queryNode( "conf/registry/game3a/witcher/people/s4/urge" ) );
//
//        Debug.fmp( 2, instrument.queryNode( "dev/deploy/root/test/cluster/vm1" ) );


        EntityNode myf = instrument.queryNode( "mnt/volE/Users" );
        Debug.fmp( 2, myf );
        NativeMFile myff = (NativeMFile) myf;

        Debug.fmp( 2, myff.listFiles() );

    }
}

public class TestEnderHydra {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Floki loki = (Floki) Pinecone.sys().getTaskManager().add( new Floki( args, Pinecone.sys() ) );
            loki.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
