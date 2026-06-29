package com.acorn.redqueen.service.deletion;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ServiceDirectoryDeleteService extends Pinenut {

    ServiceDirectoryDeleteSafetyReport checkDirectoryDelete( GUID guid );

    ServiceDirectoryDeleteSafetyReport checkDirectoryDelete( String path );

    ServiceDirectoryDeleteSafetyReport checkDirectoryPurge( GUID guid );

    ServiceDirectoryDeleteSafetyReport checkDirectoryPurge( String path );

    ServiceDirectoryDeleteSafetyReport purgeDirectory( GUID guid );

    ServiceDirectoryDeleteSafetyReport purgeDirectory( String path );

}
