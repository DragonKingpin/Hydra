package com.walnut.sparta.ucdn.console.umc.ssfm;

import com.walnut.sparta.ucdn.console.infrastructure.EFileContent;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class DefaultDistributionStrategy implements DistributionStrategy{

    private final SingleStreamFileMultiDistributionService distributionService;

    public DefaultDistributionStrategy(SingleStreamFileMultiDistributionService distributionService) {
        this.distributionService = distributionService;
    }
    @Override
    public void distribute(File file, String topic) throws IOException {
        distributionService.fileDistribution(file, topic);
    }
}
