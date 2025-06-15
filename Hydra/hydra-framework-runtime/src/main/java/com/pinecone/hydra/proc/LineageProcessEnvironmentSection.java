package com.pinecone.hydra.proc;

import java.util.HashMap;
import java.util.Map;

public class LineageProcessEnvironmentSection implements ProcessEnvironmentSection {
    protected Map<String, String[]> mSystemEnvironments;

    public LineageProcessEnvironmentSection(Map<String, String[]> systemEnvironmentVars ) {
        this.mSystemEnvironments = systemEnvironmentVars;
    }

    @Override
    public Map<String, String[]> getSystemEnvironments() {
        return this.mSystemEnvironments;
    }

    @Override
    public Map<String, String[]> extendsFrom( final Map<String, String[]> superiorEnvironmentVars, final Map<String, String[]> contextEnvVars ) {
        Map<String, String[]> neo = new HashMap<>( this.mSystemEnvironments );
        neo.putAll( superiorEnvironmentVars );
        if ( contextEnvVars != null ) {
            neo.putAll( contextEnvVars );
        }
        return neo;
    }

    @Override
    public Map<String, String[]> extendsFrom( UProcess superiorProcess, final Map<String, String[]> contextEnvVars ) {
        return this.extendsFrom( superiorProcess.getEnvironmentVariables(), contextEnvVars );
    }

}
