package com.walnut.sparta.ucdn.console.domain.service.cluster;

import javax.annotation.Resource;

public class ClusterFileSynchronizationServiceImpl implements ClusterFileSynchronizationService {
    @Resource
    private UFMTransactionSynchronizedNotifier webSocketService;

    private ClusterFileTransactionManager      transactionManager;
}
