package com.pinecone.summer;

import com.pinecone.framework.system.prototype.Factory;
import com.pinecone.framework.system.prototype.Pinenut;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SystemSpawner implements Pinenut {
    public static ArchHostSystem spawnSystem ( String szMatrixPrototypeName, SystemServlet servlet ){
        ArchHostSystem hMatrix = null;
        try {
            Class<?> pVoid = Class.forName( szMatrixPrototypeName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( SystemServlet.class );
                hMatrix = (ArchHostSystem) constructor.newInstance( servlet );
            }
            catch (NoSuchMethodException | InvocationTargetException e1){
                System.err.println( "Critical Error: Servlet Class is error defined." );
                e1.printStackTrace();
            }
        }
        catch ( ClassNotFoundException | IllegalAccessException | InstantiationException e ){
            System.err.println( "Critical Error: Checking 'web.xml' [ServletSystem] to find what was happened. " );
            e.printStackTrace();
        }

        return hMatrix;
    }

    public static ArchConnectDispatcher spawnDispatcher (String szDispatcherPrototypeName, ArchHostSystem system, RouterType routerType ){
        ArchConnectDispatcher archControlDispatcher = null;
        try {
            Class<?> pVoid = Class.forName( szDispatcherPrototypeName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchHostSystem.class, RouterType.class );
                archControlDispatcher = (ArchConnectDispatcher) constructor.newInstance( system, routerType );
            }
            catch (NoSuchMethodException | InvocationTargetException e1){
                System.err.println( "Critical Error: system Dispatcher is error defined." );
                e1.printStackTrace();
            }
        }
        catch ( ClassNotFoundException | IllegalAccessException | InstantiationException e ){
            System.err.println( "Critical Error: Checking 'web.xml' [SystemDispatcher] to find what was happened. " );
            e.printStackTrace();
        }

        return archControlDispatcher;
    }

    public static ArchWizardSummoner spawnWizardSummoner (String szSummonerPrototypeName, ArchConnection connection ) {
        ArchWizardSummoner summoner = null;
        try {
            Class<?> pVoid = Class.forName( szSummonerPrototypeName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchConnection.class );
                summoner = (ArchWizardSummoner) constructor.newInstance( connection );
            }
            catch (NoSuchMethodException | InvocationTargetException e1){
                e1.printStackTrace();
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e){
            e.printStackTrace();
        }

        return summoner;
    }
}
