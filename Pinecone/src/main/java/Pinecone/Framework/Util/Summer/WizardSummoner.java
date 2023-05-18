package Pinecone.Framework.Util.Summer;

import Pinecone.Framework.Util.Summer.prototype.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WizardSummoner implements JSONBasedSummoner {
    protected HostSystem                 mParentSystem;
    protected ArchConnection             mConnection;
    protected Wizard                     mLastWizard     = null ;

    public WizardSummoner( ArchConnection connection ) {
        this.mParentSystem = connection.getHostSystem();
        this.mConnection = connection;
    }

    public HostSystem getParentMatrix() {
        return this.mParentSystem;
    }

   public Wizard getLastSummoned(){
        return this.mLastWizard;
   }



    @Override
    public String spawnNamespace( String szNickName ){
        return this.mParentSystem.getWizardPackageName() + "." + szNickName;
    }

    protected String spawnFullModelPrototypeName( String szNickName ){
        return this.spawnNamespace(szNickName) + "." + szNickName + this.mParentSystem.getModelClassSuffix();
    }

    protected String spawnFullControlPrototypeName( String szNickName ){
        return this.spawnNamespace(szNickName) + "." + szNickName + this.mParentSystem.getControlClassSuffix();
    }

    protected JSONBasedModel   spawnWizardModelByCallHisName( String szClassName ){
        JSONBasedModel obj = null;
        try {
            Class<?> pVoid = Class.forName( szClassName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchConnection.class );
                obj = (JSONBasedModel) constructor.newInstance( this.mConnection );
            }
            catch (NoSuchMethodException | InvocationTargetException e1){
                e1.printStackTrace();
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e){
            System.err.println( "Summon Compromised: [" + e.toString() + "]" );
            //e.printStackTrace();
        }

        return obj;
    }

    protected JSONBasedControl spawnWizardControlByCallHisName( String szClassName ){
        JSONBasedControl obj = null;
        try {
            Class<?> pVoid = Class.forName( szClassName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchConnection.class );
                obj = (JSONBasedControl) constructor.newInstance( this.mConnection );
            }
            catch (NoSuchMethodException | InvocationTargetException e1){
                e1.printStackTrace();
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e){
            System.err.println( "Summon Compromised: [" + e.toString() + "]" );
            //e.printStackTrace();
        }

        return obj;
    }

    public JSONBasedModel      spawnWizardModel( String szNickName ) {
        return this.spawnWizardModelByCallHisName( this.spawnFullModelPrototypeName( szNickName ) );
    }

    public JSONBasedControl    spawnWizardControl( String szNickName ) {
        return this.spawnWizardControlByCallHisName( this.spawnFullControlPrototypeName( szNickName ) );
    }

    @Override
    public Wizard summon( String szNickName ) throws ServletException, IOException {
        JSONBasedControl hControl = this.spawnWizardControl( szNickName );
        JSONBasedModel hModel     = this.spawnWizardModel( szNickName );

        ArchWizard hArchetype = this.revealArchetype( hModel ,hControl );
        if( hArchetype == null ){
            return null;
        }
        this.beforeSummon( hModel, hControl );
        this.soulBound( hModel, hControl );

        this.mLastWizard = (Wizard) hArchetype;
        return this.mLastWizard;
    }

    @Override
    public void executeAfterSummonSequence() throws ServletException, IOException {
        if( this.mLastWizard != null ){
            ((ArchWizard)this.mLastWizard).summoning();
            ((ArchWizard)this.mLastWizard).afterSummon();
        }
    }

    @Override
    public void summonAndExecute( String szNickName ) throws ServletException, IOException {
        if( this.summon( szNickName ) == null ){
            this.mConnection.getDispatcher().traceSystem404Error();
        }
        this.executeAfterSummonSequence();
    }

    public ArchWizard revealArchetype(JSONBasedModel hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            return (ArchWizard)hModel;
        }
        else if( hControl instanceof ArchWizard){
            return (ArchWizard)hControl;
        }
        return null;
    }

    public void beforeSummon( JSONBasedModel hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            ( (ArchWizard) hModel ).beforeSummon();
        }
        if( hControl instanceof ArchWizard){
            ( (ArchWizard) hControl ).beforeSummon();
        }
    }

    public void soulBound( JSONBasedModel hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            ( (ArchWizard) hModel ).soulBound( hModel, hControl );
        }
        if( hControl instanceof ArchWizard){
            ( (ArchWizard) hControl ).soulBound( hModel, hControl );
        }
    }

}
