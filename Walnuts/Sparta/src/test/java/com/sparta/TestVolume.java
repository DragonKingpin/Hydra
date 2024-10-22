package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.UniformVolumeTree;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.hydra.storage.volume.entity.MountPoint;
import com.pinecone.hydra.storage.volume.entity.SimpleVolume;
import com.pinecone.hydra.storage.volume.entity.VolumeAllotment;
import com.pinecone.hydra.storage.volume.entity.local.LocalSimpleVolume;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GUIDs;
import com.sauron.radium.Radium;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


class Alice extends Radium {
    public Alice( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Alice( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }
    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new VolumeMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        KOIMappingDriver koiFileMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

//        KOMFileSystem fileSystem = new UniformObjectFileSystem( koiFileMappingDriver );
//
//        UniformVolumeTree volumeTree = new UniformVolumeTree( koiMappingDriver );
//        VolumeAllotment volumeAllotment = volumeTree.getVolumeAllotment();
//        LocalSimpleVolume simpleVolume = volumeAllotment.newLocalSimpleVolume();
//        simpleVolume.setName("game");
//        MountPoint mountPoint = volumeAllotment.newMountPoint();
//        mountPoint.setMountPoint("UOFS://D:\\文件系统");
//        simpleVolume.setMountPoint( mountPoint );
//        //volumeTree.put( simpleVolume );
//        //Debug.trace( volumeTree.get(GUIDs.GUID72("03981de-00029f-0000-44") ).toString() );
//        //volumeTree.remove( GUIDs.GUID72("03981de-00029f-0000-44") );
//        //Debug.trace(volumeTree.getPath( GUIDs.GUID72("038b7ea-000078-0000-a8") ));
//        LogicVolume volume = volumeTree.get(GUIDs.GUID72("03981de-00029f-0000-44"));
//        SimpleVolume evinceSimpleVolume = volume.evinceSimpleVolume();
//        File sourceFile = new File("D:\\井盖视频块\\4月13日 (2).mp4");
//        Path path = sourceFile.toPath();
//        FileNode fileNode = fileSystem.getFSNodeAllotment().newFileNode();
//        fileNode.setName(sourceFile.getName());
//        fileNode.setGuid( fileSystem.getGuidAllocator().nextGUID72() );
//        evinceSimpleVolume.channelReceiver( fileSystem,fileNode, FileChannel.open(path, StandardOpenOption.READ));
    }


}
public class TestVolume {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Alice Alice = (Alice) Pinecone.sys().getTaskManager().add( new Alice( args, Pinecone.sys() ) );
            Alice.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
