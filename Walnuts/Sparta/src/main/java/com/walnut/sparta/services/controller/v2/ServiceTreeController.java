package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.services.mapper.ServiceNodeMapper;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.walnut.sparta.services.pojo.DistributedScopeServiceTree;
import com.walnut.sparta.services.service.ServiceTreeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/serviceTree" )
public class ServiceTreeController {
    @Resource
    ServiceTreeService serviceTreeService;

    @Resource
    private DefaultMetaNodeManipulators defaultMetaNodeManipulators;
    /**
     * 用于渲染路径信息
     * @param guid 节点UUID
     * @return 返回路径信息
     */
    @GetMapping("/getPath/{GUID}")
    public String getPath(@PathVariable("GUID") String guid){
        GenericDistributedScopeTree genericDistributedScopeTree = new GenericDistributedScopeTree(this.defaultMetaNodeManipulators);
        return genericDistributedScopeTree.getPath(new GUID72(guid));
    }

    /**
     * 向指定父节点添加子节点
     * @param nodeGUID 子节点GUID
     * @param parentGUID 父节点GUID
     * @return 返回添加情况
     */
    @PostMapping("/addNodeToParent")
    public String addNodeToParent(@RequestParam("nodeGUID") String nodeGUID,@RequestParam("parentGUID") String parentGUID ){
        GUID72 nodeGUID72 = new GUID72(nodeGUID);
        GUID72 parentGUID72 = new GUID72(parentGUID);
        this.serviceTreeService.addNodeToParent(nodeGUID72,parentGUID72);
        return "添加成功";
    }
}
