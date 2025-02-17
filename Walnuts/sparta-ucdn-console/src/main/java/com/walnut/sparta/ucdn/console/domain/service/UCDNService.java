package com.walnut.sparta.ucdn.console.domain.service;

import com.pinecone.hydra.umb.UMBServiceException;

import java.io.File;
import java.io.IOException;

public interface UCDNService {
    void upload( String path, File file, String topic ) throws IOException, InterruptedException;
    void test() throws UMBServiceException;
    void testDistribution( String path, String topic ) throws IOException, InterruptedException;

    void testEDdistribution( String path, String topic ) throws IOException;
}
