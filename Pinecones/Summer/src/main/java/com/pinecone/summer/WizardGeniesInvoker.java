package com.pinecone.summer;

import com.pinecone.framework.system.prototype.Prototype;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONArraytron;
import com.pinecone.framework.util.json.JSONException;
import com.pinecone.summer.prototype.GenieBottle;
import com.pinecone.summer.prototype.JSONBasedControl;
import com.pinecone.summer.prototype.Pagesion;
import com.pinecone.summer.prototype.Wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class WizardGeniesInvoker {
    private HashSet<String > mNaughtyGeniesReel               = null  ;
    private HashSet<String > mPrivateGeniesReel               = null  ;
    private Object           mWizardProto                     = null  ;
    private boolean          mbSiftNaughtyGenies              = false ;

    public WizardGeniesInvoker( Object wizardProto ){
        this.mWizardProto = wizardProto;
        this.siftNaughtyGenies( null );
        this.siftPrivateGenies();
    }

    public WizardGeniesInvoker( Object wizardProto, boolean bSiftNaughtyGenies ){
        this.mWizardProto        = wizardProto;
        this.mbSiftNaughtyGenies = bSiftNaughtyGenies;
        this.siftNaughtyGenies(null );
        this.siftPrivateGenies();
    }

    public WizardGeniesInvoker( Object wizardProto, ArchHostSystem matrix ){
        this.mWizardProto        = wizardProto;
        this.mbSiftNaughtyGenies = matrix.getPublicWizardConfig().optBoolean("NaughtyGeniesSifted");
        this.siftNaughtyGenies( matrix );
        this.siftPrivateGenies();
    }

    private void siftFromJSONArray( JSONArray jsonArray ){
        if( jsonArray != null ){
            for (int i = 0; i < jsonArray.length(); i++) {
                this.mNaughtyGeniesReel.add( jsonArray.optString( i ) );
            }
        }
    }

    private void siftNaughtyGenies( ArchHostSystem matrix ){
        if( this.mbSiftNaughtyGenies ){
            this.prospectReel();
            Prototype.getDeclaredMethodsNameSet( this.mNaughtyGeniesReel, GenieBottle.class );
            Prototype.getDeclaredMethodsNameSet( this.mNaughtyGeniesReel, Pagesion.class   );
            Prototype.getDeclaredMethodsNameSet( this.mNaughtyGeniesReel, JSONBasedControl.class );

            if( matrix != null ){
                JSONArray otherNaughtyGenies = matrix.getPublicWizardConfig().optJSONArray("OtherNaughtyGenies");
                this.siftFromJSONArray( otherNaughtyGenies );
            }

            if( this.mWizardProto instanceof Wizard){
                try{
                    JSONArray myNaughtyGenies = ( (Wizard)(this.mWizardProto) ).getMyNaughtyGenies();
                    this.siftFromJSONArray( myNaughtyGenies );
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void siftPrivateGenies(){
        if( this.mbSiftNaughtyGenies ){
            if( this.mPrivateGeniesReel == null ){
                this.mPrivateGeniesReel = new HashSet<>();
            }
            Prototype.getDeclaredMethodsNameSet( this.mPrivateGeniesReel, this.mWizardProto.getClass() );
        }
    }

    private void prospectReel(){
        if( this.mNaughtyGeniesReel == null ){
            this.mNaughtyGeniesReel = new HashSet<>();
        }
    }

    public HashSet<String > getNaughtyGeniesReel(){
        return this.mNaughtyGeniesReel;
    }

    public HashSet<String > getPrivateGeniesReel(){
        return this.mPrivateGeniesReel;
    }

    public boolean willSiftNaughtyGenies(){
        return this.mbSiftNaughtyGenies;
    }


    public Object invokeNormalGenieByCallHisName(String szGenieName) throws NaughtyGenieInvokedException {
        if( this.mNaughtyGeniesReel != null && this.mNaughtyGeniesReel.contains( szGenieName ) ){
            throw new NaughtyGenieInvokedException( "Naughty genie has been invoked." );
        }
        if( this.mPrivateGeniesReel != null && !this.mPrivateGeniesReel.contains( szGenieName ) ){
            throw new NaughtyGenieInvokedException( "Naughty genie has been invoked." );
        }

        try{
            return Prototype.invokeNoParameterMethod( this.mWizardProto, szGenieName );
        }
        catch ( NoSuchMethodException e1 ){
            throw new NaughtyGenieInvokedException( "Ghost genie has been invoked.", NaughtyGenieInvokedException.NaughtyGenieType.N_GHOST );
        }
        catch ( InvocationTargetException e2 ){
            if( e2.getCause() instanceof TerminateSessionException ){
                throw (TerminateSessionException) e2.getCause();
            }

            throw new NaughtyGenieInvokedException( "Heterogeneous genie has been invoked.", NaughtyGenieInvokedException.NaughtyGenieType.N_HETEROGENEOUS, e2 );
        }
        catch ( IllegalAccessException e3 ){
            throw new NaughtyGenieInvokedException( "Illegal genie has been invoked.", NaughtyGenieInvokedException.NaughtyGenieType.N_ILLEGAL,e3 );
        }
    }





    public void removeNaughtyGenie( String szGenieName ){
        if( this.mNaughtyGeniesReel != null ){
            this.mNaughtyGeniesReel.remove( szGenieName );
        }

    }

    public void removeNaughtyGenie( String[] genieNames ){
        if( this.mNaughtyGeniesReel != null ){
            this.mNaughtyGeniesReel.removeAll( Arrays.asList(genieNames) );
        }
    }

    public void removeNaughtyGenie( JSONArray genieNames ){
        if( this.mNaughtyGeniesReel != null ){
            for (int i = 0; i < genieNames.length(); i++) {
                this.mNaughtyGeniesReel.remove( genieNames.optString( i ) );
            }
        }
    }

    public void removeNaughtyGenie( Method[] genies ){
        if( this.mNaughtyGeniesReel != null ) {
            for (Method row : genies) {
                this.mNaughtyGeniesReel.remove(row.getName());
            }
        }
    }

    public void removeNaughtyGenie( Class<?> wizard ){
        if( this.mNaughtyGeniesReel != null ) {
            for (Method row : wizard.getDeclaredMethods()) {
                this.mNaughtyGeniesReel.remove(row.getName());
            }
        }
    }

    public void removeNaughtyGenie( Object wizard ){
        this.prospectReel();
        for (Method row : wizard.getClass().getDeclaredMethods()) {
            this.mNaughtyGeniesReel.remove(row.getName());
        }
    }

    public void removeNaughtyGeniesByJSON( String szGeniesJSON ){
        this.removeNaughtyGenie( new JSONArraytron( szGeniesJSON ) );
    }



    public void addNaughtyGenie( String szGenieName ){
        this.prospectReel();
        this.mNaughtyGeniesReel.add( szGenieName );
    }

    public void addNaughtyGenie( String[] genieNames ){
        this.prospectReel();
        this.mNaughtyGeniesReel.addAll( Arrays.asList(genieNames) );
    }

    public void addNaughtyGenie( JSONArray genieNames ){
        this.prospectReel();
        if( genieNames != null ){
            for (int i = 0; i < genieNames.length(); i++) {
                this.mNaughtyGeniesReel.add( genieNames.optString( i ) );
            }
        }
    }

    public void addNaughtyGenie( Method[] genies ){
        this.prospectReel();
        for ( Method row : genies ) {
            this.mNaughtyGeniesReel.add( row.getName() );
        }
    }

    public void addNaughtyGenie( Class<?> wizard ){
        this.prospectReel();
        Prototype.getDeclaredMethodsNameSet( this.mNaughtyGeniesReel, wizard );
    }

    public void addNaughtyGenie( Object wizard ){
        this.prospectReel();
        Prototype.getDeclaredMethodsNameSet( this.mNaughtyGeniesReel, wizard );
    }

    public void addNaughtyGeniesByJSON( String szGeniesJSON ){
        this.addNaughtyGenie( new JSONArraytron( szGeniesJSON ) );
    }


}
