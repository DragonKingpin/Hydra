package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.slime.source.rdb.RDBQuerierDataManipulator;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IbatisManipulatorProxyMapperFactory implements Pinenut {
    static class ManipulatorProxyHandler<T extends RDBQuerierDataManipulator > implements InvocationHandler {
        private final T original;
        private final SqlSession sqlSession;

        public ManipulatorProxyHandler( T original, SqlSession sqlSession ) {
            this.original   = original;
            this.sqlSession = sqlSession;
        }

        @Override
        public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
            if ( "commit".equals( method.getName() ) ) {
                this.sqlSession.commit();
                return null;
            }
            return method.invoke( this.original, args );
        }
    }


    @SuppressWarnings("unchecked")
    public static <T extends RDBQuerierDataManipulator > T getMapper( SqlSession sqlSession, Class<T > clazz ) {
        T original = sqlSession.getMapper(clazz);
        Class<? >[] interfaces = original.getClass().getInterfaces();

        return (T) Proxy.newProxyInstance(
                original.getClass().getClassLoader(),
                interfaces,
                new ManipulatorProxyHandler<>(original, sqlSession)
        );
    }
}
