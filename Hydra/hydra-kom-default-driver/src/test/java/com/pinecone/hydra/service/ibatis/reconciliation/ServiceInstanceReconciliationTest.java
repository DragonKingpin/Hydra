package com.pinecone.hydra.service.ibatis.reconciliation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.entity.ibatis.GUID128TypeHandler;
import com.pinecone.hydra.entity.ibatis.GUIDTypeHandler;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.ibatis.ServiceInstanceMapper;
import com.pinecone.hydra.service.ibatis.ServiceRuntimeNodeMapper;
import com.pinecone.hydra.service.kom.ServiceInstanceQuery;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.hydra.service.kom.entity.GenericServiceRuntimeNodeEntity;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.event.InstanceLifecycleEvent;
import com.pinecone.hydra.service.registry.server.reconciliation.ServiceInstanceReconciliationConfig;
import com.pinecone.hydra.service.registry.server.reconciliation.ServiceInstanceObservation;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportType;
import com.pinecone.ulf.util.guid.i128.UUID128;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServiceInstanceReconciliationTest implements Pinenut {

    private static final LocalDateTime Now = LocalDateTime.of( 2026, 10, 15, 12, 0 );
    private final GUID serviceGuid = new UUID128( 1L, 1L );
    private final Set<Long> connectedClients = new HashSet<>();
    private SqlSession session;
    private ServiceInstanceMapper instances;
    private ServiceRuntimeNodeMapper nodes;
    private ServiceInstrument instrument;
    private ServiceControlTransport transport;
    private TestManager manager;

    @Before
    public void setUp() throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL( "jdbc:h2:mem:reconciliation-" + UUID.randomUUID() + ";MODE=MySQL" );
        Configuration configuration = new Configuration( new Environment( "test", new JdbcTransactionFactory(), dataSource ) );
        configuration.setLocalCacheScope( LocalCacheScope.STATEMENT );
        configuration.getTypeHandlerRegistry().register( GUID.class, GUIDTypeHandler.class );
        configuration.getTypeHandlerRegistry().register( UUID128.class, GUID128TypeHandler.class );
        for ( String name : new String[]{ "ServiceInstanceMapper", "ServiceRuntimeNodeMapper" } ) {
            String path = "mapper/kernel/service/" + name + ".xml";
            try ( InputStream xml = resource( path ) ) {
                new XMLMapperBuilder( xml, configuration, path, configuration.getSqlFragments() ).parse();
            }
        }
        session = new SqlSessionFactoryBuilder().build( configuration ).openSession( true );
        Connection connection = session.getConnection();
        try ( InputStream schema = resource( "service/reconciliation/schema.sql" ) ) {
            RunScript.execute( connection, new InputStreamReader( schema, StandardCharsets.UTF_8 ) );
        }
        instances = session.getMapper( ServiceInstanceMapper.class );
        nodes = session.getMapper( ServiceRuntimeNodeMapper.class );
        instrument = mock( ServiceInstrument.class );
        when( instrument.queryServiceInstance( any() ) ).thenAnswer( call -> instances.queryServiceInstance( call.getArgument( 0 ) ) );
        when( instrument.countServiceInstances( any() ) ).thenAnswer( call -> instances.countServiceInstances( call.getArgument( 0 ) ) );
        when( instrument.fetchOnlineServiceInstancesBefore( any(), anyLong(), anyInt() ) ).thenAnswer(
                call -> instances.fetchOnlineServiceInstancesBefore( call.getArgument( 0 ), call.getArgument( 1 ), call.getArgument( 2 ) ) );
        when( instrument.markServiceInstanceOfflineIfUnchanged( any(), any(), anyString() ) ).thenAnswer(
                call -> instances.markServiceInstanceOfflineIfUnchanged( call.getArgument( 0 ), call.getArgument( 1 ), call.getArgument( 2 ) ) );
        when( instrument.queryServiceRuntimeNode( any() ) ).thenAnswer( call -> nodes.queryServiceRuntimeNode( call.getArgument( 0 ) ) );
        doAnswer( call -> { nodes.refreshServiceRuntimeNodeRuntime( call.getArgument( 0 ) ); return null; } )
                .when( instrument ).refreshServiceRuntimeNodeRuntime( any() );
        transport = mock( ServiceControlTransport.class );
        when( transport.transportType() ).thenReturn( ServiceControlTransportType.Grpc );
        when( transport.isStarted() ).thenReturn( true );
        when( transport.containsClient( anyLong() ) ).thenAnswer( call -> connectedClients.contains( call.<Long>getArgument( 0 ) ) );
        manager = new TestManager( instrument, transport );
    }

    @After
    public void tearDown() {
        manager.stop();
        session.close();
    }

    @Test
    public void historicalOnlineBecomesOfflineOnlyAfterGrace() {
        ServiceInstanceEntry stale = seed( 1, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 29999 );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager.advance( 1 );
        manager.sweep();
        ServiceInstanceEntry offline = row( stale );
        assertEquals( "Offline", offline.getStatus() );
        assertEquals( manager.now, offline.getOfflineTime() );
        assertEquals( offline.getOfflineTime(), offline.getLatestEndTime() );
        assertTrue( offline.getStatusReason().contains( "no active registration session" ) );
        assertEquals( 0, manager.pending() );
    }

    @Test
    public void longRunningConnectedInstanceIsNotKilledForOldHeartbeat() {
        ServiceInstanceEntry active = seed( 2, Now.minusDays( 365 ) );
        connect( active );
        for ( int i = 0; i < 4; i++ ) {
            manager.sweep();
            manager.advance( 60000 );
        }
        assertEquals( "Online", row( active ).getStatus() );
        assertEquals( 0, manager.pending() );
    }

    @Test
    public void reconnectCancelsObservationAndNewDisconnectGetsFullGrace() {
        ServiceInstanceEntry stale = seed( 3, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 29000 );
        connect( stale );
        manager.sweep();
        assertEquals( 0, manager.pending() );
        connectedClients.clear();
        manager.advance( 2000 );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
        assertFalse( manager.hasOwnedInstance( stale.getGuid() ) );
    }

    @Test
    public void newInstanceOnSameNodeAndEndpointDoesNotRescueOldInstance() {
        ServiceInstanceEntry stale = seed( 4, Now.minusDays( 1 ) );
        ServiceInstanceEntry active = seed( 5, Now.minusHours( 1 ) );
        GUID nodeGuid = new UUID128( 3, 1 );
        for ( ServiceInstanceEntry entry : new ServiceInstanceEntry[]{ stale, active } ) {
            entry.setRuntimeNodeGuid( nodeGuid );
            instances.updateServiceInstance( entry );
        }
        GenericServiceRuntimeNodeEntity node = new GenericServiceRuntimeNodeEntity();
        node.setGuid( nodeGuid );
        node.setServiceGuid( serviceGuid );
        node.setNodeId( "same-node" );
        node.setStatus( "Online" );
        node.setLatestInstanceGuid( active.getGuid() );
        nodes.createServiceRuntimeNode( node );
        connect( active );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
        assertEquals( "Online", row( active ).getStatus() );
        assertEquals( "Online", nodes.queryServiceRuntimeNode( nodeGuid ).getStatus() );
        assertTrue( manager.hasOwnedInstance( active.getGuid() ) );
    }

    @Test
    public void recentRegistrationAndRecentResumeAreExcludedBySql() {
        ServiceInstanceEntry recent = seed( 6, Now.minusSeconds( 10 ) );
        ServiceInstanceEntry resumed = seed( 7, Now.minusDays( 1 ) );
        resumed.setLatestStartTime( Now );
        instances.updateServiceInstance( resumed );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( 0, manager.pending() );
        assertEquals( "Online", row( recent ).getStatus() );
        assertEquals( "Online", row( resumed ).getStatus() );
    }

    @Test
    public void paginationProgressesBeyondFourPagesAndEventuallyWraps() {
        for ( int i = 1; i <= 507; i++ ) {
            seed( i, Now.minusDays( 1 ) );
        }
        manager.sweep();
        assertEquals( 400, manager.pending() );
        assertEquals( 400, manager.cursor() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( 107, countOnline() );
        assertEquals( 107, manager.pending() );
        assertEquals( 0, manager.cursor() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( 0, countOnline() );
    }

    @Test
    public void repeatedScansDoNotExtendFirstMissingDeadline() {
        seed( 8, Now.minusHours( 1 ) );
        manager.sweep();
        for ( int i = 0; i < 3; i++ ) {
            manager.advance( 10000 );
            manager.sweep();
        }
        assertEquals( 0, countOnline() );
    }

    @Test
    public void changedRegistrationResetsObservation() {
        ServiceInstanceEntry stale = seed( 9, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 30000 );
        ServiceInstanceEntry resumed = row( stale );
        resumed.setConnectionCount( 2 );
        instances.updateServiceInstance( resumed );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
    }

    @Test
    public void sqlCompareAndSetRejectsConcurrentResumeAndStatusChange() {
        ServiceInstanceEntry original = seed( 10, Now.minusDays( 1 ) );
        ServiceInstanceEntry resumed = row( original );
        resumed.setClientId( 10000L );
        instances.updateServiceInstance( resumed );
        assertEquals( 0, instances.markServiceInstanceOfflineIfUnchanged( original, Now, "test" ) );
        resumed.setClientId( original.getClientId() );
        resumed.setConnectionCount( 2 );
        instances.updateServiceInstance( resumed );
        assertEquals( 0, instances.markServiceInstanceOfflineIfUnchanged( original, Now, "test" ) );
        resumed.setConnectionCount( 1 );
        resumed.setLatestStartTime( Now );
        instances.updateServiceInstance( resumed );
        assertEquals( 0, instances.markServiceInstanceOfflineIfUnchanged( original, Now, "test" ) );
        resumed.setLatestStartTime( original.getLatestStartTime() );
        resumed.setStatus( "Detached" );
        instances.updateServiceInstance( resumed );
        assertEquals( 0, instances.markServiceInstanceOfflineIfUnchanged( original, Now, "test" ) );
    }

    @Test
    public void queryFailureIsNotProofOfDeathAndNextTickRecovers() {
        ServiceInstanceEntry stale = seed( 11, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 30000 );
        when( instrument.queryServiceInstance( stale.getGuid() ) ).thenThrow( new IllegalStateException( "DB unavailable" ) );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        doAnswer( call -> instances.queryServiceInstance( call.getArgument( 0 ) ) )
                .when( instrument ).queryServiceInstance( stale.getGuid() );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
    }

    @Test
    public void transportFailureAndUnavailableTransportCannotKillInstances() {
        ServiceInstanceEntry stale = seed( 12, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 30000 );
        when( transport.containsClient( anyLong() ) ).thenThrow( new IllegalStateException( "Transport unavailable" ) );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        when( transport.isStarted() ).thenReturn( false );
        manager.sweep();
        assertEquals( 0, manager.pending() );
    }

    @Test
    public void unknownTransportAndIncompleteConnectedRegistrationArePreserved() {
        ServiceInstanceEntry unknown = seed( 13, Now.minusDays( 1 ) );
        unknown.setTransportType( "Other" );
        instances.updateServiceInstance( unknown );
        ServiceInstanceEntry pending = seed( 14, Now.minusDays( 1 ) );
        connectedClients.add( pending.getClientId() );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( 2, countOnline() );
    }

    @Test
    public void restartAndStopDoNotCarryExpiredObservationIntoNewLifecycle() {
        ServiceInstanceEntry stale = seed( 15, Now.minusDays( 1 ) );
        manager.sweep();
        manager.advance( 30000 );
        manager.stop();
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager = new TestManager( instrument, transport );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
    }

    @Test
    public void configurationIsOptInAndRequiresExclusiveRegistry() {
        assertFalse( new ServiceInstanceReconciliationConfig().isEnable() );
        JSONObject unsafe = new JSONMaptron();
        unsafe.put( "enable", true );
        assertThrows( IllegalArgumentException.class, () -> new ServiceInstanceReconciliationConfig( unsafe ) );
        unsafe.put( "exclusiveRegistry", true );
        unsafe.put( "missingGraceMillis", 0 );
        assertThrows( IllegalArgumentException.class, () -> new ServiceInstanceReconciliationConfig( unsafe ) );
        manager.stop();
        manager.configureInstanceReconciliation( new ServiceInstanceReconciliationConfig() );
        manager.startScheduler();
        assertFalse( manager.started() );
    }

    @Test
    public void concurrentRegistrationBetweenReadAndWriteIsNotOverwritten() {
        ServiceInstanceEntry stale = seed( 16, Now.minusDays( 1 ) );
        manager.sweep();
        when( instrument.markServiceInstanceOfflineIfUnchanged( any(), any(), anyString() ) ).thenAnswer( call -> {
            ServiceInstanceEntry resumed = row( stale );
            resumed.setConnectionCount( 2 );
            resumed.setClientId( 16000L );
            instances.updateServiceInstance( resumed );
            return instances.markServiceInstanceOfflineIfUnchanged( call.getArgument( 0 ), call.getArgument( 1 ), call.getArgument( 2 ) );
        } );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        assertEquals( Long.valueOf( 16000L ), row( stale ).getClientId() );
        assertNull( row( stale ).getOfflineTime() );
    }

    @Test
    public void failedConditionalUpdateDoesNotKillSchedulerOrRemoveRuntimeState() {
        ServiceInstanceEntry stale = seed( 17, Now.minusDays( 1 ) );
        connect( stale );
        connectedClients.clear();
        manager.sweep();
        when( instrument.markServiceInstanceOfflineIfUnchanged( any(), any(), anyString() ) )
                .thenThrow( new IllegalStateException( "DB write failed" ) );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Online", row( stale ).getStatus() );
        assertTrue( manager.hasOwnedInstance( stale.getGuid() ) );
        doAnswer( call -> instances.markServiceInstanceOfflineIfUnchanged( call.getArgument( 0 ), call.getArgument( 1 ), call.getArgument( 2 ) ) )
                .when( instrument ).markServiceInstanceOfflineIfUnchanged( any(), any(), anyString() );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
        assertFalse( manager.hasOwnedInstance( stale.getGuid() ) );
    }

    @Test
    public void failedPageQueryRetriesOnLaterSweep() {
        seed( 18, Now.minusDays( 1 ) );
        when( instrument.fetchOnlineServiceInstancesBefore( any(), anyLong(), anyInt() ) )
                .thenThrow( new IllegalStateException( "DB query failed" ) );
        manager.sweep();
        assertEquals( 1, countOnline() );
        doAnswer( call -> instances.fetchOnlineServiceInstancesBefore( call.getArgument( 0 ), call.getArgument( 1 ), call.getArgument( 2 ) ) )
                .when( instrument ).fetchOnlineServiceInstancesBefore( any(), anyLong(), anyInt() );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( 0, countOnline() );
    }

    @Test
    public void latestStaleInstanceDoesNotOfflineNodeWithOlderLiveInstance() {
        ServiceInstanceEntry active = seed( 19, Now.minusDays( 1 ) );
        ServiceInstanceEntry stale = seed( 20, Now.minusHours( 1 ) );
        GUID nodeGuid = attachNode( active, stale );
        connect( active );
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Online", nodes.queryServiceRuntimeNode( nodeGuid ).getStatus() );
        assertEquals( "Offline", row( stale ).getStatus() );
        connectedClients.clear();
        manager.sweep();
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", nodes.queryServiceRuntimeNode( nodeGuid ).getStatus() );
    }

    @Test
    public void nodeUpdateFailureIsRetriedAfterInstanceWasSettled() {
        ServiceInstanceEntry stale = seed( 21, Now.minusDays( 1 ) );
        GUID nodeGuid = attachNode( stale );
        manager.sweep();
        doThrow( new IllegalStateException( "Node write failed" ) ).when( instrument ).refreshServiceRuntimeNodeRuntime( any() );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
        assertEquals( "Online", nodes.queryServiceRuntimeNode( nodeGuid ).getStatus() );
        assertEquals( 1, manager.pending() );
        doAnswer( call -> { nodes.refreshServiceRuntimeNodeRuntime( call.getArgument( 0 ) ); return null; } )
                .when( instrument ).refreshServiceRuntimeNodeRuntime( any() );
        manager.sweep();
        assertEquals( "Offline", nodes.queryServiceRuntimeNode( nodeGuid ).getStatus() );
        assertEquals( 0, manager.pending() );
    }

    @Test
    public void reusedClientIdDoesNotRemoveNewInstancesRuntimeIndex() {
        ServiceInstanceEntry stale = seed( 22, Now.minusDays( 1 ) );
        manager.sweep();
        ServiceInstanceEntry active = seed( 23, Now.minusHours( 1 ) );
        active.setClientId( stale.getClientId() );
        instances.updateServiceInstance( active );
        connect( active );
        manager.advance( 30000 );
        manager.sweep();
        assertEquals( "Offline", row( stale ).getStatus() );
        assertEquals( "Online", row( active ).getStatus() );
        assertTrue( manager.hasOwnedInstance( active.getGuid() ) );
        assertTrue( manager.transportRegistry().hasClient( active.getClientId() ) );
    }

    @Test
    public void actualScheduledExecutorCompletesFiveStartStopRounds() throws Exception {
        manager.stop();
        manager.realClock = true;
        JSONObject config = new JSONMaptron();
        config.put( "enable", true );
        config.put( "exclusiveRegistry", true );
        config.put( "scanIntervalMillis", 25 );
        config.put( "minimumOnlineAgeMillis", 1 );
        config.put( "missingGraceMillis", 80 );
        manager.configureInstanceReconciliation( new ServiceInstanceReconciliationConfig( config ) );
        for ( int round = 0; round < 5; round++ ) {
            CountDownLatch settled = new CountDownLatch( 3 );
            manager.addRegisterEventHandler( ( clientId, instanceId, serviceId, event, cause ) -> {
                if ( event == InstanceLifecycleEvent.Offline ) {
                    settled.countDown();
                }
            } );
            for ( int i = 1; i <= 3; i++ ) {
                seed( 2000 + round * 10 + i, LocalDateTime.now().minusDays( 1 ) );
            }
            manager.startService();
            try {
                assertTrue( "Scheduled reconciliation round " + round, settled.await( 5, TimeUnit.SECONDS ) );
            }
            finally {
                manager.terminateService();
            }
            assertEquals( 0, countOnline() );
        }
    }

    @Test
    public void oldScheduledGenerationCannotActAfterRestart() {
        ServiceInstanceEntry stale = seed( 24, Now.minusDays( 1 ) );
        long oldGeneration = manager.generation();
        manager.sweep();
        manager.stop();
        manager.startScheduler();
        manager.advance( 60000 );
        manager.sweepGeneration( oldGeneration );
        assertEquals( 0, manager.pending() );
        assertEquals( "Online", row( stale ).getStatus() );
        manager.sweep();
        assertEquals( 1, manager.pending() );
    }

    @Test
    public void observationRetainsFingerprintWhenProviderReusesMutableEntity() {
        ServiceInstanceEntry entry = seed( 25, Now.minusDays( 1 ) );
        ServiceInstanceObservation observation = new ServiceInstanceObservation( entry, 0 );
        assertTrue( observation.matchesRegistration( entry ) );
        entry.setConnectionCount( entry.getConnectionCount() + 1 );
        assertFalse( observation.matchesRegistration( entry ) );
    }

    private GUID attachNode( ServiceInstanceEntry... entries ) {
        GUID nodeGuid = new UUID128( 3, 2 );
        for ( ServiceInstanceEntry entry : entries ) {
            entry.setRuntimeNodeGuid( nodeGuid );
            instances.updateServiceInstance( entry );
        }
        GenericServiceRuntimeNodeEntity node = new GenericServiceRuntimeNodeEntity();
        node.setGuid( nodeGuid );
        node.setServiceGuid( serviceGuid );
        node.setNodeId( "same-node" );
        node.setStatus( "Online" );
        node.setLatestInstanceGuid( entries[entries.length - 1].getGuid() );
        nodes.createServiceRuntimeNode( node );
        return nodeGuid;
    }

    private GenericServiceInstanceEntity seed( int number, LocalDateTime start ) {
        GenericServiceInstanceEntity entry = new GenericServiceInstanceEntity();
        entry.setGuid( new UUID128( 2L, number ) );
        entry.setServiceGuid( serviceGuid );
        entry.setClientId( (long) number );
        entry.setTransportType( "Grpc" );
        entry.setEndpointAddress( "http://same-node:5182/api" );
        entry.setStatus( "Online" );
        entry.setRegisterTime( start );
        entry.setLatestStartTime( start );
        entry.setLastHeartbeatTime( start );
        entry.setConnectionCount( 1 );
        instances.initServiceInstance( entry );
        return entry;
    }

    private ServiceInstanceEntry row( ServiceInstanceEntry entry ) {
        return instances.queryServiceInstance( entry.getGuid() );
    }

    private long countOnline() {
        ServiceInstanceQuery query = new ServiceInstanceQuery();
        query.setStatus( "Online" );
        return instances.countServiceInstances( query );
    }

    private void connect( ServiceInstanceEntry entry ) {
        connectedClients.add( entry.getClientId() );
        ServiceInstance instance = mock( ServiceInstance.class );
        USII usii = mock( USII.class );
        when( usii.getServiceId() ).thenReturn( entry.getServiceGuid() );
        when( usii.getClientId() ).thenReturn( entry.getClientId() );
        when( instance.getUSII() ).thenReturn( usii );
        when( instance.getId() ).thenReturn( entry.getGuid() );
        when( instance.getServiceId() ).thenReturn( entry.getServiceGuid() );
        manager.registerServiceInstance( instance );
    }

    private InputStream resource( String path ) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream( path );
        assertNotNull( path, stream );
        return stream;
    }

    private static class TestManager extends UniformServiceManager {
        private LocalDateTime now = Now;
        private long nanos;
        private boolean realClock;

        TestManager( ServiceInstrument instrument, ServiceControlTransport transport ) {
            super( instrument );
            JSONObject config = new JSONMaptron();
            config.put( "enable", true );
            config.put( "exclusiveRegistry", true );
            configureInstanceReconciliation( new ServiceInstanceReconciliationConfig( config ) );
            transportRegistry().hookTransport( transport );
            mInstanceReconciliationStarted = true;
        }

        @Override
        protected LocalDateTime reconciliationTime() {
            if ( realClock ) {
                return super.reconciliationTime();
            }
            return now;
        }

        @Override
        protected long reconciliationNanos() {
            if ( realClock ) {
                return super.reconciliationNanos();
            }
            return nanos;
        }

        void advance( long millis ) {
            now = now.plusNanos( TimeUnit.MILLISECONDS.toNanos( millis ) );
            nanos += TimeUnit.MILLISECONDS.toNanos( millis );
        }

        void sweep() { sweepServiceInstanceReconciliation(); }
        void stop() { stopInstanceReconciliation(); }
        void startScheduler() { startInstanceReconciliation(); }
        boolean started() { return mInstanceReconciliationStarted; }
        int pending() { return mInstanceObservations.size(); }
        long cursor() { return mInstanceReconciliationCursor; }
        long generation() { return mInstanceReconciliationGeneration; }
        void sweepGeneration( long generation ) { sweepServiceInstanceReconciliation( generation ); }
    }
}
