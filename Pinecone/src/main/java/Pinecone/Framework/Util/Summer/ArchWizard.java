package Pinecone.Framework.Util.Summer;

import Pinecone.Framework.Util.Summer.prototype.JSONBasedControl;
import Pinecone.Framework.Util.Summer.prototype.JSONBasedModel;
import Pinecone.Framework.Util.Summer.prototype.MVCYokedWizardAllyType;
import Pinecone.Framework.Util.Summer.prototype.WizardSoul;

import javax.servlet.ServletException;
import java.io.IOException;

public abstract class ArchWizard implements WizardSoul, MVCYokedWizardAllyType {
    protected ArchConnection mConnection         =  null  ;
    protected ArchHostSystem         mParentSystem    =  null  ;
    protected ArchControlDispatcher  mDispatcher      =  null  ;
    private JSONBasedModel           mYokedModel      =  null  ;
    private JSONBasedControl         mYokedControl    =  null  ;


    public ArchWizard ( ArchConnection session ) {
        this.mConnection = session;
        this.mDispatcher = this.mConnection.getDispatcher();
        this.mParentSystem = this.mDispatcher.getHostSystem();
    }

    @Override
    public ArchConnection getConnection() {
        return this.mConnection;
    }

    @Override
    public ArchHostSystem getHostSystem() {
        return this.mParentSystem;
    }

    @Override
    public ArchControlDispatcher getDispatcher(){
        return this.mDispatcher;
    }



    public void soulBound( JSONBasedModel model, JSONBasedControl control ){
        this.mYokedModel   = model;
        this.mYokedControl = control;
    }

    public JSONBasedModel   revealYokedModel(){
        return this.mYokedModel;
    }

    public JSONBasedControl revealYokedControl(){
        return this.mYokedControl;
    }



    public void beforeSummon() {
    }

    public void summoning() throws ServletException, IOException {
        try{
            if( this.mYokedControl != null ){
                this.mYokedControl.beforeDispatch();
                this.mYokedControl.dispatch();
                this.mYokedControl.afterDispatch();
            }

            if( this.mYokedModel != null ){
                this.mYokedModel.beforeDispatch();
                this.mYokedModel.dispatch();
                this.mYokedModel.render();
                this.mYokedModel.afterDispatch();
            }

        }
        catch ( TerminateSessionException e ){
            System.out.println( "Wizard: One of caught session or sequence has been terminated." );
        }
    }

    public void afterSummon() {}
}
