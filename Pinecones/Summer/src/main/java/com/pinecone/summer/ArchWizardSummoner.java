package com.pinecone.summer;

import com.pinecone.summer.prototype.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ArchWizardSummoner implements WizardSummoner {
    protected HostSystem                 mParentSystem;
    protected ArchConnection             mConnection;
    protected Wizard                     mLastWizard     = null ;

    public ArchWizardSummoner(ArchConnection connection ) {
        this.mParentSystem = connection.getHostSystem();
        this.mConnection = connection;
    }

    public HostSystem getSystem() {
        return this.mParentSystem;
    }

   public Wizard getLastSummoned(){
        return this.mLastWizard;
   }



    @Override
    public String queryNamespace( String szNickName ){
        return this.mParentSystem.getWizardPackageName() + "." + szNickName;
    }

    protected String spawnFullModelPrototypeName( String szNickName ){
        return this.queryNamespace(szNickName) + "." + szNickName + this.mParentSystem.getModelClassSuffix();
    }

    protected String spawnFullControlPrototypeName( String szNickName ){
        return this.queryNamespace(szNickName) + "." + szNickName + this.mParentSystem.getControlClassSuffix();
    }

    protected Pagesion spawnWizardModelByCallHisName(String szClassName ){
        Pagesion obj = null;
        try {
            Class<?> pVoid = Class.forName( szClassName );
            try{
                Constructor<?> constructor = pVoid.getConstructor( ArchConnection.class );
                obj = (Pagesion) constructor.newInstance( this.mConnection );
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

    public Pagesion spawnWizardModel(String szNickName ) {
        return this.spawnWizardModelByCallHisName( this.spawnFullModelPrototypeName( szNickName ) );
    }

    public JSONBasedControl    spawnWizardControl( String szNickName ) {
        return this.spawnWizardControlByCallHisName( this.spawnFullControlPrototypeName( szNickName ) );
    }

    @Override
    public Wizard summon( String szNickName, Object... args ) throws ServletException, IOException {
        JSONBasedControl hControl = this.spawnWizardControl( szNickName );
        Pagesion hModel     = this.spawnWizardModel( szNickName );

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
    public Wizard summonIfExist( String szNickName ) throws ServletException, IOException {
        Wizard wizard = this.summon( szNickName );
        if( wizard == null ){
            this.mConnection.getDispatcher().traceSystem404Error();
        }
        return wizard;
    }

    @Override
    public Wizard summonAndExecute( String szNickName ) throws ServletException, IOException {
        Wizard wizard = this.summonIfExist( szNickName );
        this.executeAfterSummonSequence();
        return wizard;
    }

    public ArchWizard revealArchetype( Pagesion hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            return (ArchWizard)hModel;
        }
        else if( hControl instanceof ArchWizard){
            return (ArchWizard)hControl;
        }
        return null;
    }

    public void beforeSummon(Pagesion hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            ( (ArchWizard) hModel ).beforeSummon();
        }
        if( hControl instanceof ArchWizard){
            ( (ArchWizard) hControl ).beforeSummon();
        }
    }

    public void soulBound(Pagesion hModel, JSONBasedControl hControl ){
        if( hModel instanceof ArchWizard){
            ( (ArchWizard) hModel ).soulBound( hModel, hControl );
        }
        if( hControl instanceof ArchWizard){
            ( (ArchWizard) hControl ).soulBound( hModel, hControl );
        }
    }

}
