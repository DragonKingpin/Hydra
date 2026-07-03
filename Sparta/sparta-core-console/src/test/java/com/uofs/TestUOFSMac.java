package com.uofs;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import org.apache.ibatis.session.SqlSession;

import java.io.File;

class UofsMacHydra extends UofsManualHydra {
    private static final String SOURCE_FILE = "/Users/wujunhong/Downloads/meow.png";
    private static final String ROOT = "/Users/wujunhong/tmp/titan-uofs-mac";
    private static final String TEMP_FOLDER = ROOT + "/temp";
    private static final String READBACK_ROOT = ROOT + "/readback";
    private static final String SEED_PREFIX = "80";

    public UofsMacHydra( String[] args, CascadeSystem parent ) {
        super( args, parent );
    }

    @Override
    public void vitalize() throws Exception {
        Debug.trace( "Titan UOFS Mac smoke start" );
        this.ensureMacWorkspace();

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
                    this.bindMappers( volumeMappingDriver ),
                    SOURCE_FILE,
                    ROOT,
                    TEMP_FOLDER,
                    READBACK_ROOT,
                    SEED_PREFIX
            );

            UofsWriteReadSmoke.DIRECT_OBJECT.run( context );
            UofsWriteReadSmoke.BLOCK_SIMPLE_THIN.run( context );
        }

        Debug.trace( "Titan UOFS Mac smoke done" );
    }

    private void ensureMacWorkspace() {
        File sourceFile = new File( SOURCE_FILE );
        if ( !sourceFile.exists() ) {
            throw new IllegalStateException( "Mac UOFS smoke source file not found: " + SOURCE_FILE );
        }
        new File( ROOT ).mkdirs();
        new File( TEMP_FOLDER ).mkdirs();
        new File( READBACK_ROOT ).mkdirs();
    }
}

public class TestUOFSMac {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            UofsMacHydra smoke = (UofsMacHydra) Pinecone.sys().getTaskManager().add(
                    new UofsMacHydra( args, Pinecone.sys() )
            );
            smoke.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
