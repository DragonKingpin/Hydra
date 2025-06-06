package com.pinecone.hydra.proc;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ProcessEnvironmentSection extends Pinenut {

    Map<String, String[]> getSystemEnvironments();

    Map<String, String[]> extendsFrom( Map<String, String[]> superiorEnvironmentVars, Map<String, String[]> contextEnvVars );

    default Map<String, String[]> extendsFrom( Map<String, String[]> superiorEnvironmentVars ) {
        return this.extendsFrom( superiorEnvironmentVars, null );
    }

    Map<String, String[]> extendsFrom( UProcess superiorProcess, Map<String, String[]> contextEnvVars );

    default Map<String, String[]> extendsFrom( UProcess superiorProcess ) {
        return this.extendsFrom( superiorProcess, null );
    }

}
