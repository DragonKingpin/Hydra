package com.walnut.sailor.stream.fm;

import java.io.File;
import java.io.IOException;

public interface SingleStreamFileMultiDistributionService {

    void fileDistribution( File file, String topic ) throws IOException;

}
