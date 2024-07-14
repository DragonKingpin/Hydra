package com.pinecone.framework.util.config;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Map;

public interface StartupCommandParser extends Pinenut {
    StartupCommandParser DefaultParser = new GenericStartupCommandParser();

    Map<String, String[] > parse( String[] args );
}
