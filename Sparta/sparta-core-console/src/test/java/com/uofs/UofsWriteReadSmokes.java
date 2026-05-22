package com.uofs;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;

import java.io.File;

enum UofsWriteReadSmoke implements UofsSmokeCase {
    DIRECT_OBJECT( "direct-object", "03" ) {
        @Override
        protected UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception {
            return new UofsVolumeFactory( context ).directObjectSimple( caseName, seed );
        }
    },

    BLOCK_SIMPLE_THIN( "block-simple-thin", "04" ) {
        @Override
        protected UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception {
            return new UofsVolumeFactory( context ).blockSimple( caseName, seed, VolumeAllocationMode.THIN );
        }
    },

    BLOCK_SIMPLE_THICK( "block-simple-thick", "05" ) {
        @Override
        protected UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception {
            return new UofsVolumeFactory( context ).blockSimple( caseName, seed, VolumeAllocationMode.THICK );
        }
    },

    BLOCK_SPANNED( "block-spanned", "06" ) {
        @Override
        protected UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception {
            return new UofsVolumeFactory( context ).blockSpanned( caseName, seed );
        }
    },

    BLOCK_STRIPED( "block-striped", "07" ) {
        @Override
        protected UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception {
            return new UofsVolumeFactory( context ).blockStriped( caseName, seed );
        }
    };

    private final String mCaseName;
    private final String mSeedSuffix;

    UofsWriteReadSmoke( String caseName, String seedSuffix ) {
        this.mCaseName = caseName;
        this.mSeedSuffix = seedSuffix;
    }

    @Override
    public void run( UofsSmokeContext context ) throws Exception {
        String seed = context.seed( this.mSeedSuffix );
        UofsVolumeFixture fixture = this.createFixture( context, this.mCaseName, seed );
        UofsSmokeTools.ensureBucket( context, this.mCaseName, fixture.volumeGuid, seed );
        KOMFileSystem fileSystem = context.hydra.createFileSystem( context.fileMappingDriver, fixture.volumeGuid, context.tempFolder );
        String uofsPath = "root@" + this.mCaseName + "/avatar.png";
        File readbackFile = new File( context.readbackRoot, this.mCaseName + "-avatar.png" );

        UofsSmokeTools.cleanupOldUofsFile( fileSystem, uofsPath );
        UofsSmokeTools.traceFixture( fixture );
        FileNode fileNode = UofsSmokeTools.writeFile( fileSystem, fixture, uofsPath, context.sourceFile );
        UofsSmokeTools.traceFatLayout( fileSystem, fileNode );
        long readBytes = UofsSmokeTools.readFile( fileSystem, fixture, uofsPath, readbackFile );
        UofsSmokeTools.verifySize( new File( context.sourceFile ).length(), readBytes, readbackFile );
        fixture.volumeManager.flush( fixture.volumeGuid );
        UofsSmokeTools.traceFixture( fixture );
    }

    protected abstract UofsVolumeFixture createFixture( UofsSmokeContext context, String caseName, String seed ) throws Exception;
}
