package com.walnut.sparta.ucdn.service.api.iface.v2;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.service.umct.FileSyncDistribution;
import com.walnut.sparta.ucdn.service.umct.FileSyncDistributionImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.service.umct.FileSyncDistribution." )
@Service
public class FileSyncDistributionController {

    @Resource
    private FileSyncDistribution fileSyncDistribution;

    @AddressMapping( "dino" )
    public void dino( String name ) {
        this.fileSyncDistribution.dino( name );
    }
}
