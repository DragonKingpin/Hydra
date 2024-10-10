package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.file.GenericKOMFileSystem;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.creator.FileSystemCreator;
import com.pinecone.hydra.file.entity.GenericLocalFrame;
import com.pinecone.hydra.file.entity.LocalFrame;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GUID72;
import com.pinecone.ulf.util.id.GuidAllocator;
import com.sauron.radium.Radium;

import java.util.UUID;

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

        KOMFileSystem fileSystem = new GenericKOMFileSystem( koiMappingDriver );
        GuidAllocator guidAllocator = fileSystem.getGuidAllocator();
        this.testInsert( fileSystem );
    }

    private void testInsert( KOMFileSystem fileSystem ){
        fileSystem.affirmFolder("game/我的世界");
        fileSystem.affirmFileNode("game/我的世界/村民");
        fileSystem.affirmFileNode("game/我的世界/暮色森林/暮色惡魂");
    }

}
 public class TestFileSystem {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Steve Steve = (Steve) Pinecone.sys().getTaskManager().add( new Steve( args, Pinecone.sys() ) );
            Steve.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
