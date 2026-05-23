package com.pinecone.meson;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.config.JSONSystemConfig;
import com.pinecone.framework.util.io.Tracerson;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.system.mold.DrakePanarchy;
import com.pinecone.hydra.system.mold.Lepton;

/**
 *  Bean Nuts Pinecone Meson
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Drake - Drake Hypha - Mold Architecture - Block / Edge / Decentralized Architecture
 *  Drake - 小龙菌丝网络 - 霉菌架构 - 弱节点/去中心化架构
 *  *****************************************************************************************
 *  Drake      | 小龙 | Meta Frame   | 框架层元原型
 *  Lepton     | 轻子 | Prototype    | 系统实现原型接口
 *  Meson      | 介子 | Archetype    | 系统实现典型抽象
 *  *****************************************************************************************
 *  DragonKing.cn of Harald
 */
public abstract class Meson extends DrakePanarchy implements Lepton {
    public static final String  NUTLET_NAME         = "Bean Nuts Hazelnut Sauron Drake";
    public static final long    VER_PINE            =  202806L;
    public static final String  VERSION             = "2.1.0";
    public static final String  RELEASE_DATE        = "2028/06/06";
    public static final String  ROOT_SERVER         = "https://www.dragonking.cn/";
    public static final String  CONTACT_INFO        = "E-Mail:info#dragonking.cn";

    public static final String  SYSTEM_PATH         = "./system/";
    public static final String  SETUP_PATH          = Meson.SYSTEM_PATH + "setup/";
    public static final String  MAIN_CONFIG_FILE    = Meson.SETUP_PATH + "config.json5";


    protected Logger                               mLogger;
    protected Path                                 mPrimaryConfigPath;
    protected DynamicFactory                       mShardDynamicFactory;

    protected void prepare_meson_before() {

    }

    protected void prepare_meson() {

    }

    protected void prepare_meson_after() {

    }

    @Override
    protected void init() {
        super.init();

        this.prepare_meson_before();
        this.prepare_meson();
        this.prepare_meson_after();
    }

    protected void prepare_system_log4j_logger() {
        this.mLogger = LoggerFactory.getLogger( this.className() + "<PrimarySystem>" );
        this.pout().print( "[System] [TracerReassignment] <Transfer console -> Slf4j>\n" );
    }

    protected String fristQualifiedConfigPathString() {
        return MAIN_CONFIG_FILE;
    }

    @Override
    protected void loadConfig() {
        try {
            Map<String, String> map = this.getStartupCommandMap();
            String workingPath = map.get( "workingPath" );
            if ( workingPath != null && !workingPath.isEmpty() ) {
                this.setRuntimePath( Path.of( workingPath ).toString() );
            }

            String configPath = map.get( "config" );
            if ( configPath != null && !configPath.isEmpty() ) {
                this.mPrimaryConfigPath = Path.of( configPath );
            }
            else {
                this.mPrimaryConfigPath = Path.of( this.getRuntimePath() ).resolve( this.fristQualifiedConfigPathString() );
            }

            this.mjoGlobalConfig = (JSONSystemConfig) ( new JSONSystemConfig( this ) ).apply( this.mPrimaryConfigPath.toFile() );
            this.mjoSystemConfig = this.mjoGlobalConfig.getChild( "System" );
        }
        catch ( IOException e ) {
            super.loadConfig();
        }
    }

    protected void loadTracer() {
        this.mConsole = new Tracerson();
    }

    @Override
    protected void onlyLoadTaskManager() {
        super.onlyLoadTaskManager();
        this.mShardDynamicFactory = new GenericDynamicFactory( this.mTaskManager.getClassLoader() );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public Meson infoLifecycle( String szWhat, String szStateOrExtra ) {
        this.getLogger().info( "[SystemLifecycle] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }

    protected void traceSystemBootingInfo() {
        this.pout().print( "\u001B[34m>>> System Booting...\u001B[0m\n\n" );
    }

    protected void traceSubsystemWelcomeInfo() {
        this.pout().print( "---------------------------------------------------------------\n" );
    }

    protected void traceSystemInfo() {
        LocalDateTime now = LocalDateTime.now();
        this.console().echo( "----------------------System Information-----------------------\n" );
        this.console().echo( "NutletName : " + Meson.NUTLET_NAME, "\n" );
        this.console().echo( "Version    : " + Meson.VERSION, "\n" );
        this.console().echo( "ReleaseDate: " + Meson.RELEASE_DATE, "\n" );
        this.console().echo( "RuntimePath: " + this.getRuntimePath(), "\n" );
        this.console().echo( "ContextPath: " + this.getRuntimeContextPath(), "\n" );
        this.console().echo( "PrimaryConf: " + ( this.mPrimaryConfigPath == null ? "N/A" : this.mPrimaryConfigPath.toString() ), "\n" );
        this.console().echo( "StartTime  : " + now.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss:SSS" ) ), "\n" );
        this.console().echo( "---------------------------------------------------------------\n" );
    }

    @Override
    protected void traceWelcomeInfo() {
        this.pout().print( "---------------------------------------------------------------\n" );
        this.pout().print( "\u001B[31mBean Nuts Pinecone Ursus for Java\u001B[0m\n" );
        this.pout().print( "\u001B[31mDrake Hypha Framework (Meson, Drake Panarchy) \u001B[0m\n" );
        this.pout().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        this.pout().print( "---------------------------------------------------------------\n" );
        this.pout().print( "\u001B[31mDragon King\u001B[0m\n" );
        this.pout().print( "\u001B[32mWebsite: https://www.dragonking.cn/ \u001B[0m\n" );

        this.traceSubsystemWelcomeInfo();
        this.traceSystemBootingInfo();
        this.prepare_system_log4j_logger();
        this.infoLifecycle( "Initialization", LogStatuses.StatusStart );
    }

    public Meson(){
        this( new String[0], null, null );
    }

    public Meson( String[] args ){
        this( args, null, null );
    }

    public Meson( String[] args, String szName ){
        this( args, szName, null );
    }

    public Meson( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Meson( String[] args, String szName, CascadeSystem parent ) {
        super( args, szName, parent );

        this.loadTracer();
        this.dispatchStartupCommand();
        this.traceSystemInfo();
        this.infoLifecycle( "Initialization", LogStatuses.StatusReady );
        this.console().echo( "\n\n" );
        this.console().getOut().flush();
    }

    @Override
    public Path getPrimaryConfigsPath() {
        return Path.of( this.getRuntimePath() ).resolve( Meson.SETUP_PATH );
    }

    @Override
    public Path getPrimaryConfigPath() {
        return this.mPrimaryConfigPath;
    }

    @Override
    public DynamicFactory getShardDynamicFactory() {
        return this.mShardDynamicFactory;
    }

}
