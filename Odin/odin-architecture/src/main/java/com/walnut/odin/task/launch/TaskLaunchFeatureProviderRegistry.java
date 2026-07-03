package com.walnut.odin.task.launch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.troll.LaunchFeature;

public class TaskLaunchFeatureProviderRegistry implements Pinenut {

    protected List<TaskLaunchFeatureProvider> mProviders;

    public TaskLaunchFeatureProviderRegistry() {
        this.mProviders = new CopyOnWriteArrayList<>();
    }

    public void addProvider( TaskLaunchFeatureProvider provider ) {
        if ( provider == null || this.mProviders.contains( provider ) ) {
            return;
        }
        this.mProviders.add( provider );
    }

    public void addProviders( Iterable<TaskLaunchFeatureProvider> providers ) {
        if ( providers == null ) {
            return;
        }
        for ( TaskLaunchFeatureProvider provider : providers ) {
            this.addProvider( provider );
        }
    }

    public List<TaskLaunchFeatureProvider> providers() {
        return Collections.unmodifiableList( new ArrayList<>( this.mProviders ) );
    }

    public LaunchFeature apply( LaunchProvideTaskParam param, LaunchFeature feature ) {
        LaunchFeature current = feature == null ? new LaunchFeature() : feature;
        if ( param == null ) {
            return current;
        }

        for ( TaskLaunchFeatureProvider provider : this.mProviders ) {
            if ( provider == null || !provider.supports( param ) ) {
                continue;
            }

            LaunchFeature provided = provider.apply( param, current );
            if ( provided != null ) {
                current = provided;
            }
        }
        return current;
    }

}
