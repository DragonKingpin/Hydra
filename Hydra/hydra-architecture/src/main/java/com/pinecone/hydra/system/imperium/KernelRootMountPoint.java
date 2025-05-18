package com.pinecone.hydra.system.imperium;

public enum KernelRootMountPoint {
    Config                   ( "Config", KernelPrivyFileSystemConstants.FileSystemRoot + ".Config", "conf" ),
    Device                   ( "Device", KernelPrivyFileSystemConstants.FileSystemRoot + ".Device", "dev" ),
    UserHome                 ( "UserHome", KernelPrivyFileSystemConstants.FileSystemRoot + ".UserHome", "home" ),
    FileSystemMountDirectory ( "FileSystemMountDirectory", KernelPrivyFileSystemConstants.FileSystemRoot + ".MountDirectory", "mnt" ),
    SystemDirectory          ( "SystemDirectory", KernelPrivyFileSystemConstants.FileSystemRoot + ".SystemDirectory", "sys" ),
    Process                  ( "Process", KernelPrivyFileSystemConstants.FileSystemRoot + ".Process", "proc" ),
    Variable                 ( "Variable", KernelPrivyFileSystemConstants.FileSystemRoot + ".Variable", "var" ),
    Meta                     ( "Meta", KernelPrivyFileSystemConstants.FileSystemRoot + ".Meta", "meta" ),
    ;


    private final String name;

    private final String configSection;

    private final String mountPoint;

    KernelRootMountPoint( String name, String configSection, String mountPoint ) {
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
