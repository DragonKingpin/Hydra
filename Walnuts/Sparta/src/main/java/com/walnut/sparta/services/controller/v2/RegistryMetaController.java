package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ArchConfigNode;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.GenericDistributeRegistry;
import com.pinecone.ulf.util.id.GUIDs;
//import com.walnut.sparta.services.drivers.RegistryMasterManipulatorImpl;
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
@RequestMapping( "/api/v2/registryMeta" )
public class RegistryMetaController {
    @Resource
    private RegistryMasterManipulator               registryMasterManipulator;

    private DistributedRegistry                     distributedRegistry;

    @PostConstruct
    public void init() {
        this.distributedRegistry = new GenericDistributeRegistry(null, this.registryMasterManipulator );
    }

    /**
     * 添加一条namespace节点
     * @param namespaceNode 节点信息
     * @return 返回操作情况
     */
    @PostMapping("/putNamespaceNode")
    public BasicResultResponse<String> putNamespaceNode(@RequestBody GenericNamespaceNode namespaceNode){
        this.distributedRegistry.put(namespaceNode);
        return BasicResultResponse.success();
    }

    /**
     * 添加一个configNode节点
     * @param configNode 节点信息
     * @return 返回操作情况
     */
    @PostMapping("/putConfigNode")
    public BasicResultResponse<String> putConfigNode( @RequestBody ArchConfigNode configNode ){
        this.distributedRegistry.put(configNode);
        return BasicResultResponse.success();
    }

    /**
     * 获取节点路径信息
     * @param guid 节点guid
     * @return 返回节点路径
     */
    @GetMapping("/getPath")
    public BasicResultResponse<String> getPath( @RequestParam("guid") String guid ){
        String path = this.distributedRegistry.getPath( GUIDs.GUID72( guid ) );
        return BasicResultResponse.success(path);
    }

    /**
     * 获取节点信息
     * @param guid 节点guid
     * @return 返回节点信息
     */
    @GetMapping("/getNode")
    public BasicResultResponse<TreeNode> getNode( @RequestParam("guid") String guid ){
        TreeNode node = this.distributedRegistry.get( GUIDs.GUID72( guid ) );
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
        GenericProperty genericProperties = new GenericProperty();
        genericProperties.setCreateTime(LocalDateTime.now());
        genericProperties.setUpdateTime(LocalDateTime.now());
        genericProperties.setKey(key);
        genericProperties.setValue(value);
        genericProperties.setType(type);

        this.distributedRegistry.putProperties( genericProperties, GUIDs.GUID72( guid ) );
        return BasicResultResponse.success();
    }

    /**
     * 移除节点
     * @param guid 节点guid
     * @return 返回状态码
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove(@RequestParam("Guid") GUID guid){
        this.distributedRegistry.remove( guid );
        return BasicResultResponse.success();
    }

    /**
     * 解析路径
     * @param path 路径信息
     * @return 返回解析后的节点信息
     */
    @GetMapping("/getNodeByPath")
    public BasicResultResponse<TreeNode> getNodeByPath( @RequestParam("path") String path ){
        TreeNode treeNode = this.distributedRegistry.getNodeByPath(path);
        return BasicResultResponse.success( treeNode );
    }

    /**
     * 给节点添加text信息
     * @param guid 几点guid
     * @param text text信息
     * @param type text类型
     * @return 返回状态码
     */
    @PostMapping("/putTextValue")
    public BasicResultResponse<String> insertTextValue(@RequestParam("guid")String guid,
                                                       @RequestParam("text") String text,
                                                       @RequestParam("type") String type){
        this.distributedRegistry.putTextValue( GUIDs.GUID72( guid ) ,text,type);
        return BasicResultResponse.success();
    }

    /**
     * 获取节点信息不含继承
     * @param guid 节点guid
     * @return 返回节点信息
     */
    @GetMapping("/getThis")
    public BasicResultResponse<TreeNode > getThis(@RequestParam("guid") String guid){
        return BasicResultResponse.success(
                this.distributedRegistry.getThis( GUIDs.GUID72( guid ) )
        );
    }
}
