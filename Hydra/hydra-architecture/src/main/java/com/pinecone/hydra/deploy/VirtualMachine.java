package com.pinecone.hydra.deploy;

public interface VirtualMachine extends Server {

    PhysicalHost getAffiliateHost();

}
