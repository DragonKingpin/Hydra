package com.pinecone.hydra.system.component;

import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.ConformitySystem;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;

public class LoggingConfigurator implements TracerConfigurator {

    protected org.slf4j.Logger log =  org.slf4j.LoggerFactory.getLogger( this.getClass() );

    protected ConformitySystem mConformitySystem;

    public LoggingConfigurator( ConformitySystem conformitySystem ) {
        this.mConformitySystem = conformitySystem;
    }

    @Override
    public void apply() {
        PatriarchalConfig tracer = this.mConformitySystem.getSystemConfig().getChild( "Tracer" );
        if ( tracer != null ) {
            PatriarchalConfig logging = tracer.getChild( "Logging" );
            if ( logging != null ) {
                PatriarchalConfig levels = logging.getChild( "Levels" );
                if ( levels instanceof JSONConfig ) {
                    JSONConfig joLevels = (JSONConfig) levels;
                    apply( joLevels );
                    log.info( "[Lifecycle] Set logging levels. <Done>" );
                }
            }
        }
    }

    public static void apply( Map<String, Object> levelMap ) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        for (Map.Entry<String, Object> entry : levelMap.entrySet()) {
            String loggerName = entry.getKey();
            String levelStr   = entry.getValue().toString();

            Level level = Level.toLevel(levelStr, Level.INFO);

            if ( "root".equalsIgnoreCase(loggerName) ) {
                context.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(level);
            }
            else {
                context.getLogger(loggerName).setLevel(level);
            }
        }
    }
}