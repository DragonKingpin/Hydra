package com.walnut.sparta.service.serviceImpl;

import com.walnut.sparta.entity.node;
import com.walnut.sparta.mapper.SystemMapper;
import com.walnut.sparta.service.SystemService;
import com.walnut.sparta.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SystemMapper systemMapper;
    @Override
    public void save_node(node node) {
        //生成uuid
        String uuid = UUIDUtil.createUUID();
        node.setUuid(uuid);
        systemMapper.save_node(node);
    }

    @Override
    public String delete_node(String uuid) {
        //检测节点是否存在
        node node = systemMapper.select_node_uuid(uuid);
        if (node==null){
            return "删除失败，节点不存在";
        }
        systemMapper.delete_node(uuid);
        return "删除成功";
    }
}
