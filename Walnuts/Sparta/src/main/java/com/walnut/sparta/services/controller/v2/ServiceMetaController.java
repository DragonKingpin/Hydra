package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationDescriptionManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceDescriptionManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.services.mapper.GenericApplicationDescriptionManipulator;
import com.walnut.sparta.services.mapper.GenericApplicationNodeManipulator;
import com.walnut.sparta.services.mapper.GenericClassifNodeManipulator;
import com.walnut.sparta.services.mapper.GenericClassifRulesManipulator;
import com.walnut.sparta.services.mapper.GenericNodeMetadataManipulator;
import com.walnut.sparta.services.mapper.GenericServiceDescriptionManipulator;
import com.walnut.sparta.services.mapper.GenericServiceNodeManipulator;
import com.walnut.sparta.services.pojo.ApplicationFunctionalNodeMeta;
import com.walnut.sparta.services.pojo.ApplicationFunctionalNodeOperator;
import com.walnut.sparta.services.pojo.ClassifFunctionalNodeMeta;
import com.walnut.sparta.services.pojo.ClassifFunctionalNodeOperator;
import com.walnut.sparta.services.pojo.DistributedScopeService;
import com.walnut.sparta.services.pojo.ServiceFunctionalNodeMeta;
import com.walnut.sparta.services.pojo.ServiceFunctionalNodeOperator;
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
    ApplicationNodeManipulator genericApplicationNodeManipulator;
    @Resource
    ApplicationDescriptionManipulator genericApplicationDescriptionManipulator;
    @Resource
    ClassifNodeManipulator genericClassifNodeManipulator;
    @Resource
    ClassifRulesManipulator genericClassifRulesManipulator;
    @Resource
    NodeMetadataManipulator genericNodeMetadataManipinate;
    @Resource
    ServiceNodeManipulator genericServiceNodeManipulator;
    @Resource
    ServiceDescriptionManipulator genericServiceDescriptionManipulator;
    @Resource
    ServiceTreeMapper serviceTreeMapper;
    /**
     * 渲染单节点信息
     * @param GUID 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public FunctionalNodeMeta queryNodeInfoByGUID(@PathVariable("GUID") String GUID){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
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
    public FunctionalNodeMeta queryNodeInfoByPath(@RequestParam("path") String path){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNodeInformation 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode(@RequestBody ServiceFunctionalNodeMeta serviceNodeInformation){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveServiceNode(serviceNodeInformation);
    }

    /**
     * 创建一个应用节点
     * @param applicationNodeInformation 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode(@RequestBody ApplicationFunctionalNodeMeta applicationNodeInformation){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveApplicationNode(applicationNodeInformation);
    }

    /**
     * 创建一个分类节点
     * @param classifNodeInformation 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode(@RequestBody ClassifFunctionalNodeMeta classifNodeInformation){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        return distributedScopeService.saveClassifNode(classifNodeInformation);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode")
    public String deleteNode(@RequestParam("nodeGUID") String guid){
        ServiceFunctionalNodeOperator serviceNodeOperation = new ServiceFunctionalNodeOperator(this.genericServiceNodeManipulator,this.genericServiceDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperator applicationNodeOperation = new ApplicationFunctionalNodeOperator(this.genericApplicationNodeManipulator,this.genericApplicationDescriptionManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperator classifNodeOperation = new ClassifFunctionalNodeOperator(this.genericClassifNodeManipulator,this.genericClassifRulesManipulator,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipulator,this.genericServiceNodeManipulator,this.genericClassifNodeManipulator);
        distributedScopeService.deleteNode(new GUID72(guid));
        return "删除成功";
    }
}
