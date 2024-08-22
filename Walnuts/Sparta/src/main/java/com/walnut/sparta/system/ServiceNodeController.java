package com.walnut.sparta.system;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.NodeInformation;
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
import com.walnut.sparta.pojo.ApplicationNodeInformation;
import com.walnut.sparta.pojo.ApplicationNodeOperation;
import com.walnut.sparta.pojo.ClassifNodeInformation;
import com.walnut.sparta.pojo.ClassifNodeOperation;
import com.walnut.sparta.pojo.DistributedScopeService;
import com.walnut.sparta.pojo.DistributedScopeTree;
import com.walnut.sparta.pojo.ServiceNodeInformation;
import com.walnut.sparta.pojo.ServiceNodeOperation;
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
    DistrubuteScopeTreeDataManipinate distrubuteScopeTreeDataManipinate;
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
    public NodeInformation queryNodeInfoByGUID(@PathVariable("GUID") String GUID){
        DistributedScopeService distributedScopeService = new DistributedScopeService(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericServiceDescriptionManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate);
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
    public NodeInformation queryNodeInfoByPath(@RequestParam("path") String path){
        DistributedScopeService distributedScopeService = new DistributedScopeService(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericServiceDescriptionManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate);
        return distributedScopeService.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNodeInformation 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode(@RequestBody ServiceNodeInformation serviceNodeInformation){
        DistributedScopeService distributedScopeService = new DistributedScopeService(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericServiceDescriptionManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate);
        return distributedScopeService.saveServiceNode(serviceNodeInformation);
    }

    /**
     * 创建一个应用节点
     * @param applicationNodeInformation 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode(@RequestBody ApplicationNodeInformation applicationNodeInformation){
        DistributedScopeService distributedScopeService = new DistributedScopeService(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericServiceDescriptionManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate);
        return distributedScopeService.saveApplicationNode(applicationNodeInformation);
    }

    /**
     * 创建一个分类节点
     * @param classifNodeInformation 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode(@RequestBody ClassifNodeInformation classifNodeInformation){
        DistributedScopeService distributedScopeService = new DistributedScopeService(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericServiceDescriptionManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate);
        return distributedScopeService.saveClassifNode(classifNodeInformation);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode")
    public String deleteNode(@RequestParam("nodeGUID") String guid){
        ServiceNodeOperation serviceNodeOperation = new ServiceNodeOperation(this.genericServiceNodeManipinate,this.genericServiceDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ApplicationNodeOperation applicationNodeOperation = new ApplicationNodeOperation(this.genericApplicationNodeManipinate,this.genericApplicationDescriptionManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        ClassifNodeOperation classifNodeOperation = new ClassifNodeOperation(this.genericClassifNodeManipinate,this.genericClassifRulesManipinate,this.genericNodeMetadataManipinate,this.serviceTreeMapper);
        DistributedScopeService distributedScopeService = new DistributedScopeService(classifNodeOperation,applicationNodeOperation,serviceNodeOperation);
        distributedScopeService.deleteNode(new GUID72(guid));
        return "删除成功";
    }
}
