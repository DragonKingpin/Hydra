package com.pinecone.framework.system;

import com.pinecone.framework.system.executum.Lifecycle;
import com.pinecone.framework.system.executum.Systemum;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.SystemConfig;

import java.util.Map;

public interface RuntimeSystem extends Pinenut, Systemum, Lifecycle {
    String[]                   getStartupCommand();

    Map<String, String[] >     getStartupCommandMap();

    SystemConfig               getGlobalConfig();

    String                     getMajorPackagePath();

    String                     getRuntimeContextPath();

    String                     getRuntimePath();

    void                       setRuntimePath( String szRealRuntimePath );

    ClassLoader                getGlobalClassLoader();

    void                       setGlobalClassLoader( ClassLoader classLoader );

    void                       handleLiveException( Exception e ) throws ProvokeHandleException;

    default void               handleKillException( Exception e ) throws ProvokeHandleException {
        throw new ProvokeHandleException( e );
    }

    default void               handleIgnoreException( Exception e ) throws ProvokeHandleException {
        // Just ignore them.
    }

    /**
     * Those Exceptions should never happened.
     */
    default void               handleDummyException( Exception e ) throws ProvokeHandleException {
        throw new ProvokeHandleException( e );
    }

}
