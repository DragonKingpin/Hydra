package com.pinecone.hydra.business.ibatis.tag;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericScenarioElement;
import com.pinecone.hydra.business.ibatis.NodeMapper;
import com.pinecone.hydra.business.ibatis.ScenarioMapper;
import com.pinecone.hydra.business.ibatis.TreeMapper;
import com.pinecone.hydra.business.ibatis.hydranium.MasterManipulatorImpl;
import com.pinecone.hydra.entity.ibatis.GUIDTypeHandler;
import com.pinecone.hydra.entity.ibatis.GUID128TypeHandler;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransaction;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionCallback;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionScope;
import com.pinecone.ulf.util.guid.i128.UUID128;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;

/** Opt-in smoke test; creates and removes only its own randomly identified node. */
public class BusinessNodeTagsMySqlTest {
    @Test
    public void migratedSchemaSupportsUnicodeMetadataAndTagLifecycle() throws Exception {
        String url = System.getenv( "BUSINESS_MYSQL_URL" );
        Assume.assumeTrue( url != null && !url.isEmpty() );
        UnpooledDataSource database = new UnpooledDataSource( "com.mysql.cj.jdbc.Driver", url,
                System.getenv( "BUSINESS_MYSQL_USER" ), System.getenv( "BUSINESS_MYSQL_PASSWORD" ) );
        Configuration config = new Configuration( new Environment( "mysql-smoke", new JdbcTransactionFactory(), database ) );
        config.getTypeHandlerRegistry().register( GUID.class, GUIDTypeHandler.class );
        config.getTypeHandlerRegistry().register( UUID128.class, GUID128TypeHandler.class );
        for ( String name : new String[] { "NodeMapper", "TreeMapper", "PathMapper", "ScenarioMapper", "ProjectMapper", "IdeaMapper", "tag/NodeTagMapper" } ) {
            String path = "mapper/kernel/business/" + name + ".xml";
            try ( InputStream xml = getClass().getClassLoader().getResourceAsStream( path ) ) {
                new XMLMapperBuilder( xml, config, path, config.getSqlFragments() ).parse();
            }
        }
        SqlSessionFactory sessions = new SqlSessionFactoryBuilder().build( config );
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
                    catch ( Exception error ) { session.rollback(); throw new IllegalStateException( error ); }
                }
            }
        };
        MasterManipulatorImpl master = new MasterManipulatorImpl() {
            @Override protected IbatisTransaction transaction() { return transaction; }
        };
        GenericScenarioElement node = new GenericScenarioElement();
        node.setGuid( new UUID128( UUID.randomUUID().toString() ) );
        node.setName( "codex-tags-smoke-" + node.getGuid() );
        node.setCreateTime( java.time.LocalDateTime.now() );
        node.setUpdateTime( node.getCreateTime() );
        node.setExtraInformation( "unicode-\ud83d\ude80" );
        transaction.required( scope -> {
            scope.mapper( NodeMapper.class ).insert( node );
            scope.mapper( ScenarioMapper.class ).insert( node );
            scope.mapper( TreeMapper.class ).insertOwned( node.getGuid(), null );
            return null;
        } );
        try {
            master.replaceNodeTags( node.getGuid(), List.of( "codex-smoke", "CODEX-SMOKE", "\u6838\u5fc3" ) );
            try ( SqlSession session = sessions.openSession() ) {
                assertEquals( node.getExtraInformation(), session.getMapper( NodeMapper.class ).get( node.getGuid() ).getExtraInformation() );
                assertEquals( 3, session.getMapper( NodeTagMapper.class ).fetchByNodeGuids( List.of( node.getGuid() ) ).size() );
            }
            master.replaceNodeTags( node.getGuid(), List.of() );
            try ( SqlSession session = sessions.openSession() ) {
                assertTrue( session.getMapper( NodeTagMapper.class ).fetchByNodeGuids( List.of( node.getGuid() ) ).isEmpty() );
            }
        }
        finally { master.removeElement( node.getGuid() ); }
        try ( SqlSession session = sessions.openSession() ) {
            assertNull( session.getMapper( NodeMapper.class ).get( node.getGuid() ) );
            assertTrue( session.getMapper( NodeTagMapper.class ).fetchByNodeGuids( List.of( node.getGuid() ) ).isEmpty() );
        }
    }
}
