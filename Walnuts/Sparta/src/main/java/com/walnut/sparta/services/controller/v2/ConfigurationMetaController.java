package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericConfNode;
import com.pinecone.hydra.config.distribute.entity.GenericNamespaceNode;
import com.pinecone.hydra.config.distribute.entity.GenericProperties;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.config.distribute.tree.DistributedConfMetaTree;
import com.pinecone.hydra.config.distribute.tree.GenericDistributeConfMetaTree;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.services.nodes.ConfManipulatorSharerImpl;
import com.walnut.sparta.services.nodes.ConfTreeManipulatorSharerImpl;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping( "/api/v2/configurationMeta" )
public class ConfigurationMetaController {
    @Resource
    private ConfManipulatorSharerImpl               confManipulatorSharer;
    @Resource
    private ConfTreeManipulatorSharerImpl           confTreeManipulatorSharer;
    private DistributedConfMetaTree                 distributedConfMetaTree;

    @PostConstruct
    public void init() {
        this.distributedConfMetaTree = new GenericDistributeConfMetaTree(this.confManipulatorSharer,confTreeManipulatorSharer);
    }

    /**
     * 添加一条namespace节点
     * @param namespaceNode 节点信息
     * @return 返回操作情况
     */
    @PostMapping("/insertNamespaceNode")
    public BasicResultResponse<String> insertNamespaceNode(@RequestBody GenericNamespaceNode namespaceNode){
        this.distributedConfMetaTree.insert(namespaceNode);
        return BasicResultResponse.success();
    }

    /**
     * 添加一个confNode节点
     * @param confNode 节点信息
     * @return 返回操作情况
     */
    @PostMapping("/insertConfNode")
    public BasicResultResponse<String> insertConfNode(@RequestBody GenericConfNode confNode){
        this.distributedConfMetaTree.insert(confNode);
        return BasicResultResponse.success();
    }
    /**
     * 获取节点路径信息
     * @param guid 节点guid
     * @return 返回节点路径
     */
    @GetMapping("/getPath")
    public BasicResultResponse<String> getPath(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        String path = this.distributedConfMetaTree.getPath(guid72);
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
        TreeNode node = this.distributedConfMetaTree.get(guid72);
        return BasicResultResponse.success(node);
    }

    /**
     * 给节点添加配置信息
     * @param key 键
     * @param guid 所属节点的guid
     * @param value 值
     * @param type 值的类型
     * @return 返回状态码
     */
    @PostMapping("/insertProperties")
    public BasicResultResponse<String> insertProperties(@RequestParam("key")String key, @RequestParam("Guid") String guid,
                                                        @RequestParam("value") String value, @RequestParam("type") String type){
        GenericProperties genericProperties = new GenericProperties();
        genericProperties.setCreateTime(LocalDateTime.now());
        genericProperties.setUpdateTime(LocalDateTime.now());
        genericProperties.setKey(key);
        genericProperties.setValue(value);
        genericProperties.setType(type);
        GUID72 guid72 = new GUID72(guid);
        this.distributedConfMetaTree.insertProperties(genericProperties,guid72);
        return BasicResultResponse.success();
    }

    /**
     * 移除节点
     * @param guid 节点guid
     * @return 返回状态码
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove(@RequestParam("Guid") GUID guid){
        this.distributedConfMetaTree.remove(guid);
        return BasicResultResponse.success();
    }

    /**
     * 解析路径
     * @param path 路径信息
     * @return 返回解析后的节点信息
     */
    @GetMapping("/parsePath")
    public BasicResultResponse<TreeNode> parsePath(@RequestParam("path") String path){
        TreeNode treeNode = this.distributedConfMetaTree.parsePath(path);
        return BasicResultResponse.success(treeNode);
    }

    /**
     * 给节点添加text信息
     * @param guid 几点guid
     * @param text text信息
     * @param type text类型
     * @return 返回状态码
     */
    @PostMapping("/insertTextValue")
    public BasicResultResponse<String> insertTextValue(@RequestParam("guid")String guid,
                                                       @RequestParam("text") String text,
                                                       @RequestParam("type") String type){
        GUID72 guid72 = new GUID72(guid);
        this.distributedConfMetaTree.insertTextValue(guid72,text,type);
        return BasicResultResponse.success();
    }

    /**
     * 获取节点信息不含继承
     * @param guid 节点guid
     * @return 返回节点信息
     */
    @GetMapping("/getWithoutInheritance")
    public BasicResultResponse<TreeNode> getWithoutInheritance(@RequestParam("guid") String guid){
        GUID72 guid72 = new GUID72(guid);
        return BasicResultResponse.success(this.distributedConfMetaTree.getWithoutInheritance(guid72));
    }
}
