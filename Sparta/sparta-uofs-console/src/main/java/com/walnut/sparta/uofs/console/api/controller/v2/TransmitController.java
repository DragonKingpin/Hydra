package com.walnut.sparta.uofs.console.api.controller.v2;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.external.ExternalFile;
import com.pinecone.hydra.storage.file.external.GenericNativeExternalFile;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.io.TitanOutputStreamChanface;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.uofs.console.api.response.BasicResultResponse;
import com.walnut.sparta.uofs.console.domain.dto.DownloadObjectByChannelDTO;
import com.walnut.sparta.uofs.console.domain.dto.UpdateObjectByChannelDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Map;

import static org.apache.commons.io.FilenameUtils.getExtension;

@RestController
@RequestMapping( "/api/v2/uofs/transmit" )
@CrossOrigin
public class TransmitController {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

//    @Resource
//    private UOFSConfig uofsConfig;

    /**
     * 使用channel上传对象
     * @param dto 上传所需数据
     * @return 返回操作结果
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/channel/update")
    public  BasicResultResponse<String> updateObjectByChannel(UpdateObjectByChannelDTO dto ) throws IOException {
        MultipartFile object = dto.getObject();
        File file = File.createTempFile( "uofs","."+ getExtension(object.getOriginalFilename()) );
        if( !file.exists() ){
            throw new IOException( "Creating file compromised, what :" + file.toPath() );
        }
        object.transferTo( file );
        Chanface chanface = this.getKChannel(file);

        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        FileNode fileNode = fsNodeAllotment.newFileNode();
        fileNode.setDefinitionSize( file.length() );
        fileNode.setName( file.getName() );

        TitanFileReceiveEntity64 receiveEntity = new TitanFileReceiveEntity64(
                this.primaryFileSystem, dto.getDestDirPath(), fileNode, chanface, this.primaryVolume
        );

        this.primaryFileSystem.receive( receiveEntity );
        if(!file.delete()){
            throw new IOException( "Purging temporary file compromised, what :" + file.toPath() );
        }
        return BasicResultResponse.success();
    }

    /**
     * 使用channel将对象下载到本地
     * @param dto 下载所需的数据
     * @return 返回操作结果
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/channel/download")
    public BasicResultResponse<String> downloadObjectByChannel( DownloadObjectByChannelDTO dto ) throws IOException {
        File file = new File( dto.getTargetPath());
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );

        FileNode fileNode = (FileNode) this.primaryFileSystem.get(this.primaryFileSystem.queryGUIDByPath(dto.getDestDirPath()));
        TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64( this.primaryFileSystem, this.primaryVolume, fileNode, titanFileChannelKChannel );
        primaryFileSystem.export( exportEntity );
        return BasicResultResponse.success();
    }

    @GetMapping("/download/guid")
    public void  getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] guids = parameterMap.get("guid");
        GUID storageObjectGuid = null;
        if( guids != null ){
            storageObjectGuid = GUIDs.GUID128( guids[0] );
        }

        ServletOutputStream outputStream = response.getOutputStream();
        TitanOutputStreamChanface kChannel = new TitanOutputStreamChanface(outputStream);

        FileNode storageObject = (FileNode) this.primaryFileSystem.get(storageObjectGuid);

        TitanFileExportEntity64 entity = new TitanFileExportEntity64(this.primaryFileSystem, this.primaryVolume, storageObject, kChannel);
        this.primaryFileSystem.export( entity );

    }

    /**
     * 使用文件路径下载文件
     */
    @GetMapping("/download/path")
    public void getFileByPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] paths = parameterMap.get("path");
        String path = null;

        if(paths != null){
            path = paths[0];
        }

        ServletOutputStream outputStream = response.getOutputStream();
        TitanOutputStreamChanface kChannel = new TitanOutputStreamChanface(outputStream);

        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        if(elementNode instanceof GenericNativeExternalFile){
            ExternalFile externalFile = (ExternalFile) elementNode;
            File nativeFile = externalFile.getNativeFile();
            try (FileInputStream fileInputStream = new FileInputStream(nativeFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                // 刷新输出流
                outputStream.flush();
                return;
            } catch (IOException e) {
                // 处理异常，比如记录日志等
                e.printStackTrace();
            }
        }

        if( elementNode instanceof GenericFileNode){
            FileNode fileNode = (FileNode) elementNode;
            TitanFileExportEntity64 entity = new TitanFileExportEntity64(this.primaryFileSystem, this.primaryVolume, fileNode, kChannel);
            this.primaryFileSystem.export( entity );
        }
    }

    /**
     *
     * @param filePath 文件要上传的路径
     * @param file 文件本体
     * @return
     */
    @PostMapping("/upload")
    public BasicResultResponse<String> upload(@RequestParam("filePath") String filePath, @RequestParam("file") MultipartFile file ) throws IOException {
            File tempFile = File.createTempFile("upload",".temp");
            if(!tempFile.exists()){
                throw new IOException( "Creating file compromised, what :" + tempFile.toPath() );
            }
            file.transferTo(tempFile);

            FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
            TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );
            FileNode fileNode = fsNodeAllotment.newFileNode();
            fileNode.setDefinitionSize( tempFile.length() );
            fileNode.setName( tempFile.getName() );
            TitanFileReceiveEntity64 receiveEntity = new TitanFileReceiveEntity64( this.primaryFileSystem,filePath, fileNode,titanFileChannelKChannel,this.primaryVolume );

            this.primaryFileSystem.receive( receiveEntity );
            if(!tempFile.delete()){
                throw new IOException( "Temporary file has been purged failed." );
            }
            return BasicResultResponse.success();
    }

    @PostMapping("/stream")
    public String handleStreamUpload(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream()) {
            // 处理输入流
            return "File stream processed.";
        }
    }

    private Chanface getKChannel(File file ) throws IOException {
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        return new TitanFileChannelChanface( channel );
    }
}
