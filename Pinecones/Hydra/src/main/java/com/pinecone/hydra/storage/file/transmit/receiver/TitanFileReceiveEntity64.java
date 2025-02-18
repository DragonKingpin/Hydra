package com.pinecone.hydra.storage.file.transmit.receiver;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class TitanFileReceiveEntity64 extends ArchFileReceiveEntity  implements FileReceiveEntity64{
    protected FileReceive fileReceive;


    public TitanFileReceiveEntity64(KOMFileSystem fileSystem, String destDirPath, FileNode file, Chanface channel, VolumeManager volumeManager) {
        super(fileSystem, destDirPath, file, channel, volumeManager);
        this.fileReceive = new TitanFileReceive64( this );
    }


    @Override
    public void receive() throws IOException {
        this.fileSystem.affirmFileNode( this.destDirPath );
        GUID volumeGuid = this.fileSystem.getMappingVolume(this.destDirPath);
        LogicVolume volume = this.volumeManager.get(volumeGuid);
        if ( !volume.checkCapacity( this.file.getDefinitionSize() ) ){
            this.fileSystem.remove( this.fileSystem.queryGUIDByPath( destDirPath ) );
            Debug.trace("容量不足");
            return;
        }
        volume.deductCapacity( this.file.getDefinitionSize() );

        this.fileReceive.receive( volume );
    }

    @Override
    public void receive( Number offset, Number endSize) throws IOException {
        this.fileSystem.affirmFileNode( this.destDirPath );
        GUID volumeGuid = this.fileSystem.getMappingVolume(this.destDirPath);
        LogicVolume volume = this.volumeManager.get(volumeGuid);
        if ( !volume.checkCapacity( this.file.getDefinitionSize() ) ){
            this.fileSystem.remove( this.fileSystem.queryGUIDByPath( destDirPath ) );
            Debug.trace("容量不足");
            return;
        }
        volume.deductCapacity( this.file.getDefinitionSize() );
        this.fileReceive.receive( volume, offset, endSize );
    }

    @Override
    public void receive(long segId) throws IOException {
        this.fileSystem.affirmFileNode( this.destDirPath );
        GUID volumeGuid = this.fileSystem.getMappingVolume(this.destDirPath);
        LogicVolume volume = this.volumeManager.get(volumeGuid);
        if ( !volume.checkCapacity( this.file.getDefinitionSize() ) ){
            this.fileSystem.remove( this.fileSystem.queryGUIDByPath( destDirPath ) );
            Debug.trace("容量不足");
            return;
        }
        volume.deductCapacity( this.file.getDefinitionSize() );
        this.fileReceive.receive( volume,segId );
    }

    @Override
    public void randomReceive(Number offset, Number endSize) throws  IOException {
        this.fileSystem.affirmFileNode( this.destDirPath );
        GUID volumeGuid = this.fileSystem.getMappingVolume(this.destDirPath);
        LogicVolume volume = this.volumeManager.get(volumeGuid);
        if ( !volume.checkCapacity( this.file.getDefinitionSize() ) ){
            this.fileSystem.remove( this.fileSystem.queryGUIDByPath( destDirPath ) );
            Debug.trace("容量不足");
            return;
        }
        volume.deductCapacity( this.file.getDefinitionSize() );
        this.fileReceive.randomReceive( volume, offset, endSize );
    }
}
