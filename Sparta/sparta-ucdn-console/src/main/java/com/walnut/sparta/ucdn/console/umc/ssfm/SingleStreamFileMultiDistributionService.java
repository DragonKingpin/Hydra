package com.walnut.sparta.ucdn.console.umc.ssfm;

import java.io.File;
import java.io.IOException;

public interface SingleStreamFileMultiDistributionService {

    void fileDistribution( File file, String topic ) throws IOException;
}
