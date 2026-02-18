package com.sauron.heist.heistron;

import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.sauron.heist.heistron.event.HeistLifecycleEventInterceptor;
import com.sauron.heist.heistron.orchestration.HeistletOrchestrator;
import com.pinecone.framework.system.executum.ExclusiveProcessum;
import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.heist.heistron.orchestration.Hierarchy;

public interface Heistgram extends ExclusiveProcessum, Servgramium {
    Hydrogen parentSystem();

    JSONConfig getTemplateHeistSchemeConfig();

    JSONConfig getLocalHeistsConfigList();

    HeistletOrchestrator getHeistletOrchestrator();

    JSONConfig queryHeistConfig ( String szHeistName );

    JSONConfig getComponentsConfig();

    String searchHeistName( Heistum that ) ;

    Heistgram addLifecycleEventInterceptors( HeistLifecycleEventInterceptor interceptor );

    Heistgram removeLifecycleEventInterceptors( HeistLifecycleEventInterceptor interceptor );

    void notifyLifecycleEvent(Heistum heist, TaskInstanceStatus instanceStatus, Hierarchy hierarchy );
}
