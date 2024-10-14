package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.hydra.file.UniformObjectFileSystem;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.file.transmit.ChannelReceiverEntity;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GuidAllocator;
import com.sauron.radium.Radium;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class Steve extends Radium {
    public Steve( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Steve( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }
    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        KOMFileSystem fileSystem = new UniformObjectFileSystem( koiMappingDriver );
        GuidAllocator guidAllocator = fileSystem.getGuidAllocator();
        //Debug.trace( fileSystem.get( GUIDs.GUID72( "020c8b0-000006-0002-54" ) ) );
        //this.testInsert( fileSystem );
        //this.testUpload(fileSystem);
        this.testDelete( fileSystem );
    }

    private void testInsert( KOMFileSystem fileSystem ){
        fileSystem.affirmFolder("game/我的世界");
        fileSystem.affirmFileNode("game/我的世界/村民");
        fileSystem.affirmFileNode("game/我的世界/暮色森林/暮色惡魂");
        fileSystem.affirmFileNode("game/泰拉瑞亚/腐化之地/世界吞噬者");
        fileSystem.affirmFileNode("movie/生还危机/浣熊市");
    }

    private void testUpload( KOMFileSystem fileSystem ) throws IOException {
        ChannelReceiverEntity channelReceiverEntity = fileSystem.getFSNodeAllotment().newChannelReceiverEntity();
        channelReceiverEntity.setDestDirPath("D:\\文件系统");
        File sourceFile = new File("D:\\井盖视频块\\4月13日 (2).mp4");
        Path path = sourceFile.toPath();
        FileNode fileNode = fileSystem.getFSNodeAllotment().newFileNode();
        fileNode.setName(sourceFile.getName());
        fileNode.setGuid( fileSystem.getGuidAllocator().nextGUID72() );
        channelReceiverEntity.setChannel(FileChannel.open(path, StandardOpenOption.READ));
        channelReceiverEntity.setFile( fileNode );
        channelReceiverEntity.receive();
    }

    private void testDelete(KOMFileSystem fileSystem ){
        fileSystem.remove( "game" );
        fileSystem.remove( "movie" );
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
