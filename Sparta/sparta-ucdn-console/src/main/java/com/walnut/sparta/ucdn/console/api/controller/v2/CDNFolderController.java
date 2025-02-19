package com.walnut.sparta.ucdn.console.api.controller.v2;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.api.response.BasicResultResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping( "/api/v2/ucdn/folder" )
@CrossOrigin
public class CDNFolderController {
    @Resource
    private KOMFileSystem primaryFileSystem;
    @Resource
    private VersionManage versionManage;

    /**
     * 获取文件夹下所有内容
     * @param folderGuid 文件夹guid
     * @returnS
     */
    @GetMapping("/folder/listItem")
    public String listItem(@RequestParam String folderGuid ){
        Folder folder = this.primaryFileSystem.getFolder(GUIDs.GUID72(folderGuid));
        List<FileTreeNode> fileTreeNodes = folder.listItem();
        for (FileTreeNode fileTreeNode : fileTreeNodes) {
            if (versionManage.queryIsManage(fileTreeNode.getGuid())){
                List<GUID> versions = versionManage.fetchVersions(fileTreeNode.getGuid());
                GUID firstVersion = versions.get(0);
                FileTreeNode firstVersionFileTreeNode = this.primaryFileSystem.get(firstVersion);
                String fileName = firstVersionFileTreeNode.getName();
                String fileExtension = "";

                if (fileName.contains(".")) {
                    fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                fileTreeNode.setName(fileTreeNode.getName()+'.'+fileExtension);
            }
        }
        return  BasicResultResponse.success(fileTreeNodes).toJSONString() ;
    }

    /**
     * 创建文件夹
     * @param destDirPath 文件夹路径
     * @return 返回操作状态
     */
    @GetMapping("/creat/folder")
    public BasicResultResponse<String> createFolder( @RequestParam("destDirPath") String destDirPath ){
        System.out.println(destDirPath);
        this.primaryFileSystem.affirmFolder( destDirPath );
        return BasicResultResponse.success();
    }

    /**
     * 获取文件或文件夹属性
     * @param nodeGuid 文件或文件夹guid
     * @return 返回属性信息
     */
    @GetMapping("/attribute")
    public             BasicResultResponse< FileTreeNode > attribute( @RequestParam("nodeGuid") String nodeGuid ){
        FileTreeNode fileTreeNode = this.primaryFileSystem.get(GUIDs.GUID72(nodeGuid));
        return BasicResultResponse.success( fileTreeNode );
    }

    /**
     * 获取所有根文件夹
     * @return 返回根信息
     */
    @GetMapping("/list/root")
    public String listRoot(){
        List<FileTreeNode> roots = this.primaryFileSystem.fetchRoot();
        return BasicResultResponse.success( roots ).toJSONString();
    }

    /**
     * 移除文件夹或者文件
     * @param fileGuid 文件夹或者文件guid
     * @return 返回操作结果
     */
    @DeleteMapping("/remove/file")
    public BasicResultResponse<String> removeFile( String fileGuid ){
        this.primaryFileSystem.remove( GUIDs.GUID72( fileGuid ) );
        return BasicResultResponse.success();
    }

}
