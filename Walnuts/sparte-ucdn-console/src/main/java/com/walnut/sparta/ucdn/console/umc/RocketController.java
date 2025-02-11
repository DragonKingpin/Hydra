package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.domain.CentralControlUnit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.RocketOperation." )
@Service
public class RocketController {
    @Resource
    CentralControlUnit centralControlUnit;

    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @AddressMapping("taskComplete")
    public void taskComplete( String path ){

    }
}
