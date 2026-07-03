package com.walnut.odin.formation.dispatch;

import com.pinecone.framework.system.regime.Executioner;
import com.walnut.odin.formation.dto.FormationDispatcherRuntimeSnapshot;

public interface FormationDispatcher extends Executioner {

    void startup();

    void shutdown();

    boolean offer( Runnable command );

    FormationDispatcherRuntimeSnapshot retrieveRuntimeSnapshot();
}
