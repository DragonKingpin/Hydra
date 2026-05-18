package com.uofs;

import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import org.apache.ibatis.session.SqlSession;

import java.io.File;

final class UofsSmokeContext {
    final UofsManualHydra hydra;
    final IbatisClient ibatisClient;
    final SqlSession sqlSession;
    final FileMappingDriver fileMappingDriver;
    final VolumeMappingDriver volumeMappingDriver;
    final UofsSmokeMappers mappers;
    final String sourceFile;
    final String root;
    final String tempFolder;
    final String readbackRoot;
    final String seedPrefix;

    UofsSmokeContext(
            UofsManualHydra hydra,
            IbatisClient ibatisClient,
            SqlSession sqlSession,
            FileMappingDriver fileMappingDriver,
            VolumeMappingDriver volumeMappingDriver,
            UofsSmokeMappers mappers
    ) {
        this(
                hydra,
                ibatisClient,
                sqlSession,
                fileMappingDriver,
                volumeMappingDriver,
                mappers,
                UofsSmokePaths.SOURCE_FILE,
                UofsSmokePaths.ROOT,
                UofsSmokePaths.TEMP_FOLDER,
                UofsSmokePaths.READBACK_ROOT,
                "70"
        );
    }

    UofsSmokeContext(
            UofsManualHydra hydra,
            IbatisClient ibatisClient,
            SqlSession sqlSession,
            FileMappingDriver fileMappingDriver,
            VolumeMappingDriver volumeMappingDriver,
            UofsSmokeMappers mappers,
            String sourceFile,
            String root,
            String tempFolder,
            String readbackRoot
    ) {
        this(
                hydra,
                ibatisClient,
                sqlSession,
                fileMappingDriver,
                volumeMappingDriver,
                mappers,
                sourceFile,
                root,
                tempFolder,
                readbackRoot,
                "70"
        );
    }

    UofsSmokeContext(
            UofsManualHydra hydra,
            IbatisClient ibatisClient,
            SqlSession sqlSession,
            FileMappingDriver fileMappingDriver,
            VolumeMappingDriver volumeMappingDriver,
            UofsSmokeMappers mappers,
            String sourceFile,
            String root,
            String tempFolder,
            String readbackRoot,
            String seedPrefix
    ) {
        this.hydra = hydra;
        this.ibatisClient = ibatisClient;
        this.sqlSession = sqlSession;
        this.fileMappingDriver = fileMappingDriver;
        this.volumeMappingDriver = volumeMappingDriver;
        this.mappers = mappers;
        this.sourceFile = sourceFile;
        this.root = root;
        this.tempFolder = tempFolder;
        this.readbackRoot = readbackRoot;
        this.seedPrefix = seedPrefix;
    }

    void ensureLocalWorkspace() {
        new File( this.root ).mkdirs();
        new File( this.tempFolder ).mkdirs();
        new File( this.readbackRoot ).mkdirs();
    }

    String seed( String caseSuffix ) {
        return this.seedPrefix + caseSuffix;
    }
}
