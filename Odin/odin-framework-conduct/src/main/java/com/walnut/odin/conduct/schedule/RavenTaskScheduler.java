package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.slime.meta.TableIndex64Meta;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;

public class RavenTaskScheduler implements UniformTaskScheduler {

    // Generate daily batches in advance (24h/Cycle)
    // 每日提前生成当日批次（24h/Cycle）
    public static final Collection<TaskScheduleCycle> DailyTaskScheduleCycles = List.of(
            TaskScheduleCycle.Month, TaskScheduleCycle.Week, TaskScheduleCycle.Day, TaskScheduleCycle.Hour
    );

    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private int                        mnScanThreadCount;
    private long                       mnScanIdWindow;

    private RuntimeAtlasInstrument     mRuntimeAtlasInstrument;

    private CentralizedTaskInstrument  mCentralizedTaskInstrument;
    private RavenTaskMasterManipulator mRavenTaskMasterManipulator;
    private TaskNodeManipulator        mTaskNodeManipulator;

    private ExecutorService            mExecutorService;

    public RavenTaskScheduler( CentralizedTaskInstrument taskInstrument, RuntimeAtlasInstrument atlasInstrument ) {
        this.mCentralizedTaskInstrument  = taskInstrument;
        this.mRavenTaskMasterManipulator = taskInstrument.getRavenTaskMasterManipulator();
        this.mTaskNodeManipulator        = this.mRavenTaskMasterManipulator.getTaskMasterManipulator().getTaskNodeManipulator();

        this.mRuntimeAtlasInstrument     = atlasInstrument;

        RavenTaskConfig config           = (RavenTaskConfig) taskInstrument.getConfig();
        this.mnScanThreadCount           = config.getScheduleScanThreadCount();
        this.mnScanIdWindow              = config.getScheduleScanIdWindow();

        this.mExecutorService            = Executors.newFixedThreadPool( this.mnScanThreadCount * 2 );
    }


    @Override
    public List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, cycles, targetTime );
    }

    @Override
    public List<TaskElement> fetchSchedulableTasksDaily( long idMin, long idMax, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, DailyTaskScheduleCycles, targetTime );
    }



    protected void prepareTaskScheduleTimeOffset( TaskElement element, LocalDateTime targetTime ) {
        TaskScheduleCycle cycle = element.getScheduleCycle();
        String            cron  = element.getScheduleCron();
        if ( cycle == null ) {
            return;
        }

        if ( cron == null || cron.isBlank() ) {
            String defaultCron = ScheduleCronHelper.generateDefaultCron( cycle );
            element.setScheduleCron( defaultCron );
            cron = defaultCron;
        }

        LocalDateTime next = element.getNextScheduleTime();
        if ( next == null ) {
            LocalDateTime firstFireTime = ScheduleCronHelper.computeNextByCron( cron, targetTime.minusSeconds( 1 ) );

            if ( firstFireTime == null ) {
                return;
            }

            element.setNextScheduleTime( firstFireTime );
            this.mTaskNodeManipulator.update( element );
            return; // next 已经是下一次了
        }

        LocalDateTime advanced = ScheduleCronHelper.computeNextByCron( cron, next );
        if ( advanced == null ) {
            return;
        }

        if ( !advanced.equals( next ) ) {
            element.setNextScheduleTime( advanced );
            //this.mTaskNodeManipulator.update( element );
        }
    }

    protected Collection<TaskElement> prepareScheduleTasks( Collection<TaskElement> elements, LocalDateTime targetTime ) {
        if ( elements == null || elements.isEmpty() ) {
            return elements;
        }

        for ( TaskElement element : elements ) {
            this.prepareTaskScheduleTimeOffset( element, targetTime );
        }

        Debug.traceSyn( elements );
        return elements;
    }

    public void prepareSchedulableInstances( Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        TableIndex64Meta range = this.mTaskNodeManipulator.selectSchedulableIdRange( cycles, targetTime );
        if ( range == null ) {
            return;
        }

        long idMin = range.getMinId();
        long idMax = range.getMaxId();
        if ( idMin <= 0 || idMax <= 0 || idMax < idMin ) {
            return;
        }

        long cursor = idMin;
        while ( cursor <= idMax ) {
            long windowStart = cursor;
            long windowEnd   = cursor + this.mnScanIdWindow - 1;

            if ( windowEnd > idMax ) {
                windowEnd = idMax;
            }

            final long finalStart = windowStart;
            final long finalEnd   = windowEnd;

            LocalDateTime finalTargetTime = targetTime;
            this.mExecutorService.submit( () -> {
                log.info( "[TaskSchedulerLifecycle] Preparing schedulable instances (Start: {}, End: {}) <Start>", finalStart, finalEnd );

                Collection<TaskElement> elements = this.mTaskNodeManipulator.fetchSchedulableTasksInRange(
                                finalStart, finalEnd, cycles, finalTargetTime
                );

                elements = this.prepareScheduleTasks( elements, finalTargetTime );

                log.info( "[TaskSchedulerLifecycle] Preparing schedulable instances (Start: {}, End: {}, Size: {}) <Done>", finalStart, finalEnd, elements.size() );
            } );

            cursor = windowEnd + 1;
        }
    }

    public void fetch() {
        this.prepareSchedulableInstances( DailyTaskScheduleCycles, LocalDateTime.now() );
    }

}
