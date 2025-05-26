package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KernelFileSystemConfig;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.file.external.GenericNativeExternalFolder;
import com.pinecone.hydra.storage.file.external.KenExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.builder.ComponentUOFSBuilder;
import com.pinecone.hydra.storage.file.builder.UOFSBuilder;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V1;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V2;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V3;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V4;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V5;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V6;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;
import com.pinecone.ulf.util.guid.i128.GUID128;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

class Steve extends Tritium {
    public Steve( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Steve( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        KOIMappingDriver koiVolumeMappingDriver = new VolumeMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );


        JSONObject jo = new JSONMaptron( "{ DefaultVolumeGuid:'1788a74-000136-0000-f8', DefaultTempFilePath: 'D:/文件系统/temp/' }" );
        FileSystemConfig config = new KernelFileSystemConfig( jo );
        VolumeConfig volumeConfig = new KernelVolumeConfig( jo );

        UOFSBuilder builder = new ComponentUOFSBuilder( koiMappingDriver, config );
        KOMFileSystem fileSystem = new UniformObjectFileSystem( koiMappingDriver, config );
//        FileSystemCacheConfig cacheConfig = new MappedFileSystemCacheConfig(new JSONMaptron("{redisHost: \"47.115.216.203\",redisPort: 6379, redisTimeOut: 2000, redisPassword: 1234abcd, redisDatabase: 0}"));
//        KOMFileSystem fileSystem = builder.registerComponentor( new UOFSCacheComponentor(cacheConfig) ).buildByRegistered();
        UniformVolumeManager volumeManager = new UniformVolumeManager(koiVolumeMappingDriver, volumeConfig);
        GuidAllocator guidAllocator = fileSystem.getGuidAllocator();
        //Debug.trace( fileSystem.get( GUIDs.GUID72( "020c8b0-000006-0002-54" ) ) );
        //this.testInsert( fileSystem );
        //this.testUpload(fileSystem);
        //this.testDelete( fileSystem );
        //this.testChannelReceive( fileSystem, volumeManager );
        //this.testChannelExport( fileSystem, volumeManager );
        //this.testQuery( fileSystem );
        //this.testExternal( fileSystem );
        //this.testCopy( fileSystem,volumeManager );


        //this.testClusterPage( fileSystem );
        GuidAllocator128 guidAllocator128 = new GuidAllocator128V1();
        Debug.trace("Guid128V1：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V2();
        Debug.trace("Guid128V2：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V3();
        Debug.trace("Guid128V3：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V4();
        Debug.trace("Guid128V4：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V5();
        Debug.trace("Guid128V5：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V6();
        Debug.trace("Guid128V6：" + guidAllocator128.nextGUID() );
        guidAllocator128 = new GuidAllocator128V7();
        GUID128 g = (GUID128) guidAllocator128.nextGUID();
        Debug.trace("Guid128V7：" + g, g.toUUID() );
        Debug.trace( guidAllocator128.parse("019706a0-3f60-708b-b925-03e89e7ad584") );
    }

    private void testQuery ( KOMFileSystem fileSystem ){
        Debug.trace( fileSystem.queryGUIDByPath("我的文件/总2127.mp4") );
    }

    private void testInsert( KOMFileSystem fileSystem ){
        fileSystem.affirmFolder("game/我的世界");
        fileSystem.affirmFileNode("game/我的世界/村民");
        fileSystem.affirmFileNode("game/我的世界/暮色森林/暮色惡魂");
        fileSystem.affirmFileNode("game/泰拉瑞亚/腐化之地/世界吞噬者");
        fileSystem.affirmFileNode("movie/生还危机/浣熊市");
    }

    private void testCopy(KOMFileSystem fileSystem, VolumeManager volumeManager) {
//        fileSystem.copy("我的文件/图片","我的文件/我的文件",volumeManager);
        FileNode fileNode = fileSystem.getFileNode(GUIDs.GUID72("14bc124-00012c-0004-f8"));
        Debug.trace( fileNode.getPath() );
    }

    private void testExternal(KOMFileSystem fileSystem){
        KenExternalFileSystemInstrument directFileSystemAccess = new KenExternalFileSystemInstrument(fileSystem);
//        GenericExternalSymbolic externalSymbolic = new GenericExternalSymbolic();
//        externalSymbolic.setName("xxx");
//        externalSymbolic.setGuid( fileSystem.getGuidAllocator().nextGUID() );
//        directFileSystemAccess.insertExternalSymbolic( externalSymbolic );

        ElementNode e = fileSystem.queryElement( "red" );
        //e.evinceFolder().createExternalSymbolic( "external" );



//        ExternalFile externalFile = (GenericExternalFile)directFileSystemAccess.queryElement("我的文件/external/《智育》概要设计.docx");
//        Debug.trace(externalFile.getPath());
        GenericNativeExternalFolder externalFolder = new GenericNativeExternalFolder(new File("D:/文件"));
        Debug.trace(externalFolder.getName());
        Debug.trace(externalFolder.toJSONString());
    }


    private void testDelete(KOMFileSystem fileSystem ){
        fileSystem.remove( "game" );
        fileSystem.remove( "movie" );
    }

    private void testChannelReceive( KOMFileSystem fileSystem, UniformVolumeManager volumeManager ) throws IOException {
        //LogicVolume volume = volumeManager.get(GUIDs.GUID72( "09d62c0-00037e-0006-c8" ));
        FSNodeAllotment fsNodeAllotment = fileSystem.getFSNodeAllotment();
        File file = new File("D:/井盖视频块/我的视频.mp4");
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );
        FileNode fileNode = fsNodeAllotment.newFileNode();
        fileNode.setDefinitionSize( file.length() );
        fileNode.setName( file.getName() );
        String destDirPath = "D:/井盖视频块/我的视频.mp4";
        TitanFileReceiveEntity64 receiveEntity = new TitanFileReceiveEntity64( fileSystem, destDirPath, fileNode,titanFileChannelKChannel,volumeManager );
        fileSystem.receive( receiveEntity );
    }

    private void testChannelExport( KOMFileSystem fileSystem, UniformVolumeManager volumeManager ) throws IOException {
        FileNode fileNode = (FileNode) fileSystem.get(fileSystem.queryGUIDByPath("D:/井盖视频块/我的视频.mp4"));
        File file = new File("D:\\文件系统\\大文件\\我的视频.mp4");
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        TitanFileChannelChanface kChannel = new TitanFileChannelChanface( channel );
        TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64( fileSystem, volumeManager, fileNode, kChannel );
        fileSystem.export( exportEntity );
    }

    private void testClusterPage( KOMFileSystem fileSystem ){
        ClusterPage clusterPage = fileSystem.fetchClustersByFileGuid( GUIDs.GUID72( "1632d6e-0001de-0003-e4" ) );
        long sum = clusterPage.getClusters();

        for ( long i = 0; i < sum; ++i ) {
            Debug.trace( clusterPage.getLocalCluster( i ) );
        }

    }

}
public class TestUOFS {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Steve Steve = (Steve) Pinecone.sys().getTaskManager().add( new Steve( args, Pinecone.sys() ) );
            Steve.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
