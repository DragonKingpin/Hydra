package com.walnut.sparta.services.controller.v2;

import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNode;
import com.pinecone.hydra.scenario.tree.DistributedScenarioMetaTree;
import com.pinecone.hydra.scenario.tree.GenericDistributedScenarioMetaTree;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.services.drivers.ScenarioMetaManipulatorSharerImpl;
import com.walnut.sparta.services.drivers.ScenarioTreeManipulatorSharerImpl;
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
@RequestMapping( "/api/v2/ScenarioMeta" )
public class ScenarioMetaController {
    @Resource
    ScenarioTreeManipulatorSharerImpl       scenarioTreeManipulatorSharer;
    @Resource
    ScenarioMetaManipulatorSharerImpl       scenarioMetaManipulatorSharer;

    private DistributedScenarioMetaTree     distributedScenarioMetaTree;

    @PostConstruct
    public void init() {
        this.distributedScenarioMetaTree = new GenericDistributedScenarioMetaTree(this.scenarioMetaManipulatorSharer,this.scenarioTreeManipulatorSharer);
    }

    /**
     * 插入一个节点
     * @param genericNamespaceNode 节点信息
     * @return
     */
    @PostMapping("/insert")
    public BasicResultResponse<String> insert(@RequestBody GenericNamespaceNode genericNamespaceNode){
        this.distributedScenarioMetaTree.insert(genericNamespaceNode);
        return BasicResultResponse.success();
    }

    /**
     * 获取路径信息
     * @param guid 节点guid
     * @return 返回路径信息
     */
    @GetMapping("/getPath")
    public BasicResultResponse<String> getPath(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        String path = this.distributedScenarioMetaTree.getPath(guid72);
        return BasicResultResponse.success(path);
    }

    /**
     * 获取命名空间信息
     * @param guid 节点guid
     * @return 返回节点信息
     */
    @GetMapping("/getNode")
    public BasicResultResponse<TreeNode> getNode(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        TreeNode treeNode = this.distributedScenarioMetaTree.get(guid72);
        return BasicResultResponse.success(treeNode);
    }

    /**
     * 解析路径信息
     * @param path 路径
     * @return 返回解析出来的节点信息
     */
    @GetMapping("/parsePath")
    public BasicResultResponse<TreeNode> parsePath(@RequestParam("path") String path){
        TreeNode treeNode = this.distributedScenarioMetaTree.parsePath(path);
        return BasicResultResponse.success(treeNode);
    }

    /**
     * 删除节点
     * @param guid 节点guid
     * @return 返回操作结果
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        this.distributedScenarioMetaTree.remove(guid72);
        return BasicResultResponse.success();
    }
}
