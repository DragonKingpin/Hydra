package com.walnut.sparta.ucdn.console.umc.ssfm;

import java.io.File;
import java.io.IOException;

public interface DistributionStrategy {
    void distribute(File file, String topic) throws IOException;
}
