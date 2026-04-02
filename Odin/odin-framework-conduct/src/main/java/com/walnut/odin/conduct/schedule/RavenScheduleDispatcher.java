package com.walnut.odin.conduct.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskPriority;
import com.walnut.odin.conduct.schedule.entity.ConcurrentQuota;
import com.walnut.odin.conduct.schedule.entity.ScheduleLaunchContext;

public class RavenScheduleDispatcher implements InstanceScheduleDispatcher {


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

    public RavenScheduleDispatcher( JSONObject config ) {
        this.mPriorityQuota            = new ConcurrentHashMap<>();
        this.mPrioritySegLocks         = new ConcurrentHashMap<>();
        this.mPriorityInstances        = new ConcurrentHashMap<>();
        this.mGlobalInstanceLock       = new ReentrantLock();

        this.from_config( config );
    }

    public RavenScheduleDispatcher( UniformTaskScheduler taskScheduler ) {
        this(
                taskScheduler.ravenTaskConfig().getScheduleGlobalDispatcherConfig().optJSONObject(
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


    protected void pipeLaunchByPriority(int nPriority, Collection<InstanceEntry> instances, ScheduleLaunchContext context ) {
        Lock segLock = this.affirmPrioritySegLock( nPriority );
        segLock.lock();

        try {
            if ( instances == null || instances.isEmpty() ) {
                return;
            }

            Map<GUID, InstanceEntry>   instanceMap         = this.affirmPriorityInstances( nPriority );
            Collection<InstanceEntry>  launchedInstances   = context.getLaunchedInstances();
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

    public ScheduleLaunchContext pipeCreate( Collection<InstanceEntry> instances ) {
        ScheduleLaunchContext context = new ScheduleLaunchContext();
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

            this.pipeLaunchByPriority( priority, instanceList, context );
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
