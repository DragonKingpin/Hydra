package com.walnut.sparta.ucdn.console.domain.service;

import com.walnut.sparta.ucdn.console.infrastructure.dto.ClusterFileSyncDTO;

import java.io.File;
import java.io.IOException;

public interface NodeFileDistributionService {
    void upload( String path, File file, String topic ) throws IOException, InterruptedException;

    void testDistribution( String path, String topic ) throws IOException, InterruptedException;

    void clusterFileSync( ClusterFileSyncDTO dto ) throws IOException, InterruptedException;
}
