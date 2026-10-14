package com.pinecone.hydra.business.ibatis.tag;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.tag.NodeTag;
import com.pinecone.hydra.business.ibatis.hydranium.MasterManipulatorImpl;
import com.pinecone.hydra.business.support.tag.BusinessTags;
import com.pinecone.hydra.entity.ibatis.GUIDTypeHandler;
import com.pinecone.hydra.entity.ibatis.GUID128TypeHandler;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransaction;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionCallback;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionScope;
import com.pinecone.ulf.util.guid.i128.UUID128;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessNodeTagsTest {
    private final GUID guid = new UUID128( "019e795e-7a86-7ade-81ec-2a8383a7b93b" );
    private JdbcDataSource database;
    private SqlSessionFactory sessions;
    private MasterManipulatorImpl master;

    @Before
    public void setup() throws Exception {
        database = new JdbcDataSource();
        database.setURL( "jdbc:h2:mem:tags-" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=5000" );
        execute( "CREATE TABLE hydra_business_nodes (id BIGINT AUTO_INCREMENT PRIMARY KEY, guid VARCHAR(82) UNIQUE, type VARCHAR(64), name VARCHAR(330), code VARCHAR(128), description VARCHAR(1024), owner VARCHAR(128), extra_information TEXT, create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)" );
        execute( "CREATE TABLE hydra_business_node_tags (id BIGINT AUTO_INCREMENT PRIMARY KEY, node_guid VARCHAR(82), tag VARCHAR(32), create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, UNIQUE(node_guid,tag))" );
        execute( "CREATE TABLE hydra_business_node_tree (guid VARCHAR(82), parent_guid VARCHAR(82), id BIGINT AUTO_INCREMENT PRIMARY KEY)" );
        execute( "CREATE TABLE hydra_business_node_cache_path (guid VARCHAR(82))" );
        execute( "CREATE TABLE hydra_business_scenario_node (guid VARCHAR(82) PRIMARY KEY)" );
        Configuration config = new Configuration( new Environment( "tags", new JdbcTransactionFactory(), database ) );
        config.getTypeHandlerRegistry().register( GUID.class, GUIDTypeHandler.class );
        config.getTypeHandlerRegistry().register( UUID128.class, GUID128TypeHandler.class );
        for ( String name : new String[] { "NodeMapper", "TreeMapper", "PathMapper", "ScenarioMapper", "ProjectMapper", "IdeaMapper", "tag/NodeTagMapper" } ) {
            String path = "mapper/kernel/business/" + name + ".xml";
            try ( InputStream xml = getClass().getClassLoader().getResourceAsStream( path ) ) {
                assertNotNull( path, xml );
                new XMLMapperBuilder( xml, config, path, config.getSqlFragments() ).parse();
            }
        }
        sessions = new SqlSessionFactoryBuilder().build( config );
        IbatisTransaction transaction = new IbatisTransaction() {
            public <T> T required( IbatisTransactionCallback<T> callback ) {
                try ( SqlSession session = sessions.openSession( false ) ) {
                    try {
                        T result = callback.execute( new IbatisTransactionScope() {
                            public <M> M mapper( Class<M> type ) { return session.getMapper( type ); }
                            public SqlSession sqlSession() { return session; }
                        } );
                        session.commit();
                        return result;
                    }
                    catch ( Exception error ) {
                        session.rollback();
                        throw error instanceof RuntimeException ? (RuntimeException) error : new IllegalStateException( error );
                    }
                }
            }
        };
        master = new MasterManipulatorImpl() {
            @Override protected IbatisTransaction transaction() { return transaction; }
        };
        execute( "INSERT INTO hydra_business_nodes(guid,type,name) VALUES ('" + guid + "','Scenario','test')" );
        execute( "INSERT INTO hydra_business_scenario_node(guid) VALUES ('" + guid + "')" );
        execute( "INSERT INTO hydra_business_node_tree(guid) VALUES ('" + guid + "')" );
    }

    @Test
    public void normalizesDeduplicatesAndPreservesUnchangedBindings() throws Exception {
        master.replaceNodeTags( guid, List.of( " finance ", "finance", "Finance", "\u6838\u5fc3" ) );
        assertEquals( Set.of( "finance", "Finance", "\u6838\u5fc3" ), tags() );
        long id = scalar( "SELECT id FROM hydra_business_node_tags WHERE tag='finance'" );
        execute( "UPDATE hydra_business_nodes SET update_time='2000-01-01 00:00:00'" );
        master.replaceNodeTags( guid, List.of( "finance", "Finance", "\u6838\u5fc3" ) );
        assertEquals( id, scalar( "SELECT id FROM hydra_business_node_tags WHERE tag='finance'" ) );
        assertEquals( 1, scalar( "SELECT COUNT(*) FROM hydra_business_nodes WHERE update_time='2000-01-01 00:00:00'" ) );
        master.replaceNodeTags( guid, List.of( "finance", "new" ) );
        assertEquals( id, scalar( "SELECT id FROM hydra_business_node_tags WHERE tag='finance'" ) );
        master.replaceNodeTags( guid, List.of() );
        assertTrue( tags().isEmpty() );
    }

    @Test
    public void rejectsInvalidTagsAndMissingNodes() {
        assertThrows( IllegalArgumentException.class, () -> master.replaceNodeTags( guid, null ) );
        for ( String tag : List.of( " ", "x".repeat( 33 ), "x\ny", "\ud83d\ude00" ) ) {
            assertThrows( IllegalArgumentException.class, () -> master.replaceNodeTags( guid, List.of( tag ) ) );
        }
        assertThrows( IllegalArgumentException.class, () -> master.replaceNodeTags( null, List.of( "test" ) ) );
        assertThrows( IllegalArgumentException.class, () -> BusinessTags.normalizeAll(
                java.util.stream.IntStream.range( 0, 17 ).mapToObj( String::valueOf ).collect( Collectors.toList() ) ) );
        assertTrue( tags().isEmpty() );
    }

    @Test
    public void supportsLiteralPrefixesAndPagedExactTagSearch() {
        master.replaceNodeTags( guid, List.of( "a_b", "axb", "a%b" ) );
        try ( SqlSession session = sessions.openSession() ) {
            NodeTagMapper mapper = session.getMapper( NodeTagMapper.class );
            assertEquals( List.of( "a_b" ), mapper.fetchCandidates( BusinessTags.prefixPattern( "a_" ), 30 ) );
            assertEquals( List.of( "a%b" ), mapper.fetchCandidates( BusinessTags.prefixPattern( "a%" ), 30 ) );
            assertEquals( 1, mapper.countNodes( "a_b" ) );
            assertEquals( 0, mapper.countNodes( "A_b" ) );
            assertEquals( 1, mapper.findNodeGuids( "a_b", 0, 20 ).size() );
            assertTrue( mapper.findNodeGuids( "a_b", 1, 20 ).isEmpty() );
        }
    }

    @Test
    public void rollsBackTagDeletionWhenNodeDeletionFails() throws Exception {
        master.replaceNodeTags( guid, List.of( "protected" ) );
        execute( "CREATE TABLE deletion_guard (guid VARCHAR(82) REFERENCES hydra_business_scenario_node(guid))" );
        execute( "INSERT INTO deletion_guard(guid) VALUES ('" + guid + "')" );
        assertThrows( RuntimeException.class, () -> master.removeElement( guid ) );
        assertEquals( Set.of( "protected" ), tags() );
        assertEquals( 1, scalar( "SELECT COUNT(*) FROM hydra_business_nodes" ) );
    }

    @Test
    public void rejectsDeletingNonLeafAndCleansLeafTags() throws Exception {
        master.replaceNodeTags( guid, List.of( "test" ) );
        execute( "INSERT INTO hydra_business_node_tree(guid,parent_guid) VALUES ('019e795e-7a86-7ade-81ec-2a8383a7b93c','" + guid + "')" );
        assertThrows( IllegalArgumentException.class, () -> master.removeElement( guid ) );
        assertEquals( Set.of( "test" ), tags() );
        execute( "DELETE FROM hydra_business_node_tree WHERE guid='019e795e-7a86-7ade-81ec-2a8383a7b93c'" );
        master.removeElement( guid );
        assertTrue( tags().isEmpty() );
        assertEquals( 0, scalar( "SELECT COUNT(*) FROM hydra_business_nodes" ) );
    }

    @Test
    public void concurrentReplacementsDoNotMergeIndependentRequests() throws Exception {
        runConcurrently( () -> master.replaceNodeTags( guid, List.of( "a", "b" ) ),
                () -> master.replaceNodeTags( guid, List.of( "c", "d" ) ) );
        assertTrue( tags().equals( Set.of( "a", "b" ) ) || tags().equals( Set.of( "c", "d" ) ) );
    }

    @Test
    public void concurrentDeleteAndTagWriteLeaveNoOrphans() throws Exception {
        runConcurrently( () -> master.removeElement( guid ), () -> {
            try { master.replaceNodeTags( guid, List.of( "race" ) ); }
            catch ( IllegalArgumentException deleted ) { assertEquals( "Business node does not exist.", deleted.getMessage() ); }
        } );
        assertTrue( tags().isEmpty() );
        assertEquals( 0, scalar( "SELECT COUNT(*) FROM hydra_business_nodes" ) );
    }

    private void runConcurrently( Runnable left, Runnable right ) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool( 2 );
        CountDownLatch start = new CountDownLatch( 1 );
        try {
            Future<?> first = pool.submit( () -> { start.await(); left.run(); return null; } );
            Future<?> second = pool.submit( () -> { start.await(); right.run(); return null; } );
            start.countDown();
            first.get( 10, java.util.concurrent.TimeUnit.SECONDS );
            second.get( 10, java.util.concurrent.TimeUnit.SECONDS );
        }
        finally { pool.shutdownNow(); }
    }

    private Set<String> tags() {
        try ( SqlSession session = sessions.openSession() ) {
            return session.getMapper( NodeTagMapper.class ).fetchByNodeGuids( List.of( guid ) ).stream()
                    .map( NodeTag::getTag ).collect( Collectors.toSet() );
        }
    }
    private void execute( String sql ) throws Exception {
        try ( Connection connection = database.getConnection(); Statement statement = connection.createStatement() ) { statement.execute( sql ); }
    }
    private long scalar( String sql ) throws Exception {
        try ( Connection connection = database.getConnection(); Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery( sql ) ) {
            result.next(); return result.getLong( 1 );
        }
    }
}
