package com.walnut.sparta.services.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.ScopeServiceTree;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.wideData.GenericWideTableFactory;
import com.pinecone.hydra.service.tree.wideData.NodeWideData;
import com.pinecone.hydra.service.tree.wideData.NodeWideTable;
import com.pinecone.hydra.service.tree.wideData.WideTableFactory;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.ulf.util.id.GUID72;
import com.pinecone.hydra.service.tree.DistributedScopeServiceTree;
import com.walnut.sparta.system.BasicResultResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping( "/api/v2/serviceNode" )
public class ServiceMetaController {
    @Resource
    private DefaultMetaNodeManipulator defaultMetaNodeManipulator;

    private ScopeServiceTree scopeServiceTree;

    WideTableFactory wideTableFactory ;

    @PostConstruct
    public void init() {
        this.scopeServiceTree = new DistributedScopeServiceTree( this.defaultMetaNodeManipulator);
        this.wideTableFactory =new GenericWideTableFactory(this.defaultMetaNodeManipulator);
    }

    /**
     * 渲染单节点信息
     * @param guid 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByGUID/{GUID}")
    public BasicResultResponse<ServiceTreeNode> queryNodeInfoByGUID(@PathVariable("GUID") String guid ){
        GUID72 guid72 = new GUID72( guid );
        return BasicResultResponse.success(this.scopeServiceTree.getNode( guid72 ));
    }

    /**
     * 用于将路径反解析为节点信息
     * @param path 节点路径
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeInfoByPath")
    public ServiceTreeNode queryNodeInfoByPath( @RequestParam("path") String path ){
        return this.scopeServiceTree.parsePath(path);
    }

    /**
     * 创建一个服务节点
     * @param serviceNode 服务节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveServiceNode")
    public GUID saveServiceNode( @RequestBody GenericServiceNode serviceNode ){
        return this.scopeServiceTree.addNode( serviceNode );
    }

    /**
     * 创建一个应用节点
     * @param applicationNode 应用节点信息
     * @return  创建的节点的GUID
     */
    @PostMapping("/saveApplicationNode")
    public GUID saveApplicationNode( @RequestBody GenericApplicationNode applicationNode ){
        return this.scopeServiceTree.addNode(applicationNode);
    }

    /**
     * 创建一个分类节点
     * @param classificationNode 分类节点信息
     * @return 创建的节点的GUID
     */
    @PostMapping("/saveClassifNode")
    public GUID saveClassifNode( @RequestBody GenericClassificationNode classificationNode ){
        return this.scopeServiceTree.addNode(classificationNode);
    }

    /**
     * 删除节点
     * @param guid 节点的guid
     * @return 返回删除情况
     */
    @DeleteMapping("/removeNode")
    public BasicResultResponse<String> removeNode(@RequestParam("nodeGUID") String guid){
        this.scopeServiceTree.removeNode( new GUID72( guid ) );
        return BasicResultResponse.success("删除成功");
    }
    /**
     * 渲染单节点所有信息（含继承）
     * @param guid 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/queryNodeWideInfo/{GUID}")
    public BasicResultResponse<NodeWideData> queryNodeWideInfo(@PathVariable("GUID") String guid ){
        GUID72 guid72 = new GUID72( guid );
        ScopeTreeManipulator scopeTreeManipulator = this.defaultMetaNodeManipulator.getScopeTreeManipulator();
        GUIDDistributedScopeNode node = scopeTreeManipulator.getNode(guid72);
        UOI type = node.getType();
        NodeWideTable uniformObjectWideTable = wideTableFactory.getUniformObjectWideTable(type.getObjectName());
        return BasicResultResponse.success(uniformObjectWideTable.get(guid72));
    }

    /**
     * 删除节点（完全移除）
     * @param guid 节点的guid
     * @return 返回移除结果
     */
    @DeleteMapping("/remove")
    public BasicResultResponse<String> remove(@RequestParam("nodeGUID") String guid){
        GUID72 guid72 = new GUID72( guid );
        ScopeTreeManipulator scopeTreeManipulator = this.defaultMetaNodeManipulator.getScopeTreeManipulator();
        GUIDDistributedScopeNode node = scopeTreeManipulator.getNode(guid72);
        UOI type = node.getType();
        NodeWideTable uniformObjectWideTable = wideTableFactory.getUniformObjectWideTable(type.getObjectName());
        uniformObjectWideTable.remove(guid72);
        return BasicResultResponse.success("删除成功");
    }
}
