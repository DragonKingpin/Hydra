package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.ulf.util.id.GUID72;
import com.pinecone.hydra.service.tree.operator.ApplicationNodeWideData;
import com.pinecone.hydra.service.tree.operator.ApplicationNodeOperator;
import com.pinecone.hydra.service.tree.operator.ClassificationNodeWideData;
import com.pinecone.hydra.service.tree.operator.ClassificationNodeOperator;
import com.walnut.sparta.services.pojo.DistributedScopeServiceTree;
import com.pinecone.hydra.service.tree.operator.ServiceNodeWideData;
import com.pinecone.hydra.service.tree.operator.ServiceNodeOperator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/serviceNode" )
public class ServiceMetaController {
    @Resource
    private DefaultMetaNodeManipulators defaultMetaNodeManipulators;

    /**
     * 渲染单节点信息
     * @param guid 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public ServiceTreeNode queryNodeInfoByGUID(@PathVariable("GUID") String guid){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        GUID72 guid72 = new GUID72(guid);
        return distributedScopeServiceTree.selectNode(guid72);
    }

    /**
     * 用于将路径反解析为节点信息
     * @param path 节点路径
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByPath")
    public ServiceTreeNode queryNodeInfoByPath(@RequestParam("path") String path){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        return distributedScopeServiceTree.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNodeInformation 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode(@RequestBody GenericServiceNode serviceNodeInformation){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        return distributedScopeServiceTree.saveServiceNode(serviceNodeInformation);
    }

    /**
     * 创建一个应用节点
     * @param applicationNodeInformation 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode(@RequestBody GenericApplicationNode applicationNodeInformation){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        return distributedScopeServiceTree.saveApplicationNode(applicationNodeInformation);
    }

    /**
     * 创建一个分类节点
     * @param classifNodeInformation 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode(@RequestBody GenericClassificationNode classifNodeInformation){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        return distributedScopeServiceTree.saveClassifNode(classifNodeInformation);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/removeNode")
    public String removeNode(@RequestParam("nodeGUID") String guid){
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(this.defaultMetaNodeManipulators);
        distributedScopeServiceTree.removeNode(new GUID72(guid));
        return "删除成功";
    }
}
