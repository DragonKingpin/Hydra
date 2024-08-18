//package com.walnut.sparta.service.serviceImpl;
//
//import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
//import com.walnut.sparta.mapper.ServiceNodeMapper;
//import com.walnut.sparta.pojo.DistributedScopeTree;
//import com.walnut.sparta.service.SystemService;
//import com.walnut.sparta.utils.UUIDUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SystemServiceImpl implements SystemService {
//    @Autowired
//    private ServiceNodeMapper serviceNodeMapper;
//    @Autowired
//    private DistributedScopeTree distributedScopeTree;
//    @Override
//    public void saveNode(GUIDDistributedScopeNode node) {
//        //生成uuid
//        String uuid = UUIDUtil.createUUID();
//        node.setUUID(uuid);
//        serviceNodeMapper.saveNode(node);
//    }
//
//    @Override
//    public String deleteNode(String uuid) {
//        //检测节点是否存在
//        GUIDDistributedScopeNode node = serviceNodeMapper.selectNode(uuid);
//        if (node==null){
//            return "删除失败，节点不存在";
//        }
//        serviceNodeMapper.deleteNode(uuid);
//        return "删除成功";
//    }
//}
