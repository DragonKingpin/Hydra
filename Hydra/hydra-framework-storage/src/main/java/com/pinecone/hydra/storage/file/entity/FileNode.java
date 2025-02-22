package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.CheckedFile;

import java.time.LocalDateTime;
import java.util.TreeMap;

public interface FileNode extends ElementNode, CheckedFile {
    LocalDateTime getDeletedTime();
    void setDeletedTime(LocalDateTime deletedTime);

    long getChecksum();
    void setChecksum(long checksum);

    int getParityCheck();
    void setParityCheck(int parityCheck);


    void copyValueTo(GUID destinationGuid );
    void copyTo    (GUID destinationGuid);

    FileMeta getFileMeta();
    void startDistribution(FileMeta fileMeta);
    GUID getDataAffinityGuid();

    boolean getIsUploadSuccessful();
    void setIsUploadSuccessful( boolean isUploadSuccessful );
    TreeMap<Long, Cluster> getClusters();

    @Override
    default FileNode evinceFileNode() {
        return this;
    }

    void removeCluster();

    long getPhysicalSize();
    void setPhysicalSize(long physicalSize);

    long getLogicSize();
    void setLogicSize(long logicSize);

    long getDefinitionSize();
    void setDefinitionSize(long definitionSize);

    long getCrc32Xor();

    void setCrc32Xor( long crc32Xor );

    boolean getIntegrityCheckEnable();
    void setIntegrityCheckEnable(boolean integrityCheckEnable);

    boolean getDisableCluster();

    void setDisableCluster(boolean disableCluster);

    boolean isUploadSuccess();

    String getPath();

    void setPath( String path );
}
