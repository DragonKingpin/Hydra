package com.walnut.sparta.service;

import com.walnut.sparta.entity.node;

public interface SystemService {
    void save_node(node node);
     String delete_node(String uuid);
}
