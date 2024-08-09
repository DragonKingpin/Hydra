package com.walnut.sparta.service;

import com.walnut.sparta.entity.Node;

public interface SystemService {
    void saveNode(Node node);
     String deleteNode(String uuid);
}
