package com.sauron.radium.heistron;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.unit.MultiScopeMaptron;
import com.pinecone.framework.unit.TreeMap;
import com.pinecone.framework.unit.affinity.RecursiveUnitOverrider;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.config.ScopedMapConfigReinterpreter;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeistScheme extends RecursiveUnitOverrider<String, Object > implements Pinenut {
    protected Heistum                           mParentHeist;
    protected Heistgram                         mHeistron;
    protected JSONConfig                        mjoTemplateHeistSchemeConfig;

    protected JSONConfig                        mjoProtoConfig ;  // 当前Heist的JSON原型配置项，等待被子Heist继承和重写
    protected JSONObject                        mjoChildrenConfig;
    protected MultiScopeMap<String, Object >    mHeistScope;

    protected MapConfigReinterpreter            mReinterpreter;


    public HeistScheme( Heistum heist ) {
        this.mParentHeist                 = heist;
        this.mHeistron                    = this.mParentHeist.getHeistgram();
        this.mjoTemplateHeistSchemeConfig = this.mHeistron.getTemplateHeistSchemeConfig();
        this.mjoProtoConfig               = this.getParentHeist().getProtoConfig();

        this.mHeistScope                  = new MultiScopeMaptron<>();
        this.getHeistScope().addParent( ( new MultiScopeMaptron<>( this.getProtoConfig() ) ).addParent(
                ( new MultiScopeMaptron<>( this.getTemplateHeistSchemeConfig() ) ).setName( "Template" ) )
        ).setName( "ProtoConfig" );
        this.mjoChildrenConfig            = (JSONObject) this.getHeistScope().get( Heistum.ConfigChildrenKey );

        this.mReinterpreter               = new ScopedMapConfigReinterpreter( null );
    }

    protected HeistScheme applyInstanceScope( Map<String, Object > instance ) {
        this.getHeistScope().setThisScope( instance );
        return this;
    }

    public JSONConfig getInstanceConfigByName( String name ) {
        return this.getInstanceConfigByName( name, false );
    }

    /**
     * getInstanceConfigByName
     * @param name ( Child instance name, which will extents the parent scope, and get its instance config of this child. )
     *             ( The `null` is the current scope, [this] )
     * @param bRecursive ( Override all object and list, if that key which its child doesnt`t had. )
     * @return Instance Config
     */
    public JSONConfig getInstanceConfigByName( @Nullable String name, boolean bRecursive ) {
        Map<String, Object > selfProto = null;
        Map<String, Object > selfCopy ;

        if( name == null ) {
            selfCopy = this.getProtoConfig().clone();
        }
        else {
            JSONObject sub = this.mjoChildrenConfig.optJSONObject( name );
            if( sub != null ) {
                selfProto = sub;
                selfCopy  = sub.clone();
            }
            else {
                return null;
            }
        }

        // Protecting the children`s key ["Children"]
        Map thisChildren = (Map)selfCopy.get( Heistum.ConfigChildrenKey );
        if( thisChildren != null ) {
            selfCopy.remove( Heistum.ConfigChildrenKey );
        }

        this.applyInstanceScope( selfCopy );
        JSONConfig neo = new JSONConfig( this.getProtoConfig() );
        LinkedHashMap<String, Object > overridden = new LinkedHashMap<>();
        this.getHeistScope().overrideTo( overridden );
        neo.setThisScope( overridden );


        if( bRecursive ) {
            this.overrideObject( overridden, this.getProtoConfig(), bRecursive );
            this.overrideObject( overridden, this.getTemplateHeistSchemeConfig(), bRecursive );
            //Debug.echo( JSON.stringify( overridden, 2 ) );
        }

        // Restoring the protected children`s key ["Children"]
        if( thisChildren != null ) {
            neo.put( Heistum.ConfigChildrenKey, thisChildren );
        }
        else {
            neo.put( Heistum.ConfigChildrenKey, new JSONMaptron() );
        }

        if( name == null ) {
            this.overrideOrchestrationSegment( this.getProtoConfig(), neo );
        }
        else {
            this.overrideOrchestrationSegment( selfProto, neo );
        }

        return neo;
    }

    protected void overrideOrchestrationSegment( Map<String, Object > selfProto, JSONConfig neo ) {
        Map jp = (Map) selfProto.get( Heistum.ConfigOrchestrationKey );
        Map<String, Object > copy ;
        if( jp == null ) {
            copy = new JSONMaptron();
        }
        else {
            copy = ( (JSONObject) jp).clone();
        }
        this.override( copy, this.getTemplateHeistSchemeConfig().opt( Heistum.ConfigOrchestrationKey ), true );

        neo.put( Heistum.ConfigOrchestrationKey, copy );
    }

    public void overrideSegment ( Map<String, Object > parentProto, Map<String, Object > instance ) {
        if( parentProto == this.getHeistScope() ) {
            this.getHeistScope().overrideTo( instance );
        }
        else {
            MultiScopeMap<String, Object > scope = new MultiScopeMaptron<>();
            scope.setThisScope( parentProto );
            scope.overrideTo( instance );
        }
    }


    public HeistScheme reinterpret( JSONConfig that ) {
        MultiScopeMap<String, Object > sysGlobalScope  = this.getHeistgram().getSystem().getGlobalConfigScope(); // System runtime global config scope.

        JSONConfig heistParentList = this.getHeistgram().getLocalHeistsConfigList();                   // Parent Scope of the master[e.g. Heist.json5::Heists] config.
        JSONConfig rootConfig      = (JSONConfig) this.getHeistgram().getSystem().getGlobalConfig();   // Root Scope of the master[e.g. config.json5] config.

        MultiScopeMap<String, Object > keyWords = new MultiScopeMaptron<>( new TreeMap<>() );
        keyWords.put( "this"      , that               );
        keyWords.put( "super"     , heistParentList    );
        keyWords.put( "__root__"  , rootConfig         );

        this.mReinterpreter.setPrimaryScope( sysGlobalScope );
        this.mReinterpreter.addExcludeKey( Heistum.ConfigChildrenKey );
        this.mReinterpreter.reinterpretByBasicKeyWordsScope( that, keyWords );
        return this;
    }

    public MultiScopeMap<String, Object > getHeistScope() {
        return this.mHeistScope;
    }

    public JSONConfig getProtoConfig() {
        return this.mjoProtoConfig;
    }

    public Heistgram getHeistgram() {
        return this.mHeistron;
    }

    public Heistum getParentHeist() {
        return this.mParentHeist;
    }

    public JSONConfig getTemplateHeistSchemeConfig() {
        return this.mjoTemplateHeistSchemeConfig;
    }

}
