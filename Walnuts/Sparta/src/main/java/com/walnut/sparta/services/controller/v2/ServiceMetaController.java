package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
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
     * @param GUID 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public ServiceTreeNode queryNodeInfoByGUID(@PathVariable("GUID") String GUID){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator( this.defaultMetaNodeManipulators );

        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        GUID72 guid72 = new GUID72();
        com.pinecone.framework.util.id.GUID parse = guid72.parse(GUID);
        return distributedScopeService.selectNode(parse);
    }

    /**
     * 用于将路径反解析为节点信息
     * @param path 节点路径
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByPath")
    public ServiceTreeNode queryNodeInfoByPath(@RequestParam("path") String path){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator(this.genericServiceNodeManipulator,this.genericServiceMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ApplicationNodeOperator applicationNodeOperation = new ApplicationNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ClassificationNodeOperator classifNodeOperation = new ClassificationNodeOperator(this.genericClassifNodeManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNodeInformation 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode(@RequestBody ServiceNodeWideData serviceNodeInformation){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator(this.genericServiceNodeManipulator,this.genericServiceMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ApplicationNodeOperator applicationNodeOperation = new ApplicationNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ClassificationNodeOperator classifNodeOperation = new ClassificationNodeOperator(this.genericClassifNodeManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveServiceNode(serviceNodeInformation);
    }

    /**
     * 创建一个应用节点
     * @param applicationNodeInformation 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode(@RequestBody ApplicationNodeWideData applicationNodeInformation){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator(this.genericServiceNodeManipulator,this.genericServiceMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ApplicationNodeOperator applicationNodeOperation = new ApplicationNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ClassificationNodeOperator classifNodeOperation = new ClassificationNodeOperator(this.genericClassifNodeManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveApplicationNode(applicationNodeInformation);
    }

    /**
     * 创建一个分类节点
     * @param classifNodeInformation 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode(@RequestBody ClassificationNodeWideData classifNodeInformation){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator(this.genericServiceNodeManipulator,this.genericServiceMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ApplicationNodeOperator applicationNodeOperation = new ApplicationNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ClassificationNodeOperator classifNodeOperation = new ClassificationNodeOperator(this.genericClassifNodeManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveClassifNode(classifNodeInformation);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode")
    public String deleteNode(@RequestParam("nodeGUID") String guid){
        ServiceNodeOperator serviceNodeOperation = new ServiceNodeOperator(this.genericServiceNodeManipulator,this.genericServiceMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ApplicationNodeOperator applicationNodeOperation = new ApplicationNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationMetaManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        ClassificationNodeOperator classifNodeOperation = new ClassificationNodeOperator(this.genericClassifNodeManipulator,this.genericNodeMetadataManipulator,this.scopeTreeManipulator);
        DistributedScopeServiceTree distributedScopeService = new DistributedScopeServiceTree(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.scopeTreeManipulator,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        distributedScopeService.deleteNode(new GUID72(guid));
        return "删除成功";
    }
}
