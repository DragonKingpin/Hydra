package com.pinecone.hydra.service.ibatis.hydranium;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.entity.ibatis.GUIDTypeHandler;
import com.pinecone.hydra.entity.ibatis.GUID128TypeHandler;
import com.pinecone.hydra.entity.ibatis.UOITypeHandler;
import com.pinecone.hydra.service.ibatis.ServiceTreeMapper;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransaction;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransactionCallback;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransactionScope;
import com.pinecone.ulf.util.guid.i128.UUID128;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceDirectoryPurgeTest {
    private final GUID guid = new UUID128( "019e795e-7a86-7ade-81ec-2a8383a7b93b" );
    private JdbcDataSource dataSource;
    private SqlSessionFactory sessions;
    private ServiceMasterManipulatorImpl manipulator;
    private int rollbacks;

    @Before
    public void setUp() throws Exception {
        dataSource = new JdbcDataSource();
        dataSource.setURL( "jdbc:h2:mem:directory-" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1" );
        try ( Connection connection = dataSource.getConnection();
              InputStream schema = resource( "service/deletion/schema.sql" ) ) {
            RunScript.execute( connection, new InputStreamReader( schema, StandardCharsets.UTF_8 ) );
        }
        Configuration config = new Configuration( new Environment( "test", new JdbcTransactionFactory(), dataSource ) );
        config.getTypeHandlerRegistry().register( GUID.class, GUIDTypeHandler.class );
        config.getTypeHandlerRegistry().register( UUID128.class, GUID128TypeHandler.class );
        config.getTypeHandlerRegistry().register( UOI.class, UOITypeHandler.class );
        for ( String name : new String[] { "ServiceTreeMapper", "ServiceNamespaceMapper", "ApplicationNodeMapper",
                "ServicePathCacheMapper", "ServiceNodeOwnerMapper" } ) {
            String path = "mapper/kernel/service/" + name + ".xml";
            try ( InputStream xml = resource( path ) ) {
                new XMLMapperBuilder( xml, config, path, config.getSqlFragments() ).parse();
            }
        }
        sessions = new SqlSessionFactoryBuilder().build( config );
        ServiceMappingTransaction transaction = new ServiceMappingTransaction() {
            @Override
            public <T> T required( ServiceMappingTransactionCallback<T> callback ) {
                try ( SqlSession session = sessions.openSession( false ) ) {
                    try {
                        T result = callback.execute( new ServiceMappingTransactionScope() {
                            @Override
                            public <M> M mapper( Class<M> type ) {
                                return session.getMapper( type );
                            }
                        } );
                        session.commit();
                        return result;
                    }
                    catch ( Exception exception ) {
                        session.rollback();
                        rollbacks++;
                        throw exception instanceof RuntimeException ? (RuntimeException) exception : new IllegalStateException( exception );
                    }
                }
            }
        };
        manipulator = new ServiceMasterManipulatorImpl() {
            @Override
            public ServiceMappingTransaction transaction() {
                return transaction;
            }
        };
    }

    @Test
    public void deletesApplicationAndAllTopologyAndAllowsPathReuse() throws Exception {
        seed( "hydra_service_application_node" );
        manipulator.purgeServiceDirectory( guid );
        assertDeleted( "hydra_service_application_node" );
        try ( SqlSession session = sessions.openSession() ) {
            assertNull( session.getMapper( ServiceTreeMapper.class ).getNode( guid ) );
        }
        seed( "hydra_service_application_node" );
        assertEquals( 1, count( "hydra_service_node_cache_path" ) );
    }

    @Test
    public void deletesNamespaceAndAllTopology() throws Exception {
        seed( "hydra_service_namespace_node" );
        manipulator.purgeServiceDirectory( guid );
        assertDeleted( "hydra_service_namespace_node" );
    }

    @Test
    public void rejectsChildAddedBeforeDeleteTransaction() throws Exception {
        seed( "hydra_service_application_node" );
        execute( "INSERT INTO hydra_service_node_tree(guid,parent_guid) VALUES('019e795e-7a86-7ade-81ec-2a8383a7b93c','" + guid + "')" );
        assertEquals( "Service directory is not empty.",
                assertThrows( IllegalArgumentException.class, () -> manipulator.purgeServiceDirectory( guid ) ).getMessage() );
        assertEquals( 1, count( "hydra_service_application_node" ) );
        assertEquals( 2, count( "hydra_service_node_tree" ) );
        assertEquals( 1, count( "hydra_service_node_cache_path" ) );
    }

    @Test
    public void rejectsServiceOrMissingNodeWithoutDeletingAnything() throws Exception {
        assertThrows( IllegalArgumentException.class, () -> manipulator.purgeServiceDirectory( guid ) );
        execute( "INSERT INTO hydra_service_nodes(guid) VALUES('" + guid + "')" );
        assertThrows( IllegalArgumentException.class, () -> manipulator.purgeServiceDirectory( guid ) );
        assertEquals( 1, count( "hydra_service_nodes" ) );
        assertThrows( IllegalArgumentException.class, () -> manipulator.purgeServiceDirectory( null ) );
    }

    @Test
    public void rollsBackTopologyWhenFinalEntityDeleteFails() throws Exception {
        seed( "hydra_service_application_node" );
        execute( "CREATE TABLE delete_guard(guid VARCHAR(82) REFERENCES hydra_service_application_node(guid))" );
        execute( "INSERT INTO delete_guard VALUES('" + guid + "')" );
        assertThrows( PersistenceException.class, () -> manipulator.purgeServiceDirectory( guid ) );
        assertEquals( 1, rollbacks );
        for ( String table : new String[] { "hydra_service_nodes", "hydra_service_node_tree",
                "hydra_service_node_cache_path", "hydra_service_application_node" } ) {
            assertEquals( table, 1, count( table ) );
        }
    }

    private void seed( String entityTable ) throws Exception {
        execute( "INSERT INTO hydra_service_nodes(guid) VALUES('" + guid + "')" );
        execute( "INSERT INTO hydra_service_node_tree(guid,linked_type) VALUES('" + guid + "','Owned')" );
        execute( "INSERT INTO hydra_service_node_cache_path(guid,path) VALUES('" + guid + "','app')" );
        execute( "INSERT INTO " + entityTable + "(guid,name) VALUES('" + guid + "','app')" );
    }

    private void assertDeleted( String entityTable ) throws Exception {
        for ( String table : new String[] { "hydra_service_nodes", "hydra_service_node_tree",
                "hydra_service_node_cache_path", entityTable } ) {
            assertEquals( table, 0, count( table ) );
        }
    }

    private InputStream resource( String path ) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream( path );
        assertNotNull( path, stream );
        return stream;
    }

    private void execute( String sql ) throws Exception {
        try ( Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement() ) {
            statement.execute( sql );
        }
    }

    private int count( String table ) throws Exception {
        try ( Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();
              ResultSet rows = statement.executeQuery( "SELECT COUNT(*) FROM " + table ) ) {
            rows.next();
            return rows.getInt( 1 );
        }
    }
}
