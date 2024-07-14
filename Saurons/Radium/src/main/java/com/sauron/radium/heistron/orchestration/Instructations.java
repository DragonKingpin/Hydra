package com.sauron.radium.heistron.orchestration;

import com.pinecone.hydra.auto.Instructation;
import com.sauron.radium.heistron.Heistum;
import com.sauron.radium.heistron.chronic.Raider;

public final class Instructations {
    public static void infoLifecycle( Heistum heistum, String szWhat, String szStateOrExtra ) {
        heistum.tracer().info( "[Lifecycle] [{}] <{}>", szWhat, szStateOrExtra );
    }

    public static void infoConformed( Heistum heistum, Instructation instructation ) {
        Instructations.infoLifecycle( heistum, "Conformed",
                String.format(
                "System committed instruction (%s)", instructation.className().replace( "Instructation", "" )
                )
        );
    }

    public static void infoConformed( Raider raider, String methodName ) {
        if( methodName == null ) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            methodName = stackTraceElements[ 2 ].getMethodName();
        }

        Instructations.infoLifecycle( raider.parentHeist(), "Conformed",
                String.format(
                        "System committed instruction (%s::%s)", raider.className(), methodName
                )
        );
    }

    public static void infoConformed( Raider raider ) {
        Instructations.infoConformed( raider, null );
    }

    public static void infoCompleted( Raider raider, String methodName ) {
        if( methodName == null ) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            methodName = stackTraceElements[ 2 ].getMethodName();
        }

        Instructations.infoLifecycle( raider.parentHeist(), "Termination",
                String.format(
                        "Instruction completed (%s::%s)", raider.className(), methodName
                )
        );
    }

    public static void infoCompleted( Raider raider ) {
        Instructations.infoCompleted( raider, null );
    }

}
