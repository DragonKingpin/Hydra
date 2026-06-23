package com.walnut.odin.formation;

import java.util.Locale;

import com.pinecone.framework.system.IrrationalProvokedException;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.dispatch.LocalFormationDispatcher;
import com.walnut.odin.formation.mapper.OdinFormationMappingDriver;
import com.walnut.odin.formation.recovery.FormationRunReconciler;
import com.walnut.odin.formation.recovery.LightweightFormationRunReconciler;
import com.walnut.odin.formation.schedule.FormationRunPreparator;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.schedule.KernelFormationRunPreparator;
import com.walnut.odin.formation.schedule.RavenFormationScheduler;
import com.walnut.odin.formation.service.FormationService;
import com.walnut.odin.formation.service.RavenFormationService;
import com.walnut.odin.system.TaskCentralControl;

public class RavenFormationColonel implements FormationColonel {

    protected TaskCentralControl    mSuperior;
    protected FormationInstrument   mFormationInstrument;

    protected FormationRunPreparator mRunPreparator;
    protected FormationRunReconciler mRunReconciler;
    protected FormationDispatcher    mDispatcher;
    protected FormationScheduler     mScheduler;
    protected FormationService       mService;

    public RavenFormationColonel( TaskCentralControl superior ) {
        this.mSuperior = superior;
    }

    @Override
    public void prepare() {
        this.prepareFormationInstrument();

        FormationConfig config = this.mFormationInstrument.formationConfig();
        this.mDispatcher = new LocalFormationDispatcher( config );
        this.mRunReconciler = new LightweightFormationRunReconciler( config );
        this.mScheduler = new RavenFormationScheduler(
                this.mFormationInstrument,
                this.mSuperior.taskScheduler(),
                this.mDispatcher,
                this.mRunReconciler
        );
        this.mRunPreparator = new KernelFormationRunPreparator( this.mFormationInstrument.runService() );
        this.mService = new RavenFormationService(
                this.mRunPreparator,
                this.mFormationInstrument,
                this.mScheduler,
                this.mDispatcher
        );
    }

    protected void prepareFormationInstrument() {
        if ( this.mFormationInstrument != null ) {
            return;
        }

        TritiumSystem system = (TritiumSystem)this.mSuperior.parentSystem();
        JSONObject subsystemConfig = (JSONObject)this.mSuperior.getSubsystemConfig();
        OdinFormationMappingDriver driver = new OdinFormationMappingDriver(
                system,
                (IbatisClient)system.getMiddlewareDirector().getRDBManager().getRDBClientByName(
                        this.resolveFormationDatabaseKey( subsystemConfig )
                ),
                system.getDispenserCenter()
        );

        this.mFormationInstrument = new RavenFormationInstrument(
                new GenericFormationConfig( subsystemConfig ),
                this.mSuperior.taskRegiment().taskInstrument().getGuidAllocator(),
                driver.groupMapper(),
                driver.groupTaskMapper(),
                driver.runMapper(),
                driver.pageMapper(),
                driver.frameMapper()
        );
    }

    protected String resolveFormationDatabaseKey( JSONObject subsystemConfig ) {
        JSONObject metaDependent = subsystemConfig == null ? null : subsystemConfig.optJSONObject( "metaDependent" );
        if ( metaDependent == null ) {
            throw new IrrationalProvokedException( "Odin formation database dependency `metaDependent` does not exist." );
        }
        return metaDependent.optString(
                "formationInstrument",
                metaDependent.optString( "taskInstrument", "" )
        );
    }

    @Override
    public void start() {
        if ( this.mService == null ) {
            this.prepare();
        }

        FormationConfig config = this.mFormationInstrument.formationConfig();
        if ( !config.isFormationEnabled() ) {
            this.mSuperior.getLogger().info( "[OdinFormation] [FormationDisabled] <Pass>" );
            return;
        }
        if ( !"single-master".equals( config.getFormationMode().toLowerCase( Locale.ROOT ) ) ) {
            this.mSuperior.getLogger().info(
                    "[OdinFormation] [FormationDisabled] (Reason: `unsupported-mode`, Mode: `{}`) <Pass>",
                    config.getFormationMode()
            );
            return;
        }

        this.mDispatcher.startup();
        this.mScheduler.startup();
    }

    @Override
    public void shutdown() {
        if ( this.mScheduler != null ) {
            this.mScheduler.shutdown();
        }
        if ( this.mDispatcher != null ) {
            this.mDispatcher.shutdown();
        }
    }

    @Override
    public FormationInstrument formationInstrument() {
        return this.mFormationInstrument;
    }

    @Override
    public FormationService formationService() {
        return this.mService;
    }

    @Override
    public FormationScheduler formationScheduler() {
        return this.mScheduler;
    }

    @Override
    public FormationDispatcher formationDispatcher() {
        return this.mDispatcher;
    }
}
