package com.walnuts.sparta.uofs.console.api.controller.v2;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnuts.sparta.uofs.console.api.response.BasicResultResponse;
import com.walnuts.sparta.uofs.console.domain.RenameDTO;
import com.walnuts.sparta.uofs.console.domain.dto.UpdateFileNameDTO;
import com.walnuts.sparta.uofs.console.rpc.thrift.AccountClient;
import com.walnuts.sparta.uofs.console.service.FileService;
import org.apache.thrift.TException;
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
@RequestMapping( "/api/v2/uofs/bucket" )
@CrossOrigin
public class FileController {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    AccountClient accountClient;

    @Resource
    private FileService fileService;

    @Resource
    private VersionManage primaryVersion;

    /**
     * 创建文件
     * @param filePath 文件路径
     * @return 返回操作状态
     */
    @GetMapping("/creat/file")
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
        FileTreeNode fileTreeNode = this.primaryFileSystem.get(GUIDs.GUID72(nodeGuid));
        return BasicResultResponse.success( fileTreeNode );
    }

    /**
     * 移除文件夹或者文件
     * @param fileGuid 文件夹或者文件guid
     * @return 返回操作结果
     */
    @DeleteMapping("/remove/file")
    public BasicResultResponse<String> removeFile( String fileGuid ){
        this.fileService.remove( GUIDs.GUID72( fileGuid ) );
        this.primaryFileSystem.remove( GUIDs.GUID72( fileGuid ) );
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

    @GetMapping("/queryByPath")
    public String queryNodeByPath(@RequestParam("path") String path) throws TException {
        return this.accountClient.queryNodeByPath(path);
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
