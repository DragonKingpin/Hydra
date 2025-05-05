package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.version.entity.TitanVersion;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.version.entity.VersionMapping;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.redstone.response.BasicResultResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/ucdn/version" )
@CrossOrigin
public class VersionController {
    @Resource
    private VersionManage versionManage;

    @PutMapping("/create/VersionMapping")
    public BasicResultResponse<String> createVersionMapping(@RequestParam("fileGuid") String fileGuid,
                                                            @RequestParam("enableVersionGuid") String enableVersionGuid
    ) {
        TitanVersion titanVersion = this.versionManage.queryByTargetStorageObjectGuid(GUIDs.GUID72(enableVersionGuid));
        TitanVersionMapping versionMapping = new TitanVersionMapping();
        versionMapping.setFileGuid(GUIDs.GUID72(fileGuid));
        versionMapping.setEnableVersionGuid(GUIDs.GUID72(enableVersionGuid));
        versionMapping.setVersionGuid((titanVersion.getVersionGuid()));
        if (!this.versionManage.isExistEnableVersionMapping(versionMapping.getEnableVersionGuid())){
            this.versionManage.insertVesionMapping(versionMapping);
        }
        else
            this.versionManage.UpdateVesionMapping(versionMapping);
        return BasicResultResponse.success();
    }
    @GetMapping("/query/VersionMapping")
    public String queryVersionMapping(@RequestParam("fileGuid") String fileGuid) {
        VersionMapping versionMapping = this.versionManage.queryVersionMapping(GUIDs.GUID72(fileGuid));
        TitanVersion version =new TitanVersion();
        if (versionMapping != null){
            version=this.versionManage.queryByTargetStorageObjectGuid(versionMapping.getEnableVersionGuid());
        }
        return BasicResultResponse.success(version).toJSONString();
    }
    @PutMapping("/update/VersionMapping")
    public BasicResultResponse<String> updateVersionMapping(
            @RequestParam("fileGuid") String fileGuid,
            @RequestParam("enableVersionGuid") String enableVersionGuid) {
        VersionMapping versionMapping = this.versionManage.queryVersionMapping(GUIDs.GUID72(fileGuid));
        versionMapping.setEnableVersionGuid(GUIDs.GUID72(enableVersionGuid));
        this.versionManage.UpdateVesionMapping(versionMapping);
        return BasicResultResponse.success();
    }


}
