package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sparta.ucdn.console.domain.ufm.UFMDClusterFrame;
import com.walnut.sparta.ucdn.console.domain.ufm.UFMDClusterDO;

@Iface
public interface FileDistribution {
    void setFileMeta(String path,long definitionSize);

    void setFrameMeta(UFMDClusterDO frameMeta);

    void saveFrameContent(UFMDClusterFrame contentVO );

    void frameEnd( String path, long segId );
}
