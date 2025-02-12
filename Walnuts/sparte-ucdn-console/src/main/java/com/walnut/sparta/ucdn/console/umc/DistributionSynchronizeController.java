package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.domain.ufm.SessionPhaser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.DistributionSynchronize." )
@Service
public class DistributionSynchronizeController {
    @Resource
    SessionPhaser sessionPhaser;

    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @AddressMapping("distributionCallBack")
    public void distributionCallBack( String path ){
        Debug.trace("回调");
        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        this.sessionPhaser.incrementDistributionSynchronize( elementNode.getGuid() );

        if( this.sessionPhaser.getDistributionSynchronize( elementNode.getGuid() ) == 1 ){
            Object lock = this.sessionPhaser.getDistributionLock(elementNode.getGuid());
            lock.notify();
            this.sessionPhaser.resetDistributionSynchronize( elementNode.getGuid() );
        }
    }
}
