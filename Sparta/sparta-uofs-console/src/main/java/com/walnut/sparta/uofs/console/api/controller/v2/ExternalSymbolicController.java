package com.walnut.sparta.uofs.console.api.controller.v2;


import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.direct.ExternalFile;
import com.pinecone.hydra.storage.file.direct.ExternalFolder;
import com.pinecone.hydra.storage.file.direct.GenericExternalFolder;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.walnut.sparta.uofs.console.api.response.BasicResultResponse;
import com.walnut.sparta.uofs.console.domain.dto.CreateExternalDTO;
import com.walnut.sparta.uofs.console.domain.dto.CreateExternalSymbolicDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping( "/api/v2/uofs/externalSymbolic" )
@CrossOrigin
public class ExternalSymbolicController {
    @Resource
    private KOMFileSystem primaryFileSystem;

    /**
     * 获取外部目录所有内容
     * @param path 路径
     * @return 返回内容
     */
    @GetMapping("/listItem")
    public String listItem(@RequestParam("path") String path){
        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        if(elementNode != null){
            ExternalSymbolic externalSymbolic = (ExternalSymbolic) elementNode;
            GenericExternalFolder externalFolder = new GenericExternalFolder(new File(externalSymbolic.getReparsedPoint()));
            return BasicResultResponse.success(externalFolder.listItem()).toJSONString();
        }
        return BasicResultResponse.success().toJSONString();
    }

    /**
     * 获取外部文件夹的所有内容
     * @param path 路径
     * @return 返回内容信息
     */
    @GetMapping("/listItem/externalFoldr")
    public String externalFolderListItem(@RequestParam("path") String path){
        File file = new File(path);
        GenericExternalFolder externalFolder = new GenericExternalFolder(file);
        return BasicResultResponse.success(externalFolder.listItem()).toJSONString();
    }

    /**
     * 外部复制
     * @param sourcePath 目标路径
     * @param destinationPath 源路径
     */
    @PutMapping("/copy")
    public BasicResultResponse<String> directCopy( @RequestParam("sourcePath") String sourcePath, @RequestParam("destinationPath") String destinationPath ) throws IOException {
        ElementNode elementNode = this.primaryFileSystem.queryElement(sourcePath);
        if(elementNode instanceof ExternalFolder){
            ExternalFolder externalFolder = (ExternalFolder) elementNode;
            this.primaryFileSystem.directCopy( externalFolder.getPath(),destinationPath );
        }

        return BasicResultResponse.success();
    }

    public BasicResultResponse<String> createExternalSymbolic(@RequestBody CreateExternalSymbolicDTO dto){
        return BasicResultResponse.success();
    }

    /**
     * 直接上传文件
     * @param file 文件
     * @param sourcePath 目标文件夹
     */
    @PostMapping("/directUpload")
    public BasicResultResponse<String> directUpload( @RequestParam("file") MultipartFile file, @RequestParam("sourcePath") String sourcePath ) throws IOException {
        ElementNode elementNode = this.primaryFileSystem.queryElement(sourcePath);
        if( elementNode instanceof ExternalFolder){
            ExternalFolder externalFolder = (ExternalFolder) elementNode;
            String path = externalFolder.getPath();

            // 确保目标文件夹存在
            Path targetDir = Paths.get(path);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            // 构建目标文件路径
            Path targetFile = targetDir.resolve(file.getOriginalFilename());

            // 将 MultipartFile 写入目标文件
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return BasicResultResponse.success();
    }

    /**
     * 删除外部文件
     * @param path 文件路径
     * @return 返回操作结果
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove( @RequestParam("path") String path ){
        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        if( elementNode instanceof ExternalFolder ){
            ExternalFolder externalFolder = (ExternalFolder) elementNode;
            externalFolder.delete();
        }else if( elementNode instanceof ExternalFile ){
            ExternalFile externalFile = (ExternalFile) elementNode;
            externalFile.delete();
        }
        return BasicResultResponse.success();
    }

    /**
     * 创建外接文件
     * @param dto 创建外接文件信息
     * @return 返回操作结果
     */
    @PostMapping("/createExternalFile")
    public BasicResultResponse<String> createExternalFile(@RequestBody CreateExternalDTO dto){
        ElementNode elementNode = this.primaryFileSystem.queryElement(dto.getFolderPath());

        Folder folder = elementNode.evinceFolder();
        folder.createExternalSymbolic( dto.getExternalSymbolicName(), dto.getReparsedPoint());
        return BasicResultResponse.success();
    }
}
