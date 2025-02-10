package com.pinecone.hydra.storage.file.transmit.exporter;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface FileExportEntity extends Pinenut {
    KOMFileSystem getFileSystem();

    void setFileSystem( KOMFileSystem fileSystem );

    VolumeManager getVolumeManager();
    void setVolumeManager( VolumeManager volumeManager );

    FileNode getFile();

    void setFile( FileNode file );

    Chanface getKChannel();
    void setKChannel( Chanface channel );

    void export() throws IOException;

    void export(Frame frame) throws IOException;

    void export( Number offset, Number endSize );
}
