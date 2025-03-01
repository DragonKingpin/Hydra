package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.hydra.umb.UMBServiceException;
import com.walnut.redstone.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.domain.service.NodeFileDistributionService;

import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping( "/api/v2/ucdn/client" )
public class ClientController {
    @Resource
    protected NodeFileDistributionService service;
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

    @GetMapping("/testDistribution")
    public void testDistribution( @RequestParam("path") String path, @RequestParam("topic") String topic ) throws IOException, InterruptedException {
        this.service.testDistribution( path,topic );
    }

}
