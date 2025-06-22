package com.walnut.archcraft.ender.system;

import com.pinecone.hydra.proc.InstitutionalProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.system.centrum.Centrum;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.types.HydraKingdom;

public interface HydraEmpire extends Centrum, HydraKingdom, Slf4jTraceable, InstitutionalProcess {

    ProcessManager processManager();

    ImageLoader imageLoader();

    VirtualExeImageInstrument virtualExeImageInstrument();

}
