package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/ucdn/version" )
@CrossOrigin
public class VersionController {
    @Resource
    private VersionManage primaryVersion;

    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @Resource
    private BucketInstrument bucketInstrument;


}
