package com.pinecone;


import com.pinecone.framework.system.Framework;
import com.pinecone.framework.system.functions.Function;
import com.pinecone.framework.util.io.Tracer;

import java.io.InputStream;
import java.io.PrintStream;

/**
 *  Pinecone Framework For Java (Bean Nuts Pinecone Ursus for Java)
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  Open Source licensed under the GPL.
 *  *****************************************************************************************
 *  Other information about this framework, such as papers, patents, etc -> http://www.rednest.cn
 *  Warning: This source code is protected by copyright law and international treaties.
 *  *****************************************************************************************
 *  www.nutgit.com/ www.xbean.net / www.rednest.cn
 *  Include Almond, C/CPP, JAVA, PHP, Python, JavaScript, ActionScript, GoLang
 *  *****************************************************************************************
 *  ;) Hope you enjoy this | Dragon King, the undefined
 */
public class Pinecone {
    public static final long        VER_PINE               =  202506L;
    public static final String      VERSION                = "2.5.1";
    public static final String      RELEASE_DATE           = "2025/06/06";
    public static final String      ROOT_SERVER            = "http://www.rednest.cn/";
    public static final String      MY_PROGRAM_NAME        = "Pinecone";
    public static final String      CONTACT_INFO           = "E-Mail:arb#rednest.cn";
    public static final boolean     S_DEBUG_MODE           = true;
    public static final int         FLOAT_ACCURACY         = 32;
    public static final int         COMMON_ACCURACY_LIMIT  = 10000;


    public static final Framework   PRIME_SYSTEM           = new Framework();

    public static int init ( Function fnInlet, Object...args ) throws Exception {
        return Pinecone.PRIME_SYSTEM.init( fnInlet, args );
    }

    public static Framework sys(){
        return Pinecone.PRIME_SYSTEM;
    }

    public static Tracer console() {
        return Pinecone.sys().console();
    }

    public static PrintStream out() {
        return Pinecone.console().getOut();
    }

    public static PrintStream err() {
        return Pinecone.console().getOut();
    }

    public static InputStream in() {
        return Pinecone.sys().in();
    }

}

