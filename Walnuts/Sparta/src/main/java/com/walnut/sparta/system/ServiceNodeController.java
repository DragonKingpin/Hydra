package com.walnut.sparta.system;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.FunctionalNodeInformation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.mapper.GenericApplicationDescriptionManipinate;
import com.walnut.sparta.mapper.GenericApplicationNodeManipinate;
import com.walnut.sparta.mapper.GenericClassifNodeManipinate;
import com.walnut.sparta.mapper.GenericClassifRulesManipinate;
import com.walnut.sparta.mapper.GenericNodeMetadataManipinate;
import com.walnut.sparta.mapper.GenericServiceDescriptionManipinate;
import com.walnut.sparta.mapper.GenericServiceNodeManipinate;
import com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation;
import com.walnut.sparta.pojo.ApplicationFunctionalNodeOperation;
import com.walnut.sparta.pojo.ClassifFunctionalNodeInformation;
import com.walnut.sparta.pojo.ClassifFunctionalNodeOperation;
import com.walnut.sparta.pojo.DistributedScopeService;
import com.walnut.sparta.pojo.ServiceFunctionalNodeInformation;
import com.walnut.sparta.pojo.ServiceFunctionalNodeOperation;
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
@RequestMapping( "/serviceNode" )
public class ServiceNodeController {
    @Resource
    GenericApplicationNodeManipinate genericApplicationNodeManipinate;
    @Resource
    GenericApplicationDescriptionManipinate genericApplicationDescriptionManipinate;
    @Resource
    GenericClassifNodeManipinate genericClassifNodeManipinate;
    @Resource
    GenericClassifRulesManipinate genericClassifRulesManipinate;
    @Resource
    GenericNodeMetadataManipinate genericNodeMetadataManipinate;
    @Resource
    GenericServiceNodeManipinate genericServiceNodeManipinate;
    @Resource
    GenericServiceDescriptionManipinate genericServiceDescriptionManipinate;
    @Resource
    ServiceTreeMapper serviceTreeMapper;
    /**
     * 渲染单节点信息
     * @param GUID 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public FunctionalNodeInformation queryNodeInfoByGUID(@PathVariable("GUID") String GUID){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
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
    public FunctionalNodeInformation queryNodeInfoByPath(@RequestParam("path") String path){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        return distributedScopeService.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNodeInformation 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode(@RequestBody ServiceFunctionalNodeInformation serviceNodeInformation){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        return distributedScopeService.saveServiceNode(serviceNodeInformation);
    }

    /**
     * 创建一个应用节点
     * @param applicationNodeInformation 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode(@RequestBody ApplicationFunctionalNodeInformation applicationNodeInformation){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        return distributedScopeService.saveApplicationNode(applicationNodeInformation);
    }

    /**
     * 创建一个分类节点
     * @param classifNodeInformation 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode(@RequestBody ClassifFunctionalNodeInformation classifNodeInformation){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        return distributedScopeService.saveClassifNode(classifNodeInformation);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode")
    public String deleteNode(@RequestParam("nodeGUID") String guid){
        ServiceFunctionalNodeOperation serviceNodeOperation = new ServiceFunctionalNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationFunctionalNodeOperation applicationNodeOperation = new ApplicationFunctionalNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifFunctionalNodeOperation classifNodeOperation = new ClassifFunctionalNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation,this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        distributedScopeService.deleteNode(new GUID72(guid));
        return "删除成功";
    }
}
