package com.uofs;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.walnut.archcraft.ender.EnderHydra;
import org.apache.ibatis.session.SqlSession;

class UofsManualHydra extends EnderHydra {
    public UofsManualHydra( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UofsManualHydra( String[] args, String szName, CascadeSystem parent ) {
        super( args, szName, parent );
    }

    @Override
    public void vitalize() throws Exception {
        Debug.trace( "Titan UOFS manual smoke start" );
        UofsSmokePaths.ensureLocalWorkspace();

        IbatisClient ibatisClient = this.openIbatisClient();
        FileMappingDriver fileMappingDriver = new FileMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );
        VolumeMappingDriver volumeMappingDriver = new VolumeMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );

        try ( SqlSession sqlSession = ibatisClient.openSession( true ) ) {
            UofsSmokeContext context = new UofsSmokeContext(
                    this,
                    ibatisClient,
                    sqlSession,
                    fileMappingDriver,
                    volumeMappingDriver,
                    this.bindMappers( volumeMappingDriver )
            );

            // Keep one case per line so manual review can enable/disable scenarios easily.
            //UofsWriteReadSmoke.DIRECT_OBJECT.run( context );
            //UofsWriteReadSmoke.BLOCK_SIMPLE_THIN.run( context );
            //UofsWriteReadSmoke.BLOCK_SIMPLE_THICK.run( context );
            //UofsWriteReadSmoke.BLOCK_SPANNED.run( context );
            UofsWriteReadSmoke.BLOCK_STRIPED.run( context );
            //UofsGeneratedSmoke.THIN_WINDOW_BOUNDARY.run( context );
            //UofsGeneratedSmoke.SMALL_FILE_BOUNDARY.run( context );
            //UofsGeneratedSmoke.SPANNED_BOUNDARY.run( context );
            //UofsGeneratedSmoke.STRIPED_BOUNDARY.run( context );
            //UofsGeneratedSmoke.MULTI_CHUNK.run( context );
        }

        Debug.trace( "Titan UOFS manual smoke done" );
    }

    protected IbatisClient openIbatisClient() {
        return (IbatisClient) this.getMiddlewareDirector()
                .getRDBManager()
                .getRDBClientByName( "MySQLKingHydranium" );
    }

    protected UofsSmokeMappers bindMappers( VolumeMappingDriver volumeMappingDriver ) {
        return new UofsSmokeMappers( (VolumeMasterManipulator) volumeMappingDriver.getMasterManipulator() );
    }

    public KOMFileSystem createFileSystem( FileMappingDriver fileMappingDriver, GUID defaultVolumeGuid ) {
        return this.createFileSystem( fileMappingDriver, defaultVolumeGuid, UofsSmokePaths.TEMP_FOLDER );
    }

    public KOMFileSystem createFileSystem( FileMappingDriver fileMappingDriver, GUID defaultVolumeGuid, String tempFolder ) {
        return new UniformObjectFileSystem(
                fileMappingDriver.getSuperiorProcess(),
                fileMappingDriver.getMasterManipulator(),
                null,
                KOMFileSystem.class.getSimpleName(),
                null,
                UofsSmokeTools.newFileSystemConfig( defaultVolumeGuid, tempFolder ),
                this.getSystemGuidAllocator()
        );
    }
}

public class TestUOFS {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            UofsManualHydra smoke = (UofsManualHydra) Pinecone.sys().getTaskManager().add(
                    new UofsManualHydra( args, Pinecone.sys() )
            );
            smoke.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
