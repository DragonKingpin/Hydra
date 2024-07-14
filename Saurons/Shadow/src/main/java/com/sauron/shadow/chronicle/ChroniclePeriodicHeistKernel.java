package com.sauron.shadow.chronicle;

import com.sauron.radium.heistron.Heistum;
import com.sauron.radium.heistron.chronic.ArchPeriodicHeistRehearsal;

public class ChroniclePeriodicHeistKernel extends ArchPeriodicHeistRehearsal {
    public ChroniclePeriodicHeistKernel( Heistum heistum, boolean bDaemon ) {
        super( heistum, bDaemon );
    }

    public ChroniclePeriodicHeistKernel( Heistum heistum ) {
        this( heistum, false );
    }
}
