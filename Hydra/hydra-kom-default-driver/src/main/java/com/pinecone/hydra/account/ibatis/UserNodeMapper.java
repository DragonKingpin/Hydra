package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.account.source.UserNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface UserNodeMapper extends UserNodeManipulator {
    void insert(Account account);

    void remove(@Param("userGuid") GUID userGuid);

    GenericAccount queryUser(@Param("userGuid") GUID userGuid );

    List<GUID > getGuidsByName(@Param("name") String name );

    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    List<GenericAccount> queryAllAccount();

    GenericAccount queryAccountByName(@Param("userName") String userName);

    void update(Account account);

    GenericAccount queryAccountByUserGuid(@Param("userGuid") GUID userGuid);
}
