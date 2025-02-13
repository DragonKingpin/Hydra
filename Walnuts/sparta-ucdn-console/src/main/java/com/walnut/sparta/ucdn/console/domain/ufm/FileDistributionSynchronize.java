package com.walnut.sparta.ucdn.console.domain.ufm;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public interface FileDistributionSynchronize extends Pinenut {
    void distributionCallBack( String path ) throws IOException;
}
