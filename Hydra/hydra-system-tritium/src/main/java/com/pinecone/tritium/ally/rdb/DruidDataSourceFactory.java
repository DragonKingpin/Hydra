package com.pinecone.tritium.ally.rdb;

import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.source.rdb.RelationalDatabase;

public class DruidDataSourceFactory implements DataSourceFactory, Pinenut {
    protected UniformRDBClient rdbClient;

    @MapStructure("Ibatis.DruidConfig.initial-size")
    protected int initialSize = 1;

    @MapStructure("Ibatis.DruidConfig.min-idle")
    protected int minIdle = 1;

    @MapStructure("Ibatis.DruidConfig.max-active")
    protected int maxActive = 20;

    @MapStructure("Ibatis.DruidConfig.max-wait")
    protected long maxWait = 60000;

    @MapStructure("Ibatis.DruidConfig.time-between-eviction-runs-millis")
    protected long timeBetweenEvictionRunsMillis = 60000;

    @MapStructure("Ibatis.DruidConfig.min-evictable-idle-time-millis")
    protected long minEvictableIdleTimeMillis = 300000;

    @MapStructure("Ibatis.DruidConfig.validation-query")
    protected String validationQuery = "SELECT 1";

    @MapStructure("Ibatis.DruidConfig.test-while-idle")
    protected boolean testWhileIdle = true;

    @MapStructure("Ibatis.DruidConfig.test-on-borrow")
    protected boolean testOnBorrow = false;

    @MapStructure("Ibatis.DruidConfig.test-on-return")
    protected boolean testOnReturn = false;

    @MapStructure("Ibatis.DruidConfig.pool-prepared-statements")
    protected boolean poolPreparedStatements = true;

    @MapStructure("Ibatis.DruidConfig.max-pool-prepared-statement-per-connection-size")
    protected int maxPoolPreparedStatementPerConnectionSize = 20;

    @MapStructure("Ibatis.DruidConfig.keep-alive")
    protected boolean keepAlive = true;

    @MapStructure("Ibatis.DruidConfig.connection-error-retry-attempts")
    protected int connectionErrorRetryAttempts = 3;

    @MapStructure("Ibatis.DruidConfig.break-after-acquire-failure")
    protected boolean breakAfterAcquireFailure = false;

    @MapStructure("Ibatis.DruidConfig.filters")
    protected String filters;


    public DruidDataSourceFactory( UniformRDBClient rdbClient ) {
        this.rdbClient = rdbClient;
        IbatisClient ibatisClient = (IbatisClient) rdbClient;

        rdbClient.getRDBManager().getSystem().getPrimaryConfigScope().autoInject(
                DruidDataSourceFactory.class, ibatisClient.getClientConf(), this
        );
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public DataSource getDataSource() {
        IbatisClient ibatisClient = (IbatisClient) this.rdbClient;
        RelationalDatabase rdb    = (RelationalDatabase) this.rdbClient;

        String driver   = ibatisClient.getJDBCDriverName();
        String url      = ibatisClient.getJDBCURL();
        String username = rdb.getUsername();
        String password = rdb.getPassword();

        DruidDataSource ds = new DruidDataSource();

        /*
         * 基础 JDBC
         */
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        /*
         * 连接池参数
         */
        ds.setInitialSize(this.initialSize);
        ds.setMinIdle(this.minIdle);
        ds.setMaxActive(this.maxActive);
        ds.setMaxWait(this.maxWait);

        /*
         * 连接回收
         */
        ds.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);

        /*
         * 连接检测
         */
        ds.setValidationQuery(this.validationQuery);
        ds.setTestWhileIdle(this.testWhileIdle);
        ds.setTestOnBorrow(this.testOnBorrow);
        ds.setTestOnReturn(this.testOnReturn);

        /*
         * PS cache
         */
        ds.setPoolPreparedStatements(this.poolPreparedStatements);
        ds.setMaxPoolPreparedStatementPerConnectionSize(
                this.maxPoolPreparedStatementPerConnectionSize
        );

        ds.setKeepAlive(this.keepAlive);
        ds.setConnectionErrorRetryAttempts(this.connectionErrorRetryAttempts);
        ds.setBreakAfterAcquireFailure(this.breakAfterAcquireFailure);

        if ( this.filters != null ) {
            try {
                ds.setFilters( this.filters );
            }
            catch ( SQLException e ) {
                throw new ProvokeHandleException( e );
            }
        }

        this.rdbClient.getRDBManager().getLogger().info(
                "[Lifecycle] New druid-data-source created (`{}`). <Done>", this.rdbClient.getInstanceName()
        );
        return ds;
    }
}
