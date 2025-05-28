package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.redstone.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.domain.service.FileSystemService;
import com.walnut.sparta.ucdn.console.infrastructure.dto.RenameDTO;
import com.walnut.sparta.ucdn.console.infrastructure.dto.UpdateFileNameDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/ucdn/file" )
@CrossOrigin
public class CDNFileController {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private FileSystemService fileSystemService;

    @Resource
    private VersionManage primaryVersion;

    /**
     * 创建文件
     * @param filePath 文件路径
     * @return 返回操作状态
     */
    @GetMapping("/create")
    public BasicResultResponse<String> createFile(@RequestParam String filePath ){
        this.primaryFileSystem.affirmFileNode( filePath );
        return BasicResultResponse.success();
    }

    /**
     * 获取文件或文件夹属性
     * @param nodeGuid 文件或文件夹guid
     * @return 返回属性信息
     */
    @GetMapping("/attribute")
    public BasicResultResponse<FileTreeNode> attribute(@RequestParam("nodeGuid") String nodeGuid ){
        FileTreeNode fileTreeNode = this.primaryFileSystem.get(GUIDs.GUID128(nodeGuid));
        return BasicResultResponse.success( fileTreeNode );
    }

    /**
     * 移除文件夹或者文件
     * @param fileGuid 文件夹或者文件guid
     * @return 返回操作结果
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> removeFile( String fileGuid ){
        this.fileSystemService.remove( GUIDs.GUID128( fileGuid ) );
        this.primaryFileSystem.remove( GUIDs.GUID128( fileGuid ) );
        return BasicResultResponse.success();
    }

    /**
     * 重命名文件或文件夹
     * @param dto 信息
     * @return 返回操作信息
     */
    @PostMapping("/rename")
    public BasicResultResponse<String> renameFile(@RequestBody RenameDTO dto){
        this.primaryFileSystem.renameFile( dto.getPath(), dto.getNewName() );
        return BasicResultResponse.success();
    }

    /**
     * 重命名接口
     * @param dto 重命名数据
     * @return
     */
    @PostMapping("/updateFileName")
    public BasicResultResponse<String> updateFileName(@RequestBody UpdateFileNameDTO dto){
        this.primaryFileSystem.renameFile( dto.getFilePath(), dto.getNewFileName() );
        return BasicResultResponse.success();
    }
}
