package com.pinecone.slime.jelly.source.ibatis.transaction;

import com.pinecone.framework.system.prototype.Pinenut;
import org.apache.ibatis.session.SqlSession;

public interface IbatisTransactionScope extends Pinenut {
    <T> T mapper( Class<T> mapperType );

    SqlSession sqlSession();
}
