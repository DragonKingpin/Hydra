package com.walnut.sparta.uis.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.summer.spring.Springron;
import org.slf4j.Logger;

public class SpartaUISService extends Springron implements UISService{
    public SpartaUISService(String szName, Processum parent, String[] springbootArgs) {
        super(szName, parent, springbootArgs);
    }

    public SpartaUISService(String szName, Processum parent) {
        super(szName, parent);
    }

    @Override
    public Logger getLogger() {
        return null;
    }
}
