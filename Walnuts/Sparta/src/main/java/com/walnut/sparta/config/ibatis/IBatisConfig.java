package com.walnut.sparta.config.ibatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan( "com.walnut.sparta.services.mapper" )
@MapperScan( {
        "com.pinecone.hydra.config.ibatis", "com.pinecone.hydra.service.ibatis", "com.pinecone.hydra.task.ibatis",
        "com.pinecone.hydra.scenario.ibatis", "com.pinecone.hydra.deploy.ibatis"
} )
public class IBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeHandlersPackage( this.getClass().getPackageName() );
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources( "classpath*:mapper/*.xml" )); // 指定Mapper XML的位置
        return factoryBean.getObject();
    }
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
