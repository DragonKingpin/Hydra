package com.sauron.shadow.chronicle;

import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.sauron.radium.heistron.Heistum;
import com.sauron.shadow.chronicle.dao.BasicChronicleManipulator;
import org.apache.ibatis.session.SqlSession;

public interface Chronicle extends Heistum {
    IbatisClient getPrimaryDataIbatisClient();

    SqlSession getPrimarySharedSqlSession();

    BasicChronicleManipulator getBasicChronicleManipulator();
}
