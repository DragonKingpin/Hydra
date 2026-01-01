package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.walnut.archcraft.redstone.response.GenericResultResponse;
import com.walnut.sparta.ucdn.console.domain.service.NodeFileDistributionService;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping( "/api/v2/ucdn/client" )
public class ClientController {
    @Resource
    protected NodeFileDistributionService service;

    @Value("${service.LocalUploadTemporaryWorkingDirectory}")
    private String majorTemporaryClusterFileDirectory;

    @Value("${service.TemporaryFileExtends}")
    private String temporaryFileExtends;
    /**
     *
     * @param filePath 文件要上传的路径
     * @param file 文件本体
     * @return
     */
    @PostMapping("/upload")
    public GenericResultResponse<String> upload(@RequestParam("filePath") String filePath, @RequestParam("file") MultipartFile file, @RequestParam("topic") String topic ) throws IOException, InterruptedException {
        File tempFile = new File(majorTemporaryClusterFileDirectory+ UUID.randomUUID()+temporaryFileExtends);
        if( !tempFile.createNewFile() ){
            throw new IOException( "Creating file compromised, what :" + tempFile.toPath() );
        }
        file.transferTo(tempFile);

        this.service.upload( filePath,tempFile,topic );
        if( !tempFile.delete() ){
            throw new IOException( "Purging temporary file compromised, what :" + tempFile.toPath() );
        }
        return GenericResultResponse.success();
    }

    @GetMapping("/testDistribution")
    public void testDistribution( @RequestParam("path") String path, @RequestParam("topic") String topic ) throws IOException, InterruptedException {
        this.service.testDistribution( path,topic );
    }

}
