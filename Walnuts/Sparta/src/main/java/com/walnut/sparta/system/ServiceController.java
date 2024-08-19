package com.walnut.sparta.system;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.pojo.DistributedScopeTree;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/service" )
public class ServiceController {
    @Resource
    DistrubuteScopeTreeDataManipinate distrubuteScopeTreeDataManipinate;
    @Resource
    ServiceTreeMapper serviceTreeMapper;

    /**
     * 用于渲染路径信息
     * @param UUID 节点UUID
     * @return 返回路径信息
     */
    @GetMapping("/getPath/{UUID}")
    public String getPath(@PathVariable("UUID") String UUID){
        DistributedScopeTree distributedScopeTree = new DistributedScopeTree(this.serviceTreeMapper,this.distrubuteScopeTreeDataManipinate);
        GUID72 guid72 = new GUID72();
        GUID parse = guid72.parse(UUID);
        return distributedScopeTree.getPath(parse);
    }

    /**
     * 渲染单节点信息
     * @param UUID 节点UUID
     * @return 返回节点信息
     */
    @GetMapping("/selectNode/{UUID}")
    public NodeInformation selectNode(@PathVariable("UUID") String UUID){
        DistributedScopeTree distributedScopeTree = new DistributedScopeTree(this.serviceTreeMapper,this.distrubuteScopeTreeDataManipinate);
        GUID72 guid72 = new GUID72();
        GUID parse = guid72.parse(UUID);
        return distributedScopeTree.selectNode(parse);
    }

    /**
     * 用于将路径反解析为节点信息
     * @param path 节点路径
     * @return 返回节点信息
     */
    @GetMapping("/parsePath/{path}")
    public NodeInformation parsePath(@PathVariable("path") String path){
        DistributedScopeTree distributedScopeTree = new DistributedScopeTree(this.serviceTreeMapper,this.distrubuteScopeTreeDataManipinate);
        return distributedScopeTree.parsePath(path);
    }
}
