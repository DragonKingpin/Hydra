package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.task.entity.GenericTaskNode;
import com.pinecone.hydra.task.source.TaskManipulatorSharer;
import com.pinecone.hydra.task.tree.DistributedTaskMetaTree;
import com.pinecone.hydra.task.tree.GenericDistributedTaskMetaTree;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.services.drivers.TaskTreeManipulatorSharerImpl;
import com.walnut.sparta.system.BasicResultResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/TaskMeta" )
public class TaskMetaController {
    @Resource
    private TaskManipulatorSharer           taskManipulatorSharer;

    @Resource
    private TaskTreeManipulatorSharerImpl   treeManipulatorSharer;

    private DistributedTaskMetaTree distributedTaskMetaTree;

    @PostConstruct
    public void init() {
        this.distributedTaskMetaTree = new GenericDistributedTaskMetaTree(this.taskManipulatorSharer,this.treeManipulatorSharer);
    }

    /**
     * 新增一个节点
     * @param genericTaskNode 节点信息
     * @return 返回节点guid
     */
    @PostMapping("/insert")
    public BasicResultResponse<String> insert(@RequestBody GenericTaskNode genericTaskNode){
        GUID insert = this.distributedTaskMetaTree.insert(genericTaskNode);
        return BasicResultResponse.success(insert.toString());
    }

    /**
     * 获取节点路径信息
     * @param guid 节点guid
     * @return 返回路径信息
     */
    @GetMapping("/getPath")
    public BasicResultResponse<String> getPath(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        String path = this.distributedTaskMetaTree.getPath(guid72);
        return BasicResultResponse.success(path);
    }

    /**
     * 获取节点信息
     * @param guid 节点guid
     * @return 返回节点信息
     */
    @GetMapping("/getNode")
    public BasicResultResponse<TreeNode> getNode(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        TreeNode treeNode = this.distributedTaskMetaTree.get(guid72);
        return BasicResultResponse.success(treeNode);
    }

    /**
     * 解析路径信息
     * @param path 路径
     * @return 返回节点信息
     */
    @GetMapping("/parsePath")
    public BasicResultResponse<TreeNode> parsePath(@RequestParam("path") String path){
        TreeNode treeNode = this.distributedTaskMetaTree.parsePath(path);
        return BasicResultResponse.success(treeNode);
    }

    /**
     * 移除节点
     * @param guid 节点guid
     * @return 返回操作信息
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        this.distributedTaskMetaTree.remove(guid72);
        return BasicResultResponse.success();
    }
}
