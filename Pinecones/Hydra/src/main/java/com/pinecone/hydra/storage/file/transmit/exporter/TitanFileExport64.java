package com.pinecone.hydra.storage.file.transmit.exporter;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.TitanStorageExportIORequest;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.LocalFrame;
import com.pinecone.hydra.storage.file.transmit.UniformSourceLocator;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.UnifiedTransmitConstructor;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.ExporterEntity;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class TitanFileExport64 implements FileExport64{
    protected Chanface channel;

    protected FileNode                   fileNode;

    protected VolumeManager              volumeManager;

    protected UnifiedTransmitConstructor constructor;

    public TitanFileExport64( FileExportEntity64 entity ){
        this.channel = entity.getKChannel();
        this.fileNode = entity.getFile();
        this.volumeManager = entity.getVolumeManager();
        this.constructor = new UnifiedTransmitConstructor();
    }
    @Override
    public void export() throws IOException {
        // 获取文件所有的簇
        TreeMap<Long, Frame> framesMap = fileNode.getFrames();
        for (long i = 0; i < framesMap.size(); i++) {
            LocalFrame frame = (LocalFrame) framesMap.get(i);
            TitanStorageExportIORequest titanExportStorageObject = new TitanStorageExportIORequest();
            titanExportStorageObject.setSize( frame.getSize() );
            titanExportStorageObject.setStorageObjectGuid( frame.getSegGuid() );
            String sourceName = frame.getSourceName();
            UniformSourceLocator uniformSourceLocator = JSON.unmarshal(sourceName, UniformSourceLocator.class);
            LogicVolume volume = this.volumeManager.get(GUIDs.GUID72(uniformSourceLocator.getVolumeGuid()));
            //volume.channelExport( titanExportStorageObject, this.channel );
            ExporterEntity exportEntity = null;
            exportEntity = this.constructor.getExportEntity(volume.getClass(), volumeManager, titanExportStorageObject, this.channel, volume);
            volume.export( exportEntity );
        }

        this.channel.close();
    }

    @Override
    public void export(Frame frame) throws IOException {
        LocalFrame localFrame = (LocalFrame) frame;
        TitanStorageExportIORequest titanExportStorageObject = new TitanStorageExportIORequest();
        titanExportStorageObject.setSize( localFrame.getSize() );
        titanExportStorageObject.setStorageObjectGuid( localFrame.getSegGuid() );
        String sourceName = localFrame.getSourceName();
        UniformSourceLocator uniformSourceLocator = JSON.unmarshal(sourceName, UniformSourceLocator.class);
        LogicVolume volume = this.volumeManager.get(GUIDs.GUID72(uniformSourceLocator.getVolumeGuid()));
        ExporterEntity exportEntity = null;
        exportEntity = this.constructor.getExportEntity(volume.getClass(), volumeManager, titanExportStorageObject, this.channel, volume);
        volume.export( exportEntity );
    }

    @Override
    public void export(Number offset, Number endSize) throws  IOException {
        TreeMap<Long, Frame> framesMap = fileNode.getFrames();
        long startPosition = offset.longValue();
        long endPosition = offset.longValue() + endSize.longValue();
        long currentPosition = 0;

        for( long i = 0;i < framesMap.size(); i++ ){
            LocalFrame frame = (LocalFrame) framesMap.get(i);
            if (startPosition < currentPosition + frame.getDefinitionSize() && endPosition > currentPosition) {
                TitanStorageExportIORequest titanExportStorageObject = new TitanStorageExportIORequest();
                titanExportStorageObject.setSize(frame.getSize());
                titanExportStorageObject.setStorageObjectGuid(frame.getSegGuid());

                String sourceName = frame.getSourceName();
                UniformSourceLocator uniformSourceLocator = JSON.unmarshal(sourceName, UniformSourceLocator.class);
                LogicVolume volume = this.volumeManager.get(GUIDs.GUID72(uniformSourceLocator.getVolumeGuid()));

                ExporterEntity exportEntity = null;
                exportEntity = this.constructor.getExportEntity(volume.getClass(), volumeManager, titanExportStorageObject, this.channel, volume);

                long startOffsetInFrame = Math.max(startPosition - currentPosition, 0);
                long sizeToExport = Math.min(endPosition - currentPosition, frame.getDefinitionSize()) - startOffsetInFrame;

                volume.export(exportEntity, startOffsetInFrame, sizeToExport);
            }

            currentPosition += frame.getDefinitionSize();
            if (currentPosition >= endPosition){
                break;
            }
        }


    }
}
