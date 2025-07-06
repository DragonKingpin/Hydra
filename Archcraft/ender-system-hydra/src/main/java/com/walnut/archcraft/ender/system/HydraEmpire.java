package com.walnut.archcraft.ender.system;

import com.acorn.redqueen.system.RedQueenSubsystem;
import com.acorn.skynet.system.SkynetSubsystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.hydra.proc.InstitutionalProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.ProcessManagerSystema;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.system.centrum.Centrum;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.types.HydraKingdom;

public interface HydraEmpire extends Centrum, HydraKingdom, Slf4jTraceable, InstitutionalProcess, ProcessManagerSystema {

    ProcessManager processManager();

    ImageLoader imageLoader();

    VirtualExeImageInstrument virtualExeImageInstrument();

    RedQueenSubsystem redQueen();

    SkynetSubsystem skynet();

    Lord getEmpireLordsByName( String lordName );

    int countEmpireLords();

}
