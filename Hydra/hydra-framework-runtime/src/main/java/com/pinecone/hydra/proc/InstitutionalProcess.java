package com.pinecone.hydra.proc;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;

/**
 *  Pinecone Ursus For Java, InstitutionalProcess
 *  Author: Harald.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Institutional Uniform Process
 *  体制化统一进程
 *  *****************************************************************************************
 *  1). Processum => Local Process, managed under local-first autonomy and jurisdictional control.
 *  2). UProcess => Uniform Process,
 *      centrally constituted and managed by unified authority, with reserved central control rights.
 *  *****************************************************************************************
 *  1). Processum => Local Process, 地方本地进程，由地方优先自治、管制
 *  2). UProcess => Uniform Process, 中央编制的统一进程，由中央统一权威管制，拥有保留的中央控制权
 *  *****************************************************************************************
 */
public interface InstitutionalProcess extends Processum {

    default Processum ownedLocalProcess() {
        return this;
    }

    UProcess ownedUniformProcess();

    default GUID getPID() {
        return this.ownedUniformProcess().getPID();
    }

}
