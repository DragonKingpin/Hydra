package com.sauron.heist.heistron;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.unit.MultiScopeMaptron;
import com.pinecone.framework.unit.TreeMap;
import com.pinecone.framework.unit.affinity.RecursiveUnitOverrider;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.config.ScopedMapConfigReinterpreter;
import com.sauron.heist.heistron.scheme.HeistSchemeRenderResult;
import com.sauron.heist.heistron.scheme.HeistSchemeRenderStage;
import com.sauron.heist.heistron.scheme.HeistSchemeWarning;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatriarchalHeistScheme extends RecursiveUnitOverrider<String, Object > implements HeistScheme, HeistSchemeInspection {
    protected Heistum                           mParentHeist;
    protected Heistgram                         mHeistron;
    protected HeistSchemeContext                mSchemeContext;
    protected JSONConfig                        mjoTemplateHeistSchemeConfig;

    protected JSONConfig                        mjoProtoConfig ;  // 当前Heist的JSON原型配置项，等待被子Heist继承和重写
    protected JSONObject                        mjoChildrenConfig;
    protected MultiScopeMap<String, Object >    mHeistScope;

    protected MapConfigReinterpreter            mReinterpreter;


    public PatriarchalHeistScheme( Heistum heist ) {
        this.mParentHeist                 = heist;
        this.mHeistron                    = this.mParentHeist.getHeistgram();
        this.mSchemeContext               = new RuntimeHeistSchemeContext( heist );
        this.initialize( this.mSchemeContext );
    }

    public PatriarchalHeistScheme( HeistSchemeContext context ) {
        this.initialize( context );
    }

    protected void initialize( HeistSchemeContext context ) {
        this.mSchemeContext               = context;
        this.mjoTemplateHeistSchemeConfig = context.getTemplateHeistSchemeConfig();
        this.mjoProtoConfig               = context.getProtoConfig();
        this.mHeistScope                  = new MultiScopeMaptron<>();
        this.getHeistScope().addParent( ( new MultiScopeMaptron<>( this.getProtoConfig() ) ).addParent(
                ( new MultiScopeMaptron<>( this.getTemplateHeistSchemeConfig() ) ).setName( "Template" ) )
        ).setName( "ProtoConfig" );
        this.mjoChildrenConfig            = (JSONObject) this.getHeistScope().get( Heistum.ConfigChildrenKey );

        this.mReinterpreter               = new ScopedMapConfigReinterpreter( null );
    }

    protected PatriarchalHeistScheme applyInstanceScope( Map<String, Object > instance ) {
        this.getHeistScope().setThisScope( instance );
        return this;
    }

    @Override
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
    @Override
    public JSONConfig getInstanceConfigByName( @Nullable String name, boolean bRecursive ) {
        InstanceConfigSelection selection = this.selectInstanceConfig( name );
        if( selection == null ) {
            return null;
        }

        return this.resolveInstanceConfig( selection, bRecursive );
    }

    protected InstanceConfigSelection selectInstanceConfig( @Nullable String name ) {
        Map<String, Object > selfProto;
        Map<String, Object > selfCopy;

        if( name == null ) {
            selfProto = this.getProtoConfig();
            selfCopy  = this.getProtoConfig().clone();
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

        return new InstanceConfigSelection( name, selfProto, selfCopy );
    }

    protected JSONConfig resolveInstanceConfig( InstanceConfigSelection selection, boolean bRecursive ) {
        Map<String, Object > selfProto = selection.getProto();
        Map<String, Object > selfCopy  = selection.getCopy();

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

        this.overrideOrchestrationSegment( selfProto, neo );

        return neo;
    }

    protected JSONConfig cloneRawInstanceConfig( InstanceConfigSelection selection ) {
        return new JSONConfig( new LinkedHashMap<>( selection.getCopy() ), null );
    }

    protected JSONConfig cloneResolvedInstanceConfig( JSONConfig resolved ) {
        return new JSONConfig( resolved.clone() );
    }

    protected JSONConfig renderInstanceConfig( JSONConfig resolved ) {
        JSONConfig rendered = this.cloneResolvedInstanceConfig( resolved );
        this.reinterpret( rendered );
        return rendered;
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

    @Override
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


    @Override
    public PatriarchalHeistScheme reinterpret( JSONConfig that ) {
        MultiScopeMap<String, Object > sysGlobalScope  = this.mSchemeContext.getGlobalConfigScope(); // System runtime global config scope.

        JSONConfig heistParentList = this.mSchemeContext.getLocalHeistsConfigList();                   // Parent Scope of the master[e.g. Heist.json5::Heists] config.
        Map<String, Object > rootConfig = this.mSchemeContext.getRootConfig();                         // Root Scope of the master[e.g. config.json5] config.

        MultiScopeMap<String, Object > keyWords = new MultiScopeMaptron<>( new TreeMap<>() );
        keyWords.put( "this"      , that               );
        keyWords.put( "super"     , heistParentList    );
        keyWords.put( "__root__"  , rootConfig         );

        this.mReinterpreter.setPrimaryScope( sysGlobalScope );
        this.mReinterpreter.addExcludeKey( Heistum.ConfigChildrenKey );
        this.mReinterpreter.reinterpretByBasicKeyWordsScope( that, keyWords );
        return this;
    }

    @Override
    public HeistSchemeRenderResult previewInstanceConfigByName( @Nullable String name, boolean recursive ) {
        HeistSchemeRenderResult result = new HeistSchemeRenderResult().setInstanceName( name );
        InstanceConfigSelection selection = this.selectInstanceConfig( name );
        if( selection == null ) {
            result.addWarning( new HeistSchemeWarning(
                    HeistSchemeRenderStage.Raw,
                    "InstanceConfigNotFound",
                    "Heist instance config not found: " + name
            ) );
            return result;
        }

        JSONConfig resolved = this.resolveInstanceConfig( selection, recursive );
        result.setRawConfig( this.cloneRawInstanceConfig( selection ) );
        result.setResolvedConfig( this.cloneResolvedInstanceConfig( resolved ) );
        result.setRenderedConfig( this.renderInstanceConfig( resolved ) );
        return result;
    }

    @Override
    public HeistSchemeRenderResult previewInstanceConfigByPath( List<String> names, boolean recursive ) {
        if( names == null || names.isEmpty() ) {
            return this.previewInstanceConfigByName( null, recursive );
        }

        PatriarchalHeistScheme currentScheme = this;
        HeistSchemeRenderResult currentResult = null;
        StringBuilder fullPath = new StringBuilder();

        for( String name : names ) {
            if( name == null || name.isBlank() ) {
                continue;
            }
            if( fullPath.length() > 0 ) {
                fullPath.append( "." );
            }
            fullPath.append( name );

            currentResult = currentScheme.previewInstanceConfigByName( name, recursive );
            if( currentResult.getRenderedConfig() == null ) {
                currentResult.setInstanceName( fullPath.toString() );
                return currentResult;
            }

            currentResult.setInstanceName( fullPath.toString() );
            currentScheme = new PatriarchalHeistScheme(
                    new NestedHeistSchemeContext(
                            this.mSchemeContext,
                            currentResult.getResolvedConfig()
                    )
            );
        }

        return currentResult == null
                ? this.previewInstanceConfigByName( null, recursive )
                : currentResult;
    }

    @Override
    public MultiScopeMap<String, Object > getHeistScope() {
        return this.mHeistScope;
    }

    @Override
    public JSONConfig getProtoConfig() {
        return this.mjoProtoConfig;
    }

    @Override
    public Heistgram getHeistgram() {
        return this.mHeistron;
    }

    @Override
    public Heistum getParentHeist() {
        return this.mParentHeist;
    }

    @Override
    public JSONConfig getTemplateHeistSchemeConfig() {
        return this.mjoTemplateHeistSchemeConfig;
    }

    protected static class RuntimeHeistSchemeContext implements HeistSchemeContext {
        private final Heistum heist;

        protected RuntimeHeistSchemeContext( Heistum heist ) {
            this.heist = heist;
        }

        @Override
        public JSONConfig getProtoConfig() {
            return this.heist.getProtoConfig();
        }

        @Override
        public JSONConfig getTemplateHeistSchemeConfig() {
            return this.heist.getHeistgram().getTemplateHeistSchemeConfig();
        }

        @Override
        public JSONConfig getLocalHeistsConfigList() {
            return this.heist.getHeistgram().getLocalHeistsConfigList();
        }

        @Override
        public MultiScopeMap<String, Object > getGlobalConfigScope() {
            return this.heist.getHeistgram().parentSystem().getGlobalConfigScope();
        }

        @Override
        public Map<String, Object > getRootConfig() {
            return (JSONConfig) this.heist.getHeistgram().parentSystem().getGlobalConfig();
        }
    }

    protected static class NestedHeistSchemeContext implements HeistSchemeContext {
        private final HeistSchemeContext parent;
        private final JSONConfig protoConfig;

        protected NestedHeistSchemeContext( HeistSchemeContext parent, JSONConfig protoConfig ) {
            this.parent = parent;
            this.protoConfig = protoConfig;
        }

        @Override
        public JSONConfig getProtoConfig() {
            return this.protoConfig;
        }

        @Override
        public JSONConfig getTemplateHeistSchemeConfig() {
            return this.parent.getTemplateHeistSchemeConfig();
        }

        @Override
        public JSONConfig getLocalHeistsConfigList() {
            return this.parent.getLocalHeistsConfigList();
        }

        @Override
        public MultiScopeMap<String, Object > getGlobalConfigScope() {
            return this.parent.getGlobalConfigScope();
        }

        @Override
        public Map<String, Object > getRootConfig() {
            return this.parent.getRootConfig();
        }
    }

    protected static class InstanceConfigSelection {
        private final Map<String, Object > proto;
        private final Map<String, Object > copy;

        protected InstanceConfigSelection( String name, Map<String, Object > proto, Map<String, Object > copy ) {
            this.proto = proto;
            this.copy = copy;
        }

        public Map<String, Object > getProto() {
            return this.proto;
        }

        public Map<String, Object > getCopy() {
            return this.copy;
        }
    }

}
