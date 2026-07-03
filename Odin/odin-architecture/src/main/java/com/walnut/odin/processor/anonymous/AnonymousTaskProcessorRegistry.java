package com.walnut.odin.processor.anonymous;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface AnonymousTaskProcessorRegistry extends Pinenut {

    AnonymousTaskProcessorRegistration register( String szNodeName, long nClientId, Map<String, String> metadata );

    AnonymousTaskProcessorRegistration unregister( long nClientId );

    AnonymousTaskProcessorRegistration getByClientId( long nClientId );

    Collection<AnonymousTaskProcessorRegistration> snapshot();
}
