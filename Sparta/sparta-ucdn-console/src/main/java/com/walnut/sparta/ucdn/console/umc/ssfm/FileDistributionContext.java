package com.walnut.sparta.ucdn.console.umc.ssfm;

import java.io.File;
import java.io.IOException;

public class FileDistributionContext {
    private DefaultDistributionStrategy strategy;

    public FileDistributionContext(DefaultDistributionStrategy strategy) {
        this.strategy = strategy;
    }
    public void distributeFile(File file, String topic) throws IOException {
        strategy.distribute(file, topic);
    }
}
