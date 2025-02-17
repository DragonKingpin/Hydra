package com.walnut.sparta.ucdn.console.umc.ufmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExternalFileMultiDistributionService {

    void fileDistribution( File file, String topic ) throws IOException;
}
