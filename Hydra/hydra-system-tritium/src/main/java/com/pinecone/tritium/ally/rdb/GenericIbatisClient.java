package com.pinecone.tritium.ally.rdb;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.sql.Connection;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.unit.LinkedTreeSet;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.lang.GenericClassScopeSet;
import com.pinecone.framework.util.lang.NamespaceCollector;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.jelly.source.ibatis.transaction.GenericIbatisTransaction;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransaction;
import com.pinecone.slime.source.DAOScanner;
import com.pinecone.slime.source.DataAccessObject;
import com.pinecone.slime.source.XMLResourceScanner;
import com.pinecone.slime.source.rdb.ArchRelationalDatabase;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.slf4j.Logger;

public class GenericIbatisClient extends ArchRelationalDatabase implements IbatisClient, UniformRDBClient {
    protected String                      mszInstanceName       ;

    protected SqlSessionFactory           mSqlSessionFactory    ;

    protected Configuration               mConfiguration        ;

    protected DataSource                  mDataSource           ;

    protected Environment                 mEnvironment          ;

    protected IbatisTransaction           mTransaction          ;

    @MapStructure( "Ibatis" )
    protected JSONObject                  mjoIbatisConf         ;

    @MapStructure( "JDBC.Driver" )
    protected String                      mszJDBCDriverName     ;

    @MapStructure( "JDBC.ExURL" )
    protected String                      mszJDBCExURL          ;

    protected JSONObject                  mjoClientConf         ;

    protected RDBManager                  mRDBManager           ;

    @MapStructure( "Ibatis.Environment" )
    protected String                      mszEnvironment        ;

    @MapStructure( "Ibatis.DataSource" )
    protected String                      mszDataSource         ;

    @MapStructure( "Ibatis.TransactionFactory" )
    protected String                      mszTransactionFactory ;

    protected Logger                      mLogger               ;

    @MapStructure( "Ibatis.PooledConfig.InitialSize" )
    protected int                         mnInitialSize = 0     ;

    @MapStructure( "Ibatis.PooledConfig.MaxActive" )
    protected int                         mnMaxActive = 20      ;

    @MapStructure( "Ibatis.PooledConfig.MaxIdle" )
    protected int                         mnMaxIdle = 20        ;

    @MapStructure( "Ibatis.PooledConfig.MinIdle" )
    protected int                         mnMinIdle  =  1       ;

    @MapStructure( "Ibatis.PooledConfig.MaxWait" )
    protected int                         mnMaxWait = 60000     ;

    @MapStructure( "Ibatis.DataAccessObject.Scanner" )
    protected String                      mszDAOScanner         ;

    @MapStructure( "Ibatis.DataAccessObject.XMLScanner" )
    protected String                      mszXMLScanner         ;

    @MapStructure( "Ibatis.DataAccessObject.ScanScopes" )
    protected Collection<String >         mScannerScopes        ;

    protected DAOScanner                  mDAOScanner           ;

    protected XMLResourceScanner          mXMLScanner           ;

    protected DataSourceFactory           mDataSourceFactory    ;

    protected final ReadWriteLock         mAddScopeLock         = new ReentrantReadWriteLock();


    public GenericIbatisClient( RDBManager manager, String szInstanceName ) {
        this.mRDBManager     = manager;
        this.mszInstanceName = szInstanceName;

        this.mjoClientConf   = this.mRDBManager.getDatabases().optJSONObject( szInstanceName );
        this.mRDBManager.getSystem().getPrimaryConfigScope().autoInject( ArchRelationalDatabase.class, this.mjoClientConf, this );
        this.mRDBManager.getSystem().getPrimaryConfigScope().autoInject( GenericIbatisClient.class, this.mjoClientConf, this );
        this.mLogger         = this.getRDBManager().getSystem().getTracerScope().newLogger( this.className() );

        this.prepareIbatisSubsystem();
    }

    protected void prepareKernelXMLList() {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        ClassScope classScope   = new GenericClassScopeSet( classLoader );
        if ( this.mszXMLScanner == null ) {
            this.mszXMLScanner = "com.pinecone.slime.jelly.source.ibatis.IbatisXMLResourceScanner";
        }
        Object ds = this.getRDBManager().getSharedUniformFactory().optLoadInstance( this.mszXMLScanner, new Object[]{ classScope, classLoader } );
        if ( ds instanceof XMLResourceScanner) {
            this.mXMLScanner = (XMLResourceScanner)ds;
        }
        else {
            throw new IllegalArgumentException( "Illegal class scanner, should be `ClassScanner`: " + this.mszXMLScanner );
        }
    }

    protected void prepareDAOMapperList() {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        ClassScope classScope   = new GenericClassScopeSet( classLoader );
        Object ds = this.getRDBManager().getSharedUniformFactory().optLoadInstance( this.mszDAOScanner, new Object[]{ classScope, classLoader } );
        if ( ds instanceof DAOScanner ) {
            this.mDAOScanner = (DAOScanner)ds;
        }
        else {
            throw new IllegalArgumentException( "Illegal class scanner, should be `ClassScanner`: " + this.mszDAOScanner );
        }


        Set<String > scopes = new LinkedTreeSet<>();
        if ( this.mScannerScopes != null && !this.mScannerScopes.isEmpty() ) {
            try {
                List<String > candidates = new ArrayList<>();
                for ( String sz : this.mScannerScopes ) {
                    scopes.add( sz );
                    this.mDAOScanner.scan( sz, true, candidates );
                }

                for ( String sz : candidates ) {
                    this.addMapper( classLoader.loadClass( sz ) );
                }
            }
            catch ( IOException | ClassNotFoundException e ) {
                throw new ProxyProvokeHandleException( e );
            }
        }
        this.mScannerScopes = scopes;
    }

    protected void prepareIbatisSubsystem() {
        this.mLogger.info( "[Lifecycle] [RDBClient::PrepareIbatisSubsystem::" + this.mszInstanceName + "] <Start>" );

        String szJDBCUrl = this.getJDBCURL();
        Object ds = this.getRDBManager().getSharedUniformFactory().optLoadInstance(
                this.mszDataSource, new Object[] { this }
        );
        if ( ds == null ) {
            ds = this.getRDBManager().getSharedUniformFactory().optLoadInstance(
                    this.mszDataSource, new Object[] { this.mszJDBCDriverName, szJDBCUrl, this.getUsername(), this.getPassword() }
            );
            if ( ds instanceof DataSource ) {
                this.mDataSource = (DataSource) ds;
                if ( ds instanceof PooledDataSource ) {
                    PooledDataSource pds = (PooledDataSource) ds;
                    pds.setPoolMaximumActiveConnections( this.mnMaxActive );
                    pds.setPoolMaximumIdleConnections( this.mnMaxIdle );
                    pds.setPoolTimeToWait( this.mnMaxWait );
                }
            }
            else {
                ds = null;
            }
        }
        else {
            this.mDataSourceFactory = (DataSourceFactory) ds;
            this.mDataSource = this.mDataSourceFactory.getDataSource();
        }

        if ( ds == null ) {
            throw new IllegalArgumentException( "Illegal data source, should be `DataSource` / `DataSourceFactory`: " + this.mszJDBCDriverName );
        }


        TransactionFactory transactionFactory;
        Object tf = this.getRDBManager().getSharedUniformFactory().optLoadInstance( this.mszTransactionFactory, null );
        if ( tf instanceof TransactionFactory ) {
            transactionFactory = (TransactionFactory) tf;
        }
        else {
            throw new IllegalArgumentException( "Illegal transaction factory, should be `TransactionFactory`: " + this.mszTransactionFactory );
        }

        this.mEnvironment       = new Environment( this.mszEnvironment, transactionFactory, this.mDataSource );
        this.mConfiguration     = new Configuration( this.mEnvironment );
        this.mSqlSessionFactory = new SqlSessionFactoryBuilder().build( this.mConfiguration );

        this.prepareDAOMapperList();
        this.prepareKernelXMLList();

        this.mLogger.info( "[Lifecycle] [RDBClient::PrepareIbatisSubsystem::" + this.mszInstanceName + "] <Done>" );
    }

    @Override
    public String getJDBCURL() {
        if ( this.mszJDBCExURL == null ) {
            this.mszJDBCExURL = "";
        }
        if ( !this.mszJDBCExURL.startsWith( "&" ) ) {
            this.mszJDBCExURL = "&" + this.mszJDBCExURL;
        }
        return super.getJDBCURL() + this.mszJDBCExURL;
    }

    @Override
    public String getInstanceName() {
        return this.mszInstanceName;
    }


    @Override
    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    @Override
    public DataSource getDataSource() {
        return this.mDataSource;
    }

    @Override
    public Environment getEnvironment() {
        return this.mEnvironment;
    }

    @Override
    public JSONObject getIbatisConf() {
        return this.mjoIbatisConf;
    }

    @Override
    public String getJDBCDriverName() {
        return this.mszJDBCDriverName;
    }

    @Override
    public JSONObject getClientConf() {
        return this.mjoClientConf;
    }

    @Override
    public DAOScanner getDAOScanner() {
        return this.mDAOScanner;
    }

    @Override
    public <T> void addMapper( Class<T> type ) {
        try {
            this.mConfiguration.addMapper( type );
        }
        catch ( BindingException ignore ) {
            // Do nothing.
        }
    }

    @Override
    public IbatisTransaction transaction() {
        if ( this.mTransaction == null ) {
            this.mTransaction = new GenericIbatisTransaction( this );
        }
        return this.mTransaction;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return this.mSqlSessionFactory;
    }

    @Override
    public SqlSession openSession() {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession();
        return sqlSession;
    }

    @Override
    public SqlSession openSession( boolean autoCommit ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(autoCommit);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( Connection connection ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(connection);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( TransactionIsolationLevel level ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(level);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( ExecutorType execType ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(execType);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( ExecutorType execType, boolean autoCommit ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(execType, autoCommit);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( ExecutorType execType, TransactionIsolationLevel level ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession(execType, level);
        return sqlSession;
    }

    @Override
    public SqlSession openSession( ExecutorType execType, Connection connection ) {
        SqlSession sqlSession = this.mSqlSessionFactory.openSession( execType, connection );
        return sqlSession;
    }

    protected void free0( SqlSession sqlSession ) {
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public RDBManager getRDBManager() {
        return this.mRDBManager;
    }

    @Override
    public DAOScanner getDataAccessObjectScanner() {
        return this.mDAOScanner;
    }

    @Override
    public boolean hasOwnDataAccessObject( Class<?> clazz ) {
        Annotation[] annotations = clazz.getAnnotations();
        for ( Annotation annotation : annotations ) {
            if ( annotation instanceof DataAccessObject ) {
                String s = ((DataAccessObject) annotation).scope();
                if ( s.isEmpty() || s.equals( this.getInstanceName() ) ) {
                    return true;
                }
            }
            else if ( annotation instanceof IbatisDataAccessObject ) {
                String s = ((IbatisDataAccessObject) annotation).scope();
                if ( s.isEmpty() || s.equals( this.getInstanceName() ) ) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<Class<?> > addDataAccessObjectScope0(
            String szPacketName, boolean bIgnoreOwnedChecked, List<String > candidates, ClassLoader classLoader
    ) throws IOException, ClassNotFoundException {
        this.mScannerScopes.add( szPacketName );
        this.mDAOScanner.scan( szPacketName, true, candidates );

        List<Class<?> > candidateClasses = new ArrayList<>();
        for ( String sz : candidates ) {
            Class<?> clazz = classLoader.loadClass( sz );
            if ( bIgnoreOwnedChecked || this.hasOwnDataAccessObject( clazz ) ) {
                candidateClasses.add( clazz );
                this.addMapper( clazz );
            }
        }
        return candidateClasses;
    }

    @Override
    public List<Class<?> > addDataAccessObjectScope( String szPacketName, boolean bIgnoreOwnedChecked ) {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        try {
            List<String > candidates = new ArrayList<>();
            this.mAddScopeLock.writeLock().lock();

            try {
                return this.addDataAccessObjectScope0( szPacketName, bIgnoreOwnedChecked, candidates, classLoader );
            }
            finally {
                this.mAddScopeLock.writeLock().unlock();
            }
        }
        catch ( IOException | ClassNotFoundException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public List<Class<?> > addDataAccessObjectScopeNoneSync( String szPacketName, boolean bIgnoreOwnedChecked ) {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        try {
            List<String > candidates = new ArrayList<>();
            return this.addDataAccessObjectScope0( szPacketName, bIgnoreOwnedChecked, candidates, classLoader );
        }
        catch ( IOException | ClassNotFoundException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public List<Class<? > > addDataAccessObjectScope( String szPacketName ) {
        return this.addDataAccessObjectScope( szPacketName, false );
    }





    private void addXMLObjectScope0(
            String szPacketName, List<String > candidates, ClassLoader classLoader
    ) throws IOException {
        this.mScannerScopes.add( szPacketName );
        this.mXMLScanner.scan( szPacketName, true, candidates );

        for ( String szResource : candidates ) {
            String szResourcePath = szResource.replace(
                    NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, NamespaceCollector.RESOURCE_NAME_SEPARATOR
            ) + ".xml";

            if ( !this.mConfiguration.isResourceLoaded( szResourcePath ) ) {
                InputStream inputStream = classLoader.getResourceAsStream( szResourcePath );
                if ( inputStream == null ) {
                    continue;
                }

                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
                        inputStream, this.mConfiguration, szResourcePath, this.mConfiguration.getSqlFragments()
                );

                xmlMapperBuilder.parse();
                this.mConfiguration.addLoadedResource( szResourcePath );
            }
        }
    }

    @Override
    public void addXMLObjectScope( String szPacketName ) {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        try {
            List<String > candidates = new ArrayList<>();
            this.mAddScopeLock.writeLock().lock();

            try {
                this.addXMLObjectScope0( szPacketName, candidates, classLoader );
            }
            finally {
                this.mAddScopeLock.writeLock().unlock();
            }
        }
        catch ( IOException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public void addXMLObjectScopeNoneSync( String szPacketName ) {
        ClassLoader classLoader = this.getRDBManager().getSystem().getTaskManager().getClassLoader();
        try {
            List<String > candidates = new ArrayList<>();
            this.addXMLObjectScope0( szPacketName, candidates, classLoader );
        }
        catch ( IOException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }




    @Override
    public void close() throws ProxyProvokeHandleException {
        if ( this.mDataSource != null ) {
            if ( this.mDataSource instanceof PooledDataSource ) {
                ((PooledDataSource) this.mDataSource).forceCloseAll();
            }
        }
    }

    @Override
    public boolean isTerminated() throws ProxyProvokeHandleException {
        try {
            if ( this.mDataSource != null ) {
                if ( this.mDataSource instanceof PooledDataSource ) {
                    return ((PooledDataSource) this.mDataSource).getPoolState().getActiveConnectionCount() == 0;
                }

                return this.mDataSource.getConnection().isClosed();
            }
            return true;
        }
        catch ( SQLException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }
}
