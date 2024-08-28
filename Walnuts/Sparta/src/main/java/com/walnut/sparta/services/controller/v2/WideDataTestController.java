package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.DistributedScopeServiceTree;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.wideData.GenericWideTableFactory;
import com.pinecone.hydra.service.tree.wideData.NodeWideTable;
import com.pinecone.hydra.service.tree.wideData.WideTableFactory;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.system.BasicResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/test" )
public class WideDataTestController {
    @Resource
    DefaultMetaNodeManipulator  defaultMetaNodeManipulator;

    WideTableFactory wideTableFactory ;
    @PostConstruct
    public void init() {
        this.wideTableFactory =new GenericWideTableFactory(this.defaultMetaNodeManipulator);
    }

    /**
     * 渲染单节点信息
     * @param guid 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public BasicResultResponse<Object> queryNodeInfoByGUID(@PathVariable("GUID") String guid ){
        GUID72 guid72 = new GUID72( guid );
        ScopeTreeManipulator scopeTreeManipulator = this.defaultMetaNodeManipulator.getScopeTreeManipulator();
        GUIDDistributedScopeNode node = scopeTreeManipulator.getNode(guid72);
        UOI type = node.getType();
        NodeWideTable uniformObjectWideTable = wideTableFactory.getUniformObjectWideTable(type.getObjectName());
        System.out.println(uniformObjectWideTable.get(guid72));
        return BasicResultResponse.success("成功");
    }
}
