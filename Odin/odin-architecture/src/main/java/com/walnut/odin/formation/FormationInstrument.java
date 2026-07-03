package com.walnut.odin.formation;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.service.FlowService;
import com.walnut.odin.formation.service.RunService;
import com.walnut.odin.formation.source.MasterManipulator;

public interface FormationInstrument extends Instrument {

    FormationConfig formationConfig();

    GuidAllocator guidAllocator();

    MasterManipulator masterManipulator();

    RunService runService();

    FlowService flowService();
}
