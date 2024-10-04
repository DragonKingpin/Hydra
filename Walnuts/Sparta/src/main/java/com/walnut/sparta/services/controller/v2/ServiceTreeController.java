package com.walnut.sparta.services.controller.v2;

import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.ulf.util.id.GUID72;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.walnut.sparta.services.drivers.ServiceMasterTreeManipulatorImpl;
import com.walnut.sparta.system.BasicResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/serviceTree" )
public class ServiceTreeController {
    @Resource
    private ServiceMasterTreeManipulatorImpl treeManipulatorSharer;

    private DistributedTrieTree distributedTrieTree;

    @PostConstruct
    public void init() {
        this.distributedTrieTree = new GenericDistributedTrieTree( this.treeManipulatorSharer);
    }

    /**
     * 用于渲染路径信息
     * @param guid 节点UUID
     * @return 返回路径信息
     */
    @GetMapping("/getPath/{GUID}")
    public BasicResultResponse<String> getPath(@PathVariable("GUID") String guid){
        return BasicResultResponse.success( this.distributedTrieTree.getCachePath( new GUID72(guid) ) );
    }

    /**
     * 向指定父节点添加子节点
     * @param nodeGUID 子节点GUID
     * @param parentGUID 父节点GUID
     * @return 返回添加情况
     */
    @PostMapping("/addNodeToParent")
    public BasicResultResponse<String> addNodeToParent(@RequestParam("nodeGUID") String nodeGUID, @RequestParam("parentGUID") String parentGUID ){
        GUID72 nodeGUID72 = new GUID72(nodeGUID);
        GUID72 parentGUID72 = new GUID72(parentGUID);
        this.distributedTrieTree.affirmOwnedNode(nodeGUID72,parentGUID72);
        return BasicResultResponse.success();
    }
}
