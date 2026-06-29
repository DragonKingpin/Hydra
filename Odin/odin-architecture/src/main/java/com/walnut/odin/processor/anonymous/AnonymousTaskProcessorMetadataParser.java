package com.walnut.odin.processor.anonymous;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface AnonymousTaskProcessorMetadataParser extends Pinenut {

    boolean isAnonymous( Map<String, String> metadata );

    String alias( Map<String, String> metadata );

    String bizPath( Map<String, String> metadata );

    String runtime( Map<String, String> metadata );

    Collection<String> execCaps( Map<String, String> metadata );
}
