package Pinecone.Framework.Util.Summer;

import Pinecone.Framework.System.Prototype.Spawner;
import Pinecone.Framework.Util.Summer.prototype.HostSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SystemSpawner implements Spawner {
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

    public static ArchControlDispatcher spawnDispatcher ( String szDispatcherPrototypeName, ArchHostSystem system ){
        ArchControlDispatcher archControlDispatcher = null;
        try {
            Class<?> pVoid = Class.forName( szDispatcherPrototypeName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchHostSystem.class );
                archControlDispatcher = (ArchControlDispatcher) constructor.newInstance( system );
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

    public static WizardSummoner spawnWizardSummoner ( String szSummonerPrototypeName, ArchConnection connection ) {
        WizardSummoner summoner = null;
        try {
            Class<?> pVoid = Class.forName( szSummonerPrototypeName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchConnection.class );
                summoner = (WizardSummoner) constructor.newInstance( connection );
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
