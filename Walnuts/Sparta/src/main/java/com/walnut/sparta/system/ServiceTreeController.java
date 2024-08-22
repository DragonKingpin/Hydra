package com.walnut.sparta.system;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericServiceNode;
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
import com.walnut.sparta.pojo.DistributedScopeService;
import com.walnut.sparta.pojo.DistributedScopeTree;
import com.walnut.sparta.service.ServiceTreeService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/serviceTree" )
public class ServiceTreeController {
    @Resource
    ServiceTreeMapper serviceTreeMapper;
    @Resource
    GenericApplicationNodeManipinate genericApplicationNodeManipinate;
    @Resource
    GenericClassifNodeManipinate genericClassifNodeManipinate;
    @Resource
    GenericServiceNodeManipinate genericServiceNodeManipinate;
    @Resource
    ServiceTreeService serviceTreeService;

    /**
     * 用于渲染路径信息
     * @param GUID 节点UUID
     * @return 返回路径信息
     */
    @GetMapping("/getPath/{GUID}")
    public String getPath(@PathVariable("GUID") String GUID){
        DistributedScopeTree distributedScopeTree = new DistributedScopeTree(this.serviceTreeMapper,this.genericApplicationNodeManipinate,this.genericServiceNodeManipinate,this.genericClassifNodeManipinate);
        GUID72 guid72 = new GUID72();
        GUID parse = guid72.parse(GUID);
        Debug.trace( parse.toString() );
        return distributedScopeTree.getPath(parse);
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

    /**
     * 删除节点
     * @param nodeGUID 节点GUID
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode")
    public String deleteNode(@RequestParam("nodeGUID") String nodeGUID){
        this.serviceTreeService.deleteNode(new GUID72(nodeGUID));
        return "删除成功";
    }
}
