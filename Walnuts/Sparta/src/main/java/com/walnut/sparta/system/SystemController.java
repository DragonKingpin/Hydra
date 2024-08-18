package com.walnut.sparta.system;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.pojo.ApplicationNodeInformation;
import com.walnut.sparta.pojo.ClassifNodeInformation;
import com.walnut.sparta.pojo.ServiceNodeInformation;
import com.walnut.sparta.pojo.DistributedScopeTree;
import com.walnut.sparta.service.SystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping( "/system" )
public class SystemController {
    //    @GetMapping( "/undefined" )
//    public String undefined() {
//        return "Hello, hi, good afternoon! This is undefined specking!";
//    }
    @Resource
    DistrubuteScopeTreeDataManipinate distrubuteScopeTreeDataManipinate;
    @Resource
    ServiceTreeMapper serviceTreeMapper;
    //node类的CRUD操作

    /**
     * 保存应用节点
     * @param applicationNodeInformation 应用节点信息
     */

//    @PostMapping("/saveApplicationNode")
//   public void saveApplicationNode(@RequestBody ApplicationNodeInformation applicationNodeInformation){
//        DistributedScopeTree distributedScopeTree = new DistributedScopeTree();
//        System.out.println(JSON.stringify(applicationNodeInformation));
//        distributedScopeTree.saveApplicationNode(applicationNodeInformation);
//   }
//
//    /**
//     * 保存服务节点
//     * @param serviceNodeInformation 服务节点信息
//     */
//   @PostMapping("/saveServiceNode")
//   public void saveServiceNode(@RequestBody ServiceNodeInformation serviceNodeInformation){
//       this.distributedScopeTree.saveServiceNode(serviceNodeInformation);
//   }
//
//    /**
//     * 保存分类节点
//     * @param classifNodeInformation 分类节点信息
//     */
//    @PostMapping("/saveClassifNode")
//   public void saveClassifNode(@RequestBody ClassifNodeInformation classifNodeInformation){
//        this.distributedScopeTree.saveClassifNode(classifNodeInformation);
//   }
//
//    /**
//     * 删除节点
//     * @param UUID 节点UUID
//     * @return 返回删除情况
//     */
//   @DeleteMapping("/deleteNode/{UUID}")
//    public String deleteNode(@PathVariable("UUID") String UUID){
//       this.distributedScopeTree.deleteNode(UUID);
//       return "删除成功";
//   }
//
//    /**
//     * 获取单节点信息
//     * @param UUID 单节点的UUID
//     * @return 返回单节点信息
//     */
//    @GetMapping("/selectNode/{UUID}")
//   public String selectNode(@PathVariable("UUID") String UUID){
//        Object Node = this.distributedScopeTree.selectNode(UUID);
//        return JSON.stringify(Node);
//    }

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
}
