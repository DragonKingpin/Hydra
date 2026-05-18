package com.uofs;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

class CodexUofsSmokeHydra extends UofsManualHydra {
    private static final String AVATAR_ROOT = "E:\\MyFiles\\Picture\\Avatar";
    private static final String WORK_ROOT = "E:\\temp\\titan-uofs-codex-smoke";
    private static final String TEMP_FOLDER = WORK_ROOT + "\\temp";
    private static final String READBACK_ROOT = "E:\\titan-uofs-codex-readback";

    private static final String[] SOURCE_FILES = new String[] {
            AVATAR_ROOT + "\\Brickleberry1.png",
            AVATAR_ROOT + "\\Bullet.bmp",
            AVATAR_ROOT + "\\Megaraptor.png"
    };

    public CodexUofsSmokeHydra( String[] args, CascadeSystem parent ) {
        super( args, parent );
    }

    @Override
    public void vitalize() throws Exception {
        Debug.trace( "Titan UOFS Codex system smoke start" );

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
            UofsSmokeMappers mappers = this.bindMappers( volumeMappingDriver );
            this.cleanupKnownTestVolumes( mappers );
            this.cleanupLocalWorkspace();
            for ( int index = 0; index < SOURCE_FILES.length; index++ ) {
                this.runSourceFile( ibatisClient, sqlSession, fileMappingDriver, volumeMappingDriver, mappers, SOURCE_FILES[index], "7" + ( index + 1 ) );
            }
        }

        Debug.trace( "Titan UOFS Codex system smoke done" );
    }

    protected void runSourceFile(
            IbatisClient ibatisClient,
            SqlSession sqlSession,
            FileMappingDriver fileMappingDriver,
            VolumeMappingDriver volumeMappingDriver,
            UofsSmokeMappers mappers,
            String sourceFile,
            String seedPrefix
    ) throws Exception {
        File source = new File( sourceFile );
        if ( !source.exists() ) {
            throw new IllegalStateException( "Codex smoke source file not found: " + sourceFile );
        }
        String sourceSlug = this.slug( source.getName() );
        UofsSmokeContext context = new UofsSmokeContext(
                this,
                ibatisClient,
                sqlSession,
                fileMappingDriver,
                volumeMappingDriver,
                mappers,
                sourceFile,
                WORK_ROOT + "\\" + sourceSlug,
                TEMP_FOLDER + "\\" + sourceSlug,
                READBACK_ROOT + "\\" + sourceSlug,
                seedPrefix
        );
        context.ensureLocalWorkspace();

        Debug.trace( "==== UOFS source group start ====", source.getName(), "size", source.length() );
        UofsWriteReadSmoke.DIRECT_OBJECT.run( context );
        UofsWriteReadSmoke.BLOCK_SIMPLE_THIN.run( context );
        UofsWriteReadSmoke.BLOCK_SIMPLE_THICK.run( context );
        UofsWriteReadSmoke.BLOCK_SPANNED.run( context );
        UofsWriteReadSmoke.BLOCK_STRIPED.run( context );
        Debug.trace( "==== UOFS source group done ====", source.getName() );
    }

    protected void cleanupKnownTestVolumes( UofsSmokeMappers mappers ) {
        String[] prefixes = new String[] { "70", "71", "72", "73" };
        String[] cases = new String[] { "03", "04", "05", "06", "07" };
        String[] suffixes = new String[] { "101", "102", "201", "202", "301", "302", "401", "402", "901" };
        for ( String prefix : prefixes ) {
            for ( String caseSuffix : cases ) {
                String seed = prefix + caseSuffix;
                for ( String suffix : suffixes ) {
                    GUID guid = this.guid( seed, suffix );
                    mappers.extentMapper.removeByParentGuid( guid );
                    mappers.volumeMapper.remove( guid );
                    mappers.physicalMapper.remove( guid );
                }
            }
        }
    }

    protected GUID guid( String seed, String suffix ) {
        return GUIDs.GUID128( "01990000-0000-" + seed + "-8000-000000000" + suffix );
    }

    protected void cleanupLocalWorkspace() throws IOException {
        this.deleteDirectory( new File( WORK_ROOT ).toPath() );
        this.deleteDirectory( new File( READBACK_ROOT ).toPath() );
    }

    protected void deleteDirectory( Path path ) throws IOException {
        if ( !Files.exists( path ) ) {
            return;
        }
        try ( java.util.stream.Stream<Path> stream = Files.walk( path ) ) {
            stream.sorted( Comparator.reverseOrder() ).forEach( currentPath -> {
                try {
                    Files.deleteIfExists( currentPath );
                }
                catch ( IOException e ) {
                    throw new IllegalStateException( "Failed to delete Codex smoke path: " + currentPath, e );
                }
            } );
        }
    }

    protected String slug( String name ) {
        return name.replaceAll( "[^A-Za-z0-9._-]", "_" );
    }
}

public class CodexUofsSystemSmoke {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            CodexUofsSmokeHydra smoke = (CodexUofsSmokeHydra) Pinecone.sys().getTaskManager().add(
                    new CodexUofsSmokeHydra( args, Pinecone.sys() )
            );
            smoke.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
