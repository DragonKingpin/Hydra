package com.pinecone.hydra.file.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.file.ibatis.BucketMapper;
import com.pinecone.hydra.file.ibatis.ExternalSymbolicMapper;
import com.pinecone.hydra.file.ibatis.FileMapper;
import com.pinecone.hydra.file.ibatis.FolderMapper;
import com.pinecone.hydra.file.ibatis.FolderVolumeMappingMapper;
import com.pinecone.hydra.file.ibatis.SymbolicMapper;
import com.pinecone.hydra.file.ibatis.fat.FileChunkLocationMapper;
import com.pinecone.hydra.file.ibatis.fat.FileChunkMapper;
import com.pinecone.hydra.file.ibatis.journal.JournalItemMapper;
import com.pinecone.hydra.file.ibatis.journal.JournalMapper;

import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.bucket.source.BucketManipulator;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;
import com.pinecone.hydra.storage.file.fat.source.FileChunkManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.file.source.FolderVolumeMappingManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class FileMasterManipulatorImpl implements FileMasterManipulator {
    @Resource
    @Structure( type = FileMapper.class )
    FileManipulator fileManipulator;

    @Resource
    @Structure( type = FolderMapper.class )
    FolderManipulator folderManipulator;

    @Resource
    @Structure( type = FileChunkMapper.class )
    FileChunkManipulator fileChunkManipulator;

    @Resource
    @Structure( type = FileChunkLocationMapper.class )
    FileChunkLocationManipulator fileChunkLocationManipulator;

    @Resource
    @Structure( type = JournalMapper.class )
    JournalManipulator journalManipulator;

    @Resource
    @Structure( type = JournalItemMapper.class )
    JournalItemManipulator journalItemManipulator;

    @Resource
    @Structure( type = BucketMapper.class )
    BucketManipulator bucketManipulator;

    @Resource
    @Structure( type = SymbolicMapper.class )
    SymbolicManipulator symbolicManipulator;

    @Resource
    @Structure( type = ExternalSymbolicMapper.class )
    ExternalSymbolicManipulator externalSymbolicManipulator;

    @Resource( type = FileMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    @Structure( type = FolderVolumeMappingMapper.class)
    FolderVolumeMappingMapper folderVolumeRelationMapper;

    public FileMasterManipulatorImpl() {

    }

    public FileMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( FileMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new FileMasterTreeManipulatorImpl( driver );
    }

    @Override
    public FileManipulator getFileManipulator() {
        return this.fileManipulator;
    }

    @Override
    public FolderManipulator getFolderManipulator() {
        return this.folderManipulator;
    }

    @Override
    public FileChunkManipulator getFileChunkManipulator() {
        return this.fileChunkManipulator;
    }

    @Override
    public FileChunkLocationManipulator getFileChunkLocationManipulator() {
        return this.fileChunkLocationManipulator;
    }

    @Override
    public JournalManipulator getJournalManipulator() {
        return this.journalManipulator;
    }

    @Override
    public JournalItemManipulator getJournalItemManipulator() {
        return this.journalItemManipulator;
    }

    @Override
    public BucketManipulator getBucketManipulator() {
        return this.bucketManipulator;
    }

    @Override
    public SymbolicManipulator getSymbolicManipulator() {
        return this.symbolicManipulator;
    }

    @Override
    public ExternalSymbolicManipulator getExternalSymbolicManipulator() {
        return this.externalSymbolicManipulator;
    }

    @Override
    public FolderVolumeMappingManipulator getFolderVolumeRelationManipulator() {
        return this.folderVolumeRelationMapper;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }
}
