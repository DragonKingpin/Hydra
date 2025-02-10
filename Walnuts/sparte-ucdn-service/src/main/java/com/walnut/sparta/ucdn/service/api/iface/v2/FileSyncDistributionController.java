package com.walnut.sparta.ucdn.service.api.iface.v2;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.service.umct.FileSyncDistribution;
import com.walnut.sparta.ucdn.service.umct.FileSyncDistributionImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.service.umct.FileSyncDistribution." )
@Service
public class FileSyncDistributionController {

    @Resource
    private FileSyncDistribution fileSyncDistribution;

    @Resource
    private KOMFileSystem primaryFileSystem;

    @AddressMapping( "dino" )
    public void dino( String name ) {

    }

    @AddressMapping(" fileDistribution ")
    public void fileDistribution( String path, String topic, String server, long startSegId, long endSegId ) throws IOException {
        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        if( elementNode instanceof FileNode){
            this.fileSyncDistribution.fileDistribution( (FileNode) elementNode, topic, server, startSegId, endSegId );
        }

    }
}
