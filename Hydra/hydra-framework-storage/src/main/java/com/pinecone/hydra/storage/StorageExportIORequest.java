package com.pinecone.hydra.storage;

public interface StorageExportIORequest extends StorageInstructRequest {
    String getSourceName(); // 具体存在磁盘的为 / I/O寻址  Source => Address
    void setSourceName( String sourceName );

    long getCrc32();
    void setCrc32( long crc32 );

    Number getSize(); // 欲存储的声明大小
    void setSize( Number size );
}
