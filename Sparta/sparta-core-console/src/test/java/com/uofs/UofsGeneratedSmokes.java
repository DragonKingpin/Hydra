package com.uofs;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.volume.config.TitanVolumeDefaults;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

enum UofsGeneratedSmoke implements UofsSmokeCase {
    SMALL_FILE_BOUNDARY( "small-file-boundary.bin", UofsWriteReadSmoke.DIRECT_OBJECT, new long[] {
            0L,
            1L,
            4L * 1024L,
            UofsSmokePaths.MB,
            UofsSmokePaths.ALLOCATION_UNIT - 1L,
            UofsSmokePaths.ALLOCATION_UNIT,
            UofsSmokePaths.ALLOCATION_UNIT + 1L
    } ),

    THIN_WINDOW_BOUNDARY( "thin-window-boundary.bin", UofsWriteReadSmoke.BLOCK_SIMPLE_THIN, new long[] {
            UofsSmokePaths.ALLOCATION_UNIT - 1L,
            UofsSmokePaths.ALLOCATION_UNIT,
            UofsSmokePaths.ALLOCATION_UNIT + 1L
    } ),

    SPANNED_BOUNDARY( "spanned-boundary.bin", UofsWriteReadSmoke.BLOCK_SPANNED, new long[] {
            40L * UofsSmokePaths.MB - 1L,
            40L * UofsSmokePaths.MB,
            40L * UofsSmokePaths.MB + 1L
    } ),

    STRIPED_BOUNDARY( "striped-boundary.bin", UofsWriteReadSmoke.BLOCK_STRIPED, new long[] {
            TitanVolumeDefaults.DefaultStripeUnit - 1L,
            TitanVolumeDefaults.DefaultStripeUnit,
            TitanVolumeDefaults.DefaultStripeUnit + 1L,
            TitanVolumeDefaults.DefaultStripeUnit * 5L + 17L
    } ),

    MULTI_CHUNK( "multi-chunk.bin", UofsWriteReadSmoke.BLOCK_SIMPLE_THIN, new long[] {
            9L * UofsSmokePaths.MB
    } );

    private final String mGeneratedFileName;
    private final UofsSmokeCase mSmokeCase;
    private final long[] mSizes;

    UofsGeneratedSmoke( String generatedFileName, UofsSmokeCase smokeCase, long[] sizes ) {
        this.mGeneratedFileName = generatedFileName;
        this.mSmokeCase = smokeCase;
        this.mSizes = sizes;
    }

    @Override
    public void run( UofsSmokeContext context ) throws Exception {
        File sourceFile = new File( new File( context.root, "generated" ), this.mGeneratedFileName );
        for ( long size : this.mSizes ) {
            this.writePatternFile( sourceFile, size );
            Debug.trace( "==== generated source prepared ====", sourceFile.getPath(), "size", size );
            UofsSmokeContext generatedContext = new UofsSmokeContext(
                    context.hydra,
                    context.ibatisClient,
                    context.sqlSession,
                    context.fileMappingDriver,
                    context.volumeMappingDriver,
                    context.mappers,
                    sourceFile.getPath(),
                    context.root,
                    context.tempFolder,
                    context.readbackRoot,
                    context.seedPrefix
            );
            this.mSmokeCase.run( generatedContext );
        }
    }

    protected void writePatternFile( File file, long size ) throws Exception {
        File parent = file.getParentFile();
        if ( parent != null ) {
            parent.mkdirs();
        }
        byte[] buffer = new byte[64 * 1024];
        for ( int i = 0; i < buffer.length; ++i ) {
            buffer[i] = (byte)( i * 31 + 17 );
        }
        try ( OutputStream outputStream = Files.newOutputStream( file.toPath() ) ) {
            long remaining = size;
            while ( remaining > 0L ) {
                int step = (int)Math.min( buffer.length, remaining );
                outputStream.write( buffer, 0, step );
                remaining -= step;
            }
        }
    }
}
