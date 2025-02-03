package com.pinecone.hydra.account;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.Credential;
import com.pinecone.hydra.account.entity.Domain;
import com.pinecone.hydra.account.entity.ElementNode;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Group;
import com.pinecone.hydra.account.entity.Role;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.ulf.util.guid.GUID72;

import java.util.List;

public interface AccountManager extends KOMInstrument {
    AccountConfig KernelAccountConfig = new KernelAccountConfig();

    Account affirmAccount( String path );

    Group   affirmGroup( String path );

    Domain  affirmDomain( String path );

    void insertCredential( Credential credential );

    void insertRole(Role role);

    ElementNode queryElement( String path );

    void addChildren(GUID parentGuid, GUID childrenGuid);

    boolean containsChild( GUID parentGuid, String childName );

    List<GUID> queryAccountGuidByName(String userName);

    boolean queryAccountByGuid(GUID userGuid, String kernelCredential);

    void insertPrivilege(GenericPrivilege privilege);
    void removePrivilege(GUID privilegeGuid);

    Object queryPrivilege(GUID72 guid72);
    List<GenericPrivilege> queryPrivilegeByName(String name);

    List<GenericPrivilege> queryAllPrivileges();

    void updateRole(GenericRole role);
    GUID queryUserCredentialByGuid(GUID userGuid);
    boolean hasPermission(GUID userGuid, String requiredPrivilegeCode);
    void insertAuthorization(GenericAuthorization authorization);

    void removeAuthorizationByUserGuid(GUID userGuid);

    List<GenericAccount> queryAllAccount();

    List<GenericDomain> queryAllDomain();

    Group queryGroupByGroupGuid(GUID groupGuid);

    String queryDomainNameByGuid(GUID domainGuid);

}
