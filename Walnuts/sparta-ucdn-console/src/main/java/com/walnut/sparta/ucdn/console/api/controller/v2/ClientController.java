package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.walnut.sparta.ucdn.console.api.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.domain.service.UCDNService;
import com.walnut.sparta.ucdn.console.domain.service.impl.UCDNServiceImpl;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.umc.UMCMasterWarehouse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping( "/api/v2/ucdn/transmit" )
public class ClientController {
    @Resource
    protected UCDNService       service;
    /**
     *
     * @param filePath 文件要上传的路径
     * @param file 文件本体
     * @return
     */
    @PostMapping("/upload")
    public BasicResultResponse<String> upload(@RequestParam("filePath") String filePath, @RequestParam("file") MultipartFile file,@RequestParam("topic") String topic ) throws IOException, InterruptedException {
        File tempFile = File.createTempFile("upload",".temp");
        file.transferTo(tempFile);

        this.service.upload( filePath,tempFile,topic );
        return BasicResultResponse.success();
    }

    @GetMapping("/test")
    public void test() throws UMBServiceException {
        this.service.test();
    }

    @GetMapping("/testDistribution")
    public void testDistribution( @RequestParam("path") String path, @RequestParam("topic") String topic ) throws IOException, InterruptedException {
        this.service.testDistribution( path,topic );
    }

    @GetMapping("/testEDistribution")
    public void testEDistribution( @RequestParam("path") String path, @RequestParam("topic") String topic ) throws IOException, InterruptedException {
        this.service.testEDdistribution( path,topic );
    }
}
