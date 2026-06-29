package com.acorn.redqueen.service.purge;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface PurgeService extends Pinenut {
    PurgeSafetyReport check( String serviceIdentifier );

    PurgeSafetyReport purge( String serviceIdentifier );

    void shutdownInstance( Identification instanceId, String reason ) throws PurgeException;
}
