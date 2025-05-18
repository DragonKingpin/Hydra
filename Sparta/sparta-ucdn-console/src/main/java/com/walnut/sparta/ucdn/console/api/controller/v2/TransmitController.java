package com.walnut.sparta.ucdn.console.api.controller.v2;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.entity.Site;
import com.pinecone.hydra.storage.bucket.source.SiteManipulator;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.direct.ExternalFile;
import com.pinecone.hydra.storage.file.direct.GenericNativeExternalFile;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.io.TitanOutputStreamChanface;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.version.entity.TitanVersion;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.redstone.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.domain.service.NodeFileDistributionService;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConsoleContents;
import com.walnut.sparta.ucdn.console.infrastructure.dto.ClusterFileSyncDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping( "/api/v2/ucdn/transmit" )
@CrossOrigin
public class TransmitController {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @Resource
    private BucketInstrument bucketInstrument;

    @Resource
    private VersionManage primaryVersion;
    @Resource
    private VersionManage versionManage;

    @Resource
    private NodeFileDistributionService fileDistributionService;

    @Value("${service.LocalUploadTemporaryWorkingDirectory}")
    private String majorTemporaryClusterFileDirectory;

    @Value("${service.TemporaryFileExtends}")
    private String temporaryFileExtends;

    @GetMapping("/download/guid")
    public void  getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] guids = parameterMap.get("guid");
        GUID storageObjectGuid = null;
        if( guids != null ){
            storageObjectGuid = GUIDs.GUID72( guids[0] );
        }

        ServletOutputStream outputStream = response.getOutputStream();
        TitanOutputStreamChanface kChannel = new TitanOutputStreamChanface(outputStream);

        FileNode storageObject = (FileNode) this.primaryFileSystem.get(storageObjectGuid);

        TitanFileExportEntity64 entity = new TitanFileExportEntity64(this.primaryFileSystem, this.primaryVolume, storageObject, kChannel);
        this.primaryFileSystem.export( entity );

    }

    /**
     * 上传文件
     * @param filePath 目标路径
     * @param version 版本号
     * @param file 文件
     * @param siteName 站点
     * @return 返回操作结果
     */
    @PostMapping("/upload")
    public BasicResultResponse<String> CDNUpload(@RequestParam("siteName") String siteName, @RequestParam("filePath") String filePath, @RequestParam("version") String version, @RequestParam("file") MultipartFile file) throws IOException {
        SiteManipulator siteManipulator = this.bucketInstrument.getSiteManipulator();
        Site site = siteManipulator.querySiteByName(siteName);
        if( site == null ){
            return BasicResultResponse.error("站点不存在");
        }
        int dotIndex = filePath.lastIndexOf(UCDNConsoleContents.PERIOD);
        String baseName = filePath.substring(0, dotIndex);
        String extension = filePath.substring(dotIndex + 1);
        String realFilePath = this.primaryFileSystem.getPath(site.getMountPointGuid()) + UCDNConsoleContents.FORWARD_SLASH + baseName;

        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        Folder node = this.primaryFileSystem.affirmFolder(realFilePath);
        String storageObjectPath = realFilePath + UCDNConsoleContents.VERSION_PREFIX+ UCDNConsoleContents.FORWARD_SLASH + version +UCDNConsoleContents.PERIOD+ extension;
        File tempFile = new File(majorTemporaryClusterFileDirectory+ UUID.randomUUID()+temporaryFileExtends);
        if( !tempFile.createNewFile() ){
            throw new IOException( "Creating file compromised, what :" + tempFile.toPath() );
        }
        file.transferTo(tempFile);

        FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
        TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );
        FileNode fileNode = fsNodeAllotment.newFileNode();
        fileNode.setDefinitionSize( tempFile.length() );
        fileNode.setName( tempFile.getName() );
        TitanFileReceiveEntity64 receiveEntity = new TitanFileReceiveEntity64( this.primaryFileSystem,storageObjectPath, fileNode,titanFileChannelKChannel,this.primaryVolume );

        this.primaryFileSystem.receive( receiveEntity );

        FileTreeNode storageObject = this.primaryFileSystem.get(this.primaryFileSystem.queryGUIDByPath(storageObjectPath));
        TitanVersion titanVersion = new TitanVersion();
        titanVersion.setVersion( version );
        titanVersion.setFileGuid( node.getGuid() );
        titanVersion.setTargetStorageObjectGuid( storageObject.getGuid() );
        titanVersion.setVersionGuid( this.primaryFileSystem.getGuidAllocator().nextGUID() );
        TitanVersionMapping versionMapping = new TitanVersionMapping();
        versionMapping.setFileGuid(titanVersion.getFileGuid());
        versionMapping.setEnableVersionGuid(titanVersion.getTargetStorageObjectGuid());
        versionMapping.setVersionGuid((titanVersion.getVersionGuid()));
        this.versionManage.insertVesionMapping(versionMapping);
        this.primaryVersion.insert( titanVersion );
        if( !tempFile.delete() ){
            throw new IOException( "Purging temporary file compromised, what :" + tempFile.toPath() );
        }

        return BasicResultResponse.success();
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

    @PostMapping("/clusterFileSync")
    public void clusterFileSync(@RequestBody ClusterFileSyncDTO dto) throws IOException, InterruptedException {
        this.fileDistributionService.clusterFileSync( dto );
    }

    private Chanface getKChannel( File file ) throws IOException {
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        return new TitanFileChannelChanface( channel );
    }
}
