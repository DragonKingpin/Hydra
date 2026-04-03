package com.walnut.odin.conduct.schedule.entity;

import java.util.HashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.JSONObject;

public class ConcurrentQuota implements Pinenut {

    private final short      mnPriority;

    // 最大水位，如果高于该水位，对应优先级的任务不允许再并行启动
    // The maximum level, if it is higher than this level,
    // tasks of corresponding priority are not allowed to start in parallel again
    private volatile double  mnMaximumRatio;
    private volatile Long    mnMaximumCnt;


    // 最低保障水位，如果低于该水位，会主动启动对应优先级的任务
    // The minimum guaranteed level,
    // if it is lower than this level, the corresponding priority task will be initiated actively
    private volatile double  mnMinimumRatio;
    private volatile Long    mnMinimumCnt;

    public ConcurrentQuota( short priority ) {
        this.mnPriority = priority;
    }

    public short getPriority() {
        return this.mnPriority;
    }

    public double getMaximumRatio() {
        return this.mnMaximumRatio;
    }

    public void setMaximumRatio( double nMaximumRatio ) {
        this.mnMaximumRatio = nMaximumRatio;
    }

    public Long getMaximumCnt() {
        return this.mnMaximumCnt;
    }

    public void setMaximumCnt( Long nMaximumCnt ) {
        this.mnMaximumCnt = nMaximumCnt;
    }

    public double getMinimumRatio() {
        return this.mnMinimumRatio;
    }

    public void setMinimumRatio( double nMinimumRatio ) {
        this.mnMinimumRatio = nMinimumRatio;
    }

    public Long getMinimumCnt() {
        return this.mnMinimumCnt;
    }

    public void setMinimumCnt( Long nMinimumCnt ) {
        this.mnMinimumCnt = nMinimumCnt;
    }


    public boolean isMaximumRatioMode() {
        return this.mnMaximumRatio >= 0D;
    }

    public boolean isMinimumRatioMode() {
        return this.mnMinimumRatio >= 0D;
    }

    public boolean isMaximumUnlimited() {
        return this.mnMaximumCnt != null && this.mnMaximumCnt < 0L;
    }

    public boolean isMinimumUnlimited() {
        return this.mnMinimumCnt != null && this.mnMinimumCnt < 0L;
    }



    public static ConcurrentQuota from( JSONObject map ) {
        short nPriority = (short) map.optLong( "priority" );
        ConcurrentQuota quota = new ConcurrentQuota( nPriority );

        quota.setMaximumRatio( map.optDouble( "maximumRatio", 0D ) );
        quota.setMinimumRatio( map.optDouble( "minimumRatio", 0D ) );

        if ( map.hasOwnProperty( "maximumCnt" ) && !map.isNull( "maximumCnt" ) ) {
            long nMaximumCnt = map.optLong( "maximumCnt" );
            if ( nMaximumCnt < 0 ) {
                quota.setMaximumCnt( Long.MAX_VALUE );
            }
            else {
                quota.setMaximumCnt( nMaximumCnt );
            }
        }

        if ( map.hasOwnProperty( "minimumCnt" ) && !map.isNull( "minimumCnt" ) ) {
            long nMinimumCnt = map.optLong( "minimumCnt" );
            if ( nMinimumCnt < 0 ) {
                quota.setMinimumCnt( Long.MAX_VALUE );
            }
            else {
                quota.setMinimumCnt( nMinimumCnt );
            }
        }

        return quota;
    }

    public static Map<String, ConcurrentQuota> fromThose( JSONObject map ) {
        Map<String, ConcurrentQuota> quotas = new HashMap<>();

        if ( map == null ) {
            return quotas;
        }

        for ( Map.Entry<String, Object> entry : map.entrySet() ) {
            String szKey = entry.getKey();
            JSONObject joQuota = (JSONObject) entry.getValue();
            ConcurrentQuota quota = ConcurrentQuota.from( joQuota );
            quotas.put( szKey, quota );
        }

        return quotas;
    }


    public ConcurrentQuota reproduce( short nPriority ) {
        ConcurrentQuota quota = new ConcurrentQuota( nPriority );
        quota.setMaximumRatio( this.getMaximumRatio() );
        quota.setMinimumRatio( this.getMinimumRatio() );
        quota.setMaximumCnt( this.getMaximumCnt() );
        quota.setMinimumCnt( this.getMinimumCnt() );
        return quota;
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "priority"      , this.getPriority() ),
                new KeyValue<>( "maximumRatio"  , this.getMaximumRatio() ),
                new KeyValue<>( "maximumCnt"    , this.getMaximumCnt() ),
                new KeyValue<>( "minimumRatio"  , this.getMinimumRatio() ),
                new KeyValue<>( "minimumCnt"    , this.getMaximumCnt() )
        } );
    }
}
