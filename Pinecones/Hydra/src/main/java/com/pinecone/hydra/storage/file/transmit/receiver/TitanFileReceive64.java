package com.pinecone.hydra.storage.file.transmit.receiver;

import com.pinecone.framework.util.Bytes;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.entity.RemoteCluster;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.StorageReceiveIORequest;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.TitanStorageReceiveIORequest;
import com.pinecone.hydra.storage.file.ClusterSegmentNaming;
import com.pinecone.hydra.storage.file.KOFSClusterSegmentNaming;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.Verification;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.UniformSourceLocator;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.volume.UnifiedTransmitConstructor;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.hydra.storage.volume.entity.ReceiveEntity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.zip.CRC32;

public class TitanFileReceive64 implements FileReceive64{
    protected KOMFileSystem                 mKOMFileSystem;

    protected ClusterSegmentNaming mClusterSegmentNaming;

    protected UnifiedTransmitConstructor    constructor;

    protected Chanface                      chanface;

    protected FileNode                      fileNode;

    protected String                        destDirPath;

    protected VolumeManager                 volumeManager;


    public TitanFileReceive64( FileReceiveEntity64 entity ){
        this.mKOMFileSystem      = entity.getFileSystem();
        this.mClusterSegmentNaming = new KOFSClusterSegmentNaming();
        this.constructor         = new UnifiedTransmitConstructor();
        this.chanface            = entity.getChannel();
        this.destDirPath         = entity.getDestDirPath();
        this.fileNode            = entity.getFile();
        this.volumeManager       = entity.getVolumeManager();
    }

    @Override
    public void receive( LogicVolume volume ) throws IOException {
        long frameSize = this.mKOMFileSystem.getConfig().getClusterSize().longValue();
        this.fileNode.setGuid( mKOMFileSystem.queryGUIDByPath( this.destDirPath ) );

        FSNodeAllotment allotment = mKOMFileSystem.getFSNodeAllotment();
        long segId = 0;
        long currentPosition = 0;
        long endSize = frameSize;

        long parityCheck = 0;
        long checkSum    = 0;
        long crc32       = 0;

        StorageIOResponse storageIOResponse = null;
        while ( true ) {
            if( currentPosition >= this.fileNode.getDefinitionSize() ){
                break;
            }

            if( currentPosition + endSize > this.fileNode.getDefinitionSize() ){
                endSize = this.fileNode.getDefinitionSize() - currentPosition;
            }

            LocalCluster localCluster = allotment.newLocalCluster();
            RemoteCluster remoteCluster = allotment.newRemoteCluster( this.fileNode.getGuid(),(int)segId );
            remoteCluster.setDeviceGuid(this.mKOMFileSystem.getConfig().getLocalhostGUID());
            remoteCluster.setSegGuid( localCluster.getSegGuid() );

            StorageReceiveIORequest storageReceiveIORequest = new TitanStorageReceiveIORequest();
            storageReceiveIORequest.setSize( this.fileNode.getDefinitionSize() );
            storageReceiveIORequest.setName( this.fileNode.getName() );
            storageReceiveIORequest.setStorageObjectGuid( localCluster.getSegGuid() );

            //storageIOResponse = volume.channelReceive(storageReceiveIORequest, kChannel, currentPosition, endSize);
            ReceiveEntity receiveEntity = null;
            receiveEntity = this.constructor.getReceiveEntity(volume.getClass(), this.volumeManager, storageReceiveIORequest, this.chanface, volume);
            storageIOResponse = volume.receive( receiveEntity, currentPosition, endSize );

            UniformSourceLocator uniformSourceLocator = new UniformSourceLocator();
            if( storageIOResponse != null ){
                localCluster.setCrc32( storageIOResponse.getCre32().getValue() );
                parityCheck += storageIOResponse.getParityCheck();
                checkSum += storageIOResponse.getChecksum();
                if( segId == 0 ){
                    crc32 = storageIOResponse.getCre32().getValue();
                }
                else {
                    crc32 = crc32 ^ storageIOResponse.getCre32().getValue();
                }
            }
            uniformSourceLocator.setVolumeGuid( volume.getGuid().toString() );
            localCluster.setSize( endSize );
            localCluster.setSourceName( uniformSourceLocator.toJSONString() );
            localCluster.setFileGuid( this.fileNode.getGuid() );
            localCluster.setSegId( segId );


            ++segId;
            localCluster.save();
            remoteCluster.save();
            currentPosition += endSize;
        }


        this.fileNode.setPhysicalSize( currentPosition );
        this.fileNode.setLogicSize   ( currentPosition );
        this.fileNode.setChecksum    ( checkSum );
        this.fileNode.setCrc32Xor    ( crc32 );
        this.fileNode.setParityCheck ( (int) parityCheck );
        this.mKOMFileSystem.update   ( this.fileNode );

//       Verification verification = this.getVerification();
//        fileNode.setChecksum( verification.getChecksum() );
//        fileNode.setParityCheck( verification.getParityCheck() );
//        fileNode.setCrc32Xor( Long.toHexString(verification.getCrc32().getValue()) );
//        mKOMFileSystem.update( fileNode );
    }

    @Override
    public void receive(LogicVolume volume, long segId) throws  IOException {
        long frameSize = this.mKOMFileSystem.getConfig().getClusterSize().longValue();
        FSNodeAllotment allotment = mKOMFileSystem.getFSNodeAllotment();
        //this.mKOMFileSystem.deleteCluster( this.fileNode, segId );
        LocalCluster localCluster = (LocalCluster)this.mKOMFileSystem.getClusterByFileWithId(this.fileNode.getGuid(), segId);
        long endSize = frameSize;

        long currentPosition = 0;
        if( currentPosition + endSize > localCluster.getSize() ){
            endSize = localCluster.getSize() - currentPosition;
        }

        Debug.trace( "更新簇的大小:"+endSize );
        RemoteCluster remoteCluster = allotment.newRemoteCluster( this.fileNode.getGuid(),(int)segId );
        remoteCluster.setDeviceGuid(this.mKOMFileSystem.getConfig().getLocalhostGUID());
        remoteCluster.setSegGuid( localCluster.getSegGuid() );

        StorageReceiveIORequest storageReceiveIORequest = new TitanStorageReceiveIORequest();
        storageReceiveIORequest.setSize( this.fileNode.getDefinitionSize() );
        storageReceiveIORequest.setName( this.fileNode.getName() );
        storageReceiveIORequest.setStorageObjectGuid( localCluster.getSegGuid() );

        StorageIOResponse storageIOResponse = null;

        ReceiveEntity receiveEntity = null;
        receiveEntity = this.constructor.getReceiveEntity(volume.getClass(), this.volumeManager, storageReceiveIORequest, this.chanface, volume);
        storageIOResponse = volume.receive( receiveEntity, currentPosition, endSize );

        UniformSourceLocator uniformSourceLocator = new UniformSourceLocator();
        if( storageIOResponse != null ){
            localCluster.setCrc32( storageIOResponse.getCre32().getValue() );
        }
        uniformSourceLocator.setVolumeGuid( volume.getGuid().toString() );
        localCluster.setSize( endSize );
        localCluster.setSourceName( uniformSourceLocator.toJSONString() );
        localCluster.setFileGuid( this.fileNode.getGuid() );
        localCluster.setSegId( segId );

        localCluster.save();
        remoteCluster.save();
    }

    @Override
    public void receive(LogicVolume volume, Number offset, Number endSize) throws IOException {

    }

    @Override
    public void randomReceive(LogicVolume volume, Number offset, Number endSize) throws  IOException {
        long frameSize = this.mKOMFileSystem.getConfig().getClusterSize().longValue();
        this.fileNode.setGuid( mKOMFileSystem.queryGUIDByPath( this.destDirPath ) );

        FSNodeAllotment allotment = mKOMFileSystem.getFSNodeAllotment();
        long segId = offset.longValue() / frameSize + 1;
        long startPosition = offset.longValue();
        long endPosition = startPosition + endSize.longValue();
        long frameTerminatePosition = segId * frameSize;
        LocalCluster frame = (LocalCluster) this.mKOMFileSystem.getClusterByFileWithId(this.fileNode.getGuid(), segId);

        if( frame == null ){
            frame = allotment.newLocalCluster();
            RemoteCluster remoteCluster = allotment.newRemoteCluster( this.fileNode.getGuid(),(int)segId );
            remoteCluster.setDeviceGuid(this.mKOMFileSystem.getConfig().getLocalhostGUID());
            remoteCluster.setSegGuid( frame.getSegGuid() );
            remoteCluster.save();
        }

        if( endPosition <= frameTerminatePosition + frameSize ){
            StorageReceiveIORequest storageReceiveIORequest = new TitanStorageReceiveIORequest();
            storageReceiveIORequest.setSize( this.fileNode.getDefinitionSize() );
            storageReceiveIORequest.setName( this.fileNode.getName() );
            storageReceiveIORequest.setStorageObjectGuid( frame.getSegGuid() );

            ReceiveEntity receiveEntity = null;
            receiveEntity = this.constructor.getReceiveEntity(volume.getClass(), this.volumeManager, storageReceiveIORequest, this.chanface, volume);
            volume.randomReceive( receiveEntity, startPosition, endSize );

            UniformSourceLocator uniformSourceLocator = new UniformSourceLocator();
            uniformSourceLocator.setVolumeGuid( volume.getGuid().toString() );
            frame.setSize( frame.getSize() + endSize.longValue() );
            frame.setSourceName( uniformSourceLocator.toJSONString() );
            frame.setFileGuid( this.fileNode.getGuid() );
            frame.setSegId( segId );

            frame.save();
        }
        else {
            long midPosition = Math.min(frameTerminatePosition + frameSize, endPosition);
            this.randomReceive(volume, startPosition, midPosition - startPosition);
            if (midPosition < endPosition) {
                this.randomReceive(volume, midPosition, endPosition - midPosition);
            }
        }

    }

    Verification getVerification() throws IOException {
        File tempFile = File.createTempFile("temp",".temp");
        FileNode fileNode = (FileNode)this.mKOMFileSystem.get(this.mKOMFileSystem.queryGUIDByPath(this.destDirPath));
        FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        TitanFileChannelChanface kChannel = new TitanFileChannelChanface(channel);
        TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64(this.mKOMFileSystem, this.volumeManager, fileNode, kChannel);
        this.mKOMFileSystem.export( exportEntity );

        return getVerification(tempFile);
    }

    private Verification getVerification(File tempFile) throws IOException {
        Verification verification = new Verification();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile))) {
            CRC32 crc = new CRC32();
            long checksum = 0;
            int parityCheck = 0;

            // 使用一个缓冲区一次读取多个字节
            byte[] buffer = new byte[8192]; // 8KB 缓冲区
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte b = buffer[i];

                    // 批量处理每个字节
                    parityCheck += Bytes.calculateParity(b);
                    checksum += b & 0xFF;
                    crc.update(b);
                }
            }

            verification.setChecksum(checksum);
            verification.setCrc32(crc);
            verification.setParityCheck(parityCheck);
        }
        return verification;
    }
}
