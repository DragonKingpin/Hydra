package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;

import java.util.Map;

@Iface
public interface MasterProcessLifecycleIface extends Pinenut {

    void startRemoteUProcess( String processId );

    RemoteVitalizationResponse vitalizeRemoteUProcess( String imageAddress, boolean isURI, UProcessHandlerDTO handlerDTO );

}
