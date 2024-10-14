package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.source.FileManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class GenericFileNode extends ArchElementNode implements FileNode{
    private LocalDateTime               deletedTime;
    private long                        checksum;
    private int                         parityCheck;
    private FileMeta                    fileMeta;

    private KOMFileSystem               fileSystem;
    private FileManipulator             fileManipulator;
    private TreeMap<Long, Frame>        frames = new TreeMap<>();
    private boolean                     isUploadSuccessful;
    private long                        physicalSize;
    private long                        logicSize;
    private long                        definitionSize;
    private String                     crc32Xor;
    private boolean                     integrityCheckEnable;
    private boolean                     disableCluster;

    @Override
    public boolean getIsUploadSuccessful() {
        return this.isUploadSuccessful;
    }

    @Override
    public void setIsUploadSuccessful(boolean isUploadSuccessful) {
        this.isUploadSuccessful = isUploadSuccessful;
    }


    @Override
    public TreeMap<Long, Frame> getFrames() {
        return this.frames;
    }




    public GenericFileNode() {
    }

    public GenericFileNode(KOMFileSystem fileSystem ) {
        this.fileSystem = fileSystem;

        this.frames = fileSystem.getFrameByFileGuid(this.guid);
        GuidAllocator guidAllocator = this.fileSystem.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
        this.setCreateTime( LocalDateTime.now() );
    }
    public GenericFileNode( KOMFileSystem fileSystem, FileManipulator fileManipulator ) {
        this(fileSystem);
        this.fileManipulator = fileManipulator;
    }


    public void apply( KOMFileSystem fileSystem ) {
        this.fileSystem = fileSystem;
    }

    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    public String getName() {
        return name;
    }

    @Override
    public FileSystemAttributes getAttributes() {
        return this.fileSystemAttributes;
    }

    @Override
    public KOMFileSystem parentFileSystem() {
        return fileSystem;
    }


    public void setName(String name) {
        this.name = name;
    }


    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }


    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }


    public long getChecksum() {
        return checksum;
    }


    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }


    public int getParityCheck() {
        return parityCheck;
    }


    public void setParityCheck(int parityCheck) {
        this.parityCheck = parityCheck;
    }


    @Override
    public void copyValueTo(GUID destinationGuid) {

    }

    @Override
    public void copyTo(GUID destinationGuid) {

    }


    public FileMeta getFileMeta() {
        return fileMeta;
    }


    public void setFileMeta(FileMeta fileMeta) {
        this.fileMeta = fileMeta;
    }

    @Override
    public GUID getDataAffinityGuid() {
        return null;
    }


    public FileSystemAttributes getAttribute() {
        return fileSystemAttributes;
    }


    public void setAttribute(FileSystemAttributes fileSystemAttributes) {
        this.fileSystemAttributes = fileSystemAttributes;
    }

    @Override
    public void removeFrame() {
        if ( this.frames == null || this.frames.isEmpty() ){
            this.frames = this.fileSystem.getFrameByFileGuid( this.guid );
        }
        for (Frame frame : this.frames.values()){
            frame.remove();
        }
    }

    @Override
    public long getPhysicalSize() {
        return this.physicalSize;
    }

    @Override
    public void setPhysicalSize(long physicalSize) {
        this.physicalSize = physicalSize;
    }

    @Override
    public long getLogicSize() {
        return this.logicSize;
    }

    @Override
    public void setLogicSize(long logicSize) {
        this.logicSize = logicSize;
    }

    @Override
    public long getDefinitionSize() {
        return this.definitionSize;
    }

    @Override
    public void setDefinitionSize(long definitionSize) {
        this.definitionSize = definitionSize;
    }

    @Override
    public String getCrc32Xor() {
        return this.crc32Xor;
    }

    @Override
    public void setCrc32Xor(String crc32Xor) {
        this.crc32Xor = crc32Xor;
    }

    @Override
    public boolean getIntegrityCheckEnable() {
        return this.integrityCheckEnable;
    }

    @Override
    public void setIntegrityCheckEnable(boolean integrityCheckEnable) {
        this.integrityCheckEnable = integrityCheckEnable;
    }

    @Override
    public boolean getDisableCluster() {
        return this.disableCluster;
    }

    @Override
    public boolean isUploadSuccess() {
        if ( this.physicalSize == this.definitionSize ){
            return true;
        }
        return false;
    }

    @Override
    public void setDisableCluster(boolean disableCluster) {
        this.disableCluster = disableCluster;
    }

    public void fragmentation(long size ){
//        long segNum = (this.size + size - 1) / size;
//        for( int i = 0 ; i<segNum ;i++ ){
//            GuidAllocator guidAllocator = this.fileSystem.getGuidAllocator();
//            LocalFrame localFrame = this.fileSystem.getFileSystemCreator().dummyLocalFrame();
//            GUID segGuid = guidAllocator.nextGUID72();
//            localFrame.setSegGuid(segGuid);
//            localFrame.setFileGuid(this.guid);
//            localFrame.setSegId(i);
//
//            long segmentSize = (i == segNum - 1) ? this.size % size : size;
//            if (segmentSize == 0) {
//                segmentSize = size;
//            }
//            localFrame.setSize(segmentSize);
//            localFrame.setSourceName(this.name+i);
//            localFrame.save();
//        }
    }

    public String toString() {
        return "GenericFileNode{enumId = " + enumId + ", guid = " + guid + ", createTime = " + createTime + ", updateTime = " + updateTime + ", name = " + name + ", deletedTime = " + deletedTime + ", checksum = " + checksum + ", parityCheck = " + parityCheck + ", "  + ", fileMeta = " + fileMeta + ", attribute = " + fileSystemAttributes + "}";
    }
}
