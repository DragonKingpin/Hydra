package com.walnut.sparta.service.serviceImpl;

import com.walnut.sparta.entity.Node;
import com.walnut.sparta.mapper.SystemMapper;
import com.walnut.sparta.pojo.ServiceTree;
import com.walnut.sparta.service.SystemService;
import com.walnut.sparta.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SystemMapper systemMapper;
    @Autowired
    private ServiceTree serviceTree;
    @Override
    public void saveNode(Node node) {
        //生成uuid
        String uuid = UUIDUtil.createUUID();
        node.setUUID(uuid);
        systemMapper.saveNode(node);
    }

    @Override
    public String deleteNode(String uuid) {
        //检测节点是否存在
        Node node = systemMapper.selectNode(uuid);
        if (node==null){
            return "删除失败，节点不存在";
        }
        systemMapper.deleteNode(uuid);
        return "删除成功";
    }
}
