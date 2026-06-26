package com.walnut.odin.conduct.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.CollectionUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskPriority;
import com.walnut.odin.conduct.schedule.entity.ConcurrentQuota;
import com.walnut.odin.conduct.schedule.entity.ScheduleAllocatorQuotaSnapshot;
import com.walnut.odin.conduct.schedule.entity.ScheduleAllocatorSnapshot;
import com.walnut.odin.conduct.schedule.entity.ScheduleFittingContext;

public class RavenScheduleAllocator implements InstanceScheduleAllocator {

    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private String                                             mszPartitionName;
    private Map<String, ConcurrentQuota>                       mQuotaConfig;
    private ConcurrentMap<Integer, ConcurrentQuota>            mPriorityQuota;
    private AtomicLong                                         mGlobalConcurrentInstance;

    private ConcurrentMap<Integer, Lock>                       mPrioritySegLocks;
    private ConcurrentMap<Integer, Map<GUID, InstanceEntry>>   mPriorityInstances;
    private Lock                                               mGlobalInstanceLock;

    protected void from_config( JSONObject config ) {
        this.mszPartitionName = config.optString( "name" );

        JSONObject joQuotaConfig = config.getJSONObject( "quota" );
        this.mQuotaConfig = ConcurrentQuota.fromThose( joQuotaConfig );
        this.mGlobalConcurrentInstance = new AtomicLong( config.optLong( "globalConcurrentInstance" ) );

        for ( Map.Entry<String, ConcurrentQuota> entry : this.mQuotaConfig.entrySet() ) {
            if ( entry == null ) {
                continue;
            }

            String          szKey   = entry.getKey();
            ConcurrentQuota value   = entry.getValue();

            if ( szKey == null || value == null ) {
                continue;
            }

            if ( "default".equalsIgnoreCase( szKey ) ) {
                continue;
            }

            this.refreshQuotaCount( value, this.mGlobalConcurrentInstance.get() );
            this.mPriorityQuota.put( (int) value.getPriority(), value );
        }
    }

    protected void trace_dispatcher_config() {
        JSONObject jo = new JSONMaptron();

        jo.put( "PartitionName", this.mszPartitionName );
        jo.put( "ConcurrentInstance", this.mGlobalConcurrentInstance.get() );
        jo.put( "QuotaConfig", new JSONMaptron( CollectionUtils.genericConvert( this.mQuotaConfig ), true ) );

        log.info( "[ScheduleAllocator] Allocator configured with following configs: {}", jo.toJSONStringI( 2 ) );
    }

    public RavenScheduleAllocator( JSONObject config ) {
        this.mPriorityQuota            = new ConcurrentHashMap<>();
        this.mPrioritySegLocks         = new ConcurrentHashMap<>();
        this.mPriorityInstances        = new ConcurrentHashMap<>();
        this.mGlobalInstanceLock       = new ReentrantLock();

        this.from_config( config );
        this.trace_dispatcher_config();
    }

    public RavenScheduleAllocator( UniformTaskScheduler taskScheduler ) {
        this(
                taskScheduler.ravenTaskConfig().getScheduleGlobalAllocatorConfig().optJSONObject(
                        taskScheduler.ravenTaskConfig().getSchedulePartitionName()
                )
        );
    }


    protected ConcurrentQuota resolveQuotaTemplate( short nPriority ) {
        if ( this.mQuotaConfig == null || this.mQuotaConfig.isEmpty() ) {
            return null;
        }

        if ( isQuotaBypassedPriority( nPriority ) ) {
            ConcurrentQuota unlimitedQuota = this.mQuotaConfig.get( "unlimited" );
            if ( unlimitedQuota != null ) {
                return unlimitedQuota.reproduce( nPriority );
            }
        }

        ConcurrentQuota directQuota = this.mPriorityQuota.get( (int) nPriority );
        if ( directQuota != null ) {
            return directQuota.reproduce( nPriority );
        }

        ConcurrentQuota defaultQuota = this.mQuotaConfig.get( "default" );
        if ( defaultQuota != null ) {
            return defaultQuota.reproduce( nPriority );
        }

        return null;
    }

    protected static Map<Integer, List<InstanceEntry>> groupInstancesByPriority( Collection<InstanceEntry> instances ) {
        Map<Integer, List<InstanceEntry>> grouped = new HashMap<>();

        for ( InstanceEntry instance : instances ) {
            if ( instance == null ) {
                continue;
            }

            int nPriority = instance.getActuallyPriority();

            List<InstanceEntry> list = grouped.computeIfAbsent(
                    nPriority,
                    k -> new ArrayList<>()
            );
            list.add( instance );
        }

        return grouped;
    }

    protected Lock affirmPrioritySegLock( Integer nPriority ) {
        return this.mPrioritySegLocks.computeIfAbsent(
                nPriority,
                k -> new ReentrantLock()
        );
    }

    protected ConcurrentQuota affirmQuota( short nPriority ) {
        ConcurrentQuota quota = this.mPriorityQuota.computeIfAbsent(
                (int) nPriority,
                k -> {
                    ConcurrentQuota template = this.resolveQuotaTemplate( nPriority );
                    if ( template != null ) {
                        return template;
                    }
                    return new ConcurrentQuota( nPriority );
                }
        );

        this.refreshQuotaCount( quota, this.mGlobalConcurrentInstance.get() );

        return quota;
    }

    protected void refreshQuotaCount( ConcurrentQuota quota, long nGlobalConcurrentInstance ) {
        if ( quota == null ) {
            return;
        }

        if ( quota.isMaximumRatioMode() ) {
            long nMaximumCnt = (long) Math.floor( nGlobalConcurrentInstance * quota.getMaximumRatio() );
            if ( nMaximumCnt < 0 ) {
                nMaximumCnt = 0;
            }
            quota.setMaximumCnt( nMaximumCnt );
        }
        else {
            Long nMaximumCnt = quota.getMaximumCnt();
            if ( nMaximumCnt == null ) {
                quota.setMaximumCnt( 0L );
            }
            else if ( nMaximumCnt < 0 ) {
                quota.setMaximumCnt( Long.MAX_VALUE );
            }
        }

        if ( quota.isMinimumRatioMode() ) {
            long nMinimumCnt = (long) Math.floor( nGlobalConcurrentInstance * quota.getMinimumRatio() );
            if ( nMinimumCnt < 0 ) {
                nMinimumCnt = 0;
            }
            quota.setMinimumCnt( nMinimumCnt );
        }
        else {
            Long nMinimumCnt = quota.getMinimumCnt();
            if ( nMinimumCnt == null ) {
                quota.setMinimumCnt( 0L );
            }
            else if ( nMinimumCnt < 0 ) {
                quota.setMinimumCnt( Long.MAX_VALUE );
            }
        }
    }

    protected Map<GUID, InstanceEntry> affirmPriorityInstances( int nPriority ) {
        return this.mPriorityInstances.computeIfAbsent(
                nPriority,
                k -> new HashMap<>()
        );
    }

    public static boolean isQuotaBypassedPriority( int nPriority ) {
        return nPriority > TaskPriority.UNLIMITED.getValue();
    }

    public long getGlobalConcurrentInstance() {
        return this.mGlobalConcurrentInstance.get();
    }

    @Override
    public ScheduleAllocatorSnapshot snapshot() {
        ScheduleAllocatorSnapshot snapshot = new ScheduleAllocatorSnapshot();
        snapshot.setPartitionName( this.mszPartitionName );
        snapshot.setGlobalConcurrentInstance( this.mGlobalConcurrentInstance.get() );
        snapshot.setCurrentInstanceCount( this.currentInstanceCount() );
        snapshot.setFulledPriorities( new ArrayList<>( this.queryFulledPriority() ) );
        snapshot.setQuotas( this.quotaSnapshots() );
        return snapshot;
    }

    protected List<ScheduleAllocatorQuotaSnapshot> quotaSnapshots() {
        List<ScheduleAllocatorQuotaSnapshot> snapshots = new ArrayList<>();

        if ( this.mQuotaConfig == null || this.mQuotaConfig.isEmpty() ) {
            return snapshots;
        }

        List<String> names = new ArrayList<>( this.mQuotaConfig.keySet() );
        Collections.sort( names );
        for ( String name : names ) {
            ConcurrentQuota quota = this.mQuotaConfig.get( name );
            if ( quota == null ) {
                continue;
            }

            this.refreshQuotaCount( quota, this.mGlobalConcurrentInstance.get() );
            snapshots.add( this.quotaSnapshot( name, quota ) );
        }
        return snapshots;
    }

    protected ScheduleAllocatorQuotaSnapshot quotaSnapshot( String szName, ConcurrentQuota quota ) {
        ScheduleAllocatorQuotaSnapshot snapshot = new ScheduleAllocatorQuotaSnapshot();
        long nCurrentCnt = this.currentInstanceCount( quota.getPriority() );
        long nMaximumCnt = this.quotaCount( quota.getMaximumCnt() );

        snapshot.setName( szName );
        snapshot.setPriority( quota.getPriority() );
        snapshot.setMaximumRatio( quota.getMaximumRatio() );
        snapshot.setMinimumRatio( quota.getMinimumRatio() );
        snapshot.setMaximumCnt( nMaximumCnt );
        snapshot.setMinimumCnt( this.quotaCount( quota.getMinimumCnt() ) );
        snapshot.setCurrentCnt( nCurrentCnt );
        snapshot.setMaximumRatioMode( quota.isMaximumRatioMode() );
        snapshot.setMinimumRatioMode( quota.isMinimumRatioMode() );
        snapshot.setMaximumUnlimited( nMaximumCnt == Long.MAX_VALUE );
        snapshot.setMinimumUnlimited( this.quotaCount( quota.getMinimumCnt() ) == Long.MAX_VALUE );
        snapshot.setFull( nMaximumCnt != Long.MAX_VALUE && nCurrentCnt >= nMaximumCnt );
        return snapshot;
    }

    protected long quotaCount( Long nQuotaCount ) {
        if ( nQuotaCount == null ) {
            return 0L;
        }
        return nQuotaCount;
    }

    protected long currentInstanceCount() {
        long nCount = 0L;
        for ( Integer nPriority : this.mPriorityInstances.keySet() ) {
            if ( nPriority == null ) {
                continue;
            }
            nCount += this.currentInstanceCount( nPriority );
        }
        return nCount;
    }

    protected long currentInstanceCount( int nPriority ) {
        Lock segLock = this.affirmPrioritySegLock( nPriority );
        segLock.lock();
        try {
            Map<GUID, InstanceEntry> instanceMap = this.mPriorityInstances.get( nPriority );
            return instanceMap == null ? 0L : instanceMap.size();
        }
        finally {
            segLock.unlock();
        }
    }

    public void setGlobalConcurrentInstance( long nGlobalConcurrentInstance ) {
        this.mGlobalInstanceLock.lock();
        try {
            this.mGlobalConcurrentInstance.set( nGlobalConcurrentInstance );

            for ( ConcurrentQuota quota : this.mPriorityQuota.values() ) {
                if ( quota == null ) {
                    continue;
                }
                this.refreshQuotaCount( quota, nGlobalConcurrentInstance );
            }
        }
        finally {
            this.mGlobalInstanceLock.unlock();
        }
    }

    public Collection<Integer> queryFulledPriority() {
        Collection<Integer> fulledPriorities = new ArrayList<>();

        for ( Map.Entry<Integer, ConcurrentQuota> kv : this.mPriorityQuota.entrySet() ) {
            Integer         nPriority = kv.getKey();
            ConcurrentQuota quota     = kv.getValue();

            if ( nPriority == null || quota == null ) {
                continue;
            }

            if ( isQuotaBypassedPriority( nPriority ) ) {
                continue;
            }

            Lock segLock = this.affirmPrioritySegLock( nPriority );
            segLock.lock();
            try {
                long nMaximumCnt = quota.getMaximumCnt();
                if ( nMaximumCnt == Long.MAX_VALUE ) {
                    continue;
                }

                Map<GUID, InstanceEntry> instanceMap = this.mPriorityInstances.get( nPriority );
                long                     nCurrentCnt = 0;

                if ( instanceMap != null ) {
                    nCurrentCnt = instanceMap.size();
                }

                if ( nCurrentCnt >= nMaximumCnt ) {
                    fulledPriorities.add( nPriority );
                }
            }
            finally {
                segLock.unlock();
            }
        }

        return fulledPriorities;
    }

    public Collection<InstanceEntry> queryPriorityInstances( int nPriority ) {
        Lock segLock = this.affirmPrioritySegLock( nPriority );
        segLock.lock();
        try {
            Map<GUID, InstanceEntry> instanceMap = this.mPriorityInstances.get( nPriority );
            if ( instanceMap == null || instanceMap.isEmpty() ) {
                return new ArrayList<>();
            }

            return new ArrayList<>( instanceMap.values() );
        }
        finally {
            segLock.unlock();
        }
    }

    @Override
    public String getPartitionName() {
        return this.mszPartitionName;
    }


    protected void pipeFittingByPriority( int nPriority, Collection<InstanceEntry> instances, ScheduleFittingContext context ) {
        Lock segLock = this.affirmPrioritySegLock( nPriority );
        segLock.lock();

        try {
            if ( instances == null || instances.isEmpty() ) {
                return;
            }

            Map<GUID, InstanceEntry>   instanceMap         = this.affirmPriorityInstances( nPriority );
            Collection<InstanceEntry>  launchedInstances   = context.getFittedInstances();
            Collection<InstanceEntry>  discardedInstances  = context.getDiscardedInstances();

            if ( isQuotaBypassedPriority( nPriority ) ) {
                for ( InstanceEntry instance : instances ) {
                    if ( instance == null || instance.getGuid() == null ) {
                        continue;
                    }

                    instanceMap.put( instance.getGuid(), instance );
                    launchedInstances.add( instance );
                }
                return;
            }

            ConcurrentQuota quota = this.affirmQuota( (short) nPriority );
            long            nMaximumCnt = quota.getMaximumCnt();

            long nRemaining = nMaximumCnt - instanceMap.size();
            if ( nRemaining <= 0 ) {
                discardedInstances.addAll( instances );
                return;
            }

            for ( InstanceEntry instance : instances ) {
                if ( instance == null ) {
                    continue;
                }

                GUID instanceGuid = instance.getGuid();
                if ( instanceGuid == null ) {
                    discardedInstances.add( instance );
                    continue;
                }

                if ( instanceMap.containsKey( instanceGuid ) ) {
                    continue;
                }

                if ( nRemaining <= 0 ) {
                    discardedInstances.add( instance );
                    continue;
                }

                instanceMap.put( instanceGuid, instance );
                launchedInstances.add( instance );
                --nRemaining;
            }
        }
        finally {
            segLock.unlock();
        }
    }

    @Override
    public ScheduleFittingContext pipeFitting( Collection<InstanceEntry> instances ) {
        ScheduleFittingContext context = new ScheduleFittingContext();
        if ( instances == null || instances.isEmpty() ) {
            return context;
        }

        Map<Integer, List<InstanceEntry>> groupedInstances = groupInstancesByPriority( instances );
        for ( Map.Entry<Integer, List<InstanceEntry>> kv : groupedInstances.entrySet() ) {
            Integer             priority     = kv.getKey();
            List<InstanceEntry> instanceList = kv.getValue();

            if ( priority == null || instanceList == null || instanceList.isEmpty() ) {
                continue;
            }

            this.pipeFittingByPriority( priority, instanceList, context );
        }

        return context;
    }




    public InstanceEntry reclaimInstance( int nPriority, GUID instanceGuid ) {
        if ( instanceGuid == null ) {
            return null;
        }

        Lock segLock = this.affirmPrioritySegLock( nPriority );
        segLock.lock();
        try {
            Map<GUID, InstanceEntry> instanceMap = this.mPriorityInstances.get( nPriority );
            if ( instanceMap == null ) {
                return null;
            }

            return instanceMap.remove( instanceGuid );
        }
        finally {
            segLock.unlock();
        }
    }

    @Override
    public InstanceEntry reclaimInstance( GUID instanceGuid ) {
        if ( instanceGuid == null ) {
            return null;
        }

        for ( Integer nPriority : this.mPriorityInstances.keySet() ) {
            if ( nPriority == null ) {
                continue;
            }

            Lock segLock = this.affirmPrioritySegLock( nPriority );
            segLock.lock();
            try {
                Map<GUID, InstanceEntry> instanceMap = this.mPriorityInstances.get( nPriority );
                if ( instanceMap == null ) {
                    continue;
                }

                InstanceEntry removed = instanceMap.remove( instanceGuid );
                if ( removed != null ) {
                    return removed;
                }
            }
            finally {
                segLock.unlock();
            }
        }

        return null;
    }

}
