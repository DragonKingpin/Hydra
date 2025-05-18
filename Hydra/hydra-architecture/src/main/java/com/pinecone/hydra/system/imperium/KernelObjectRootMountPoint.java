package com.pinecone.hydra.system.imperium;

public enum KernelObjectRootMountPoint {
    KernelConfig  ( "KernelConfig", KernelRootMountPoint.Config.getConfigSection() + ".Kernel", KernelRootMountPoint.Config.getMountPoint() + "/kernel" ),
    Registry      ( "Registry", KernelRootMountPoint.Config.getConfigSection() + ".Registry", KernelRootMountPoint.Config.getMountPoint() + "/registry" ),

    TaskMeta      ( "TaskMeta", KernelRootMountPoint.Meta.getConfigSection() + ".Task", KernelRootMountPoint.Meta.getMountPoint() + "/task" ),
    ServiceMeta   ( "ServiceMeta", KernelRootMountPoint.Meta.getConfigSection() + ".Service", KernelRootMountPoint.Meta.getMountPoint() + "/service" ),
    DeployMeta    ( "DeployMeta", KernelRootMountPoint.Meta.getConfigSection() + ".Deploy", KernelRootMountPoint.Device.getMountPoint() + "/deploy" ),
    ;


    private final String name;

    private final String configSection;

    private final String mountPoint;

    KernelObjectRootMountPoint( String name, String configSection, String mountPoint ) {
        this.name          = name;
        this.configSection = configSection;
        this.mountPoint    = mountPoint;
    }

    public String getName() {
        return this.name;
    }

    public String getConfigSection() {
        return this.configSection;
    }

    public String getMountPoint() {
        return this.mountPoint;
    }
}
