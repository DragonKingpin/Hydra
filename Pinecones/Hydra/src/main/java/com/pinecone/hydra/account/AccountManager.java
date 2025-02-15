package com.pinecone.hydra.account;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.ACNodeAllotment;
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
import com.pinecone.hydra.account.entity.Privilege;
import com.pinecone.hydra.account.entity.Role;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.ulf.util.guid.GUID72;

import java.util.List;

public interface AccountManager extends KOMInstrument {
    ACNodeAllotment getAllotment();
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

    void removeAuthorizationByGuid(GUID userGuid);

    void removeAuthorizationByUserGuid(GUID userGuid);

    List<GenericAccount> queryAllAccount();

    List<GenericDomain> queryAllDomain();

    Group queryGroupByGroupGuid(GUID groupGuid);

    String queryDomainNameByGuid(GUID domainGuid);

    List<GenericAuthorization> queryAllAuthorization();

    List<GenericRole> queryAllRoles();

    Account queryAccountByName(String userName);

    void updateAccount(Account account);

    Account queryAccountByUserGuid(GUID userGuid);

    Privilege queryPrivilegeByGuid(GUID guid);

    void updatePrivilege(Privilege privilege);

    void updateAuthorization(GUID guid72);

    void removeRole(int id);

    List<GenericAuthorization> queryAuthorizationByUserGuid(GUID userGuid);

    Domain queryDomainByGuid(GUID domainGuid);

    void updateDomain(Domain domain);

    void updateGroup(Group group);

}
