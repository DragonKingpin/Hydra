package com.walnut.sparta.ucdn.service.umct;

import com.pinecone.framework.util.Debug;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FileSyncDistributionImpl implements FileSyncDistribution {
    @Override
    public void dino( String name ) {
        Debug.greenf( "I am a cute dino " + name );
    }
}
