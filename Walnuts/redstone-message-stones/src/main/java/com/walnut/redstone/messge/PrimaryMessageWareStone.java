package com.walnut.redstone.messge;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;

public interface PrimaryMessageWareStone extends Pinenut {

    RuntimeSystem getSystem();

    Processum getParentProcess();

    DuplexAppointServer getWolfKingAppointServer();

    DuplexAppointClient getWolfAppointClient();

    UlfBroadcastControlNode getPrimaryKafkaClient();

    UlfBroadcastControlNode getPrimaryRocketClient();

}
