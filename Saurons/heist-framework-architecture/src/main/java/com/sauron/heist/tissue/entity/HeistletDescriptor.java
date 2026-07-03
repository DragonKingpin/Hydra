package com.sauron.heist.tissue.entity;

import com.sauron.heist.heistron.Heistum;

public class HeistletDescriptor {
    private String                  mszName;
    private String                  mszClassName;
    private Class<? extends Heistum> mHeistletClass;
    private LoadedHeistJar          mLoadedHeistJar;

    public String getName() {
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    public String getClassName() {
        return this.mszClassName;
    }

    public void setClassName( String szClassName ) {
        this.mszClassName = szClassName;
    }

    public Class<? extends Heistum> getHeistletClass() {
        return this.mHeistletClass;
    }

    public void setHeistletClass( Class<? extends Heistum> heistletClass ) {
        this.mHeistletClass = heistletClass;
    }

    public LoadedHeistJar getLoadedHeistJar() {
        return this.mLoadedHeistJar;
    }

    public void setLoadedHeistJar( LoadedHeistJar loadedHeistJar ) {
        this.mLoadedHeistJar = loadedHeistJar;
    }
}
