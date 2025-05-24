package com.pinecone.hydra.account;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.account.entity.ACNodeAllotment;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.Credential;
import com.pinecone.hydra.account.entity.Domain;
import com.pinecone.hydra.account.entity.ElementNode;
import com.pinecone.hydra.account.entity.GenericACNodeAllotment;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.entity.GenericGroup;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Group;
import com.pinecone.hydra.account.entity.Privilege;
import com.pinecone.hydra.account.entity.Role;
import com.pinecone.hydra.account.source.AuthorizationManipulator;
import com.pinecone.hydra.account.source.CredentialManipulator;
import com.pinecone.hydra.account.source.PrivilegeManipulator;
import com.pinecone.hydra.account.source.RoleManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.MultiFolderPathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.account.operator.GenericAccountOperatorFactory;
import com.pinecone.hydra.account.source.DomainNodeManipulator;
import com.pinecone.hydra.account.source.GroupNodeManipulator;
import com.pinecone.hydra.account.source.UserMasterManipulator;
import com.pinecone.hydra.account.source.UserNodeManipulator;
import com.pinecone.ulf.util.guid.i64.GUID72;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UniformAccountManager extends ArchKOMTree implements AccountManager {
    protected UserMasterManipulator             userMasterManipulator;

    protected GroupNodeManipulator              groupNodeManipulator;

    protected UserNodeManipulator               userNodeManipulator;

    protected DomainNodeManipulator             domainNodeManipulator;

    protected CredentialManipulator             credentialManipulator;

    protected AuthorizationManipulator          authorizationManipulator;

    protected PrivilegeManipulator              privilegeManipulator;
    protected RoleManipulator                   roleManipulator;

    protected List<GUIDNameManipulator >        folderManipulators;

    protected List<GUIDNameManipulator >        fileManipulators;

    protected ACNodeAllotment                   acNodeAllotment;


    public UniformAccountManager( Processum superiorProcess, KOIMasterManipulator masterManipulator, AccountManager parent, String name, String superiorPathScope ) {
        super( superiorProcess, masterManipulator, KernelAccountConfig, parent, name, superiorPathScope );
        this.userMasterManipulator = (UserMasterManipulator) masterManipulator;
        this.pathResolver          = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator         = GUIDs.newGuidAllocator();

        this.operatorFactory            = new GenericAccountOperatorFactory( this, this.userMasterManipulator );
        this.groupNodeManipulator       = this.userMasterManipulator.getGroupNodeManipulator();
        this.userNodeManipulator        = this.userMasterManipulator.getUserNodeManipulator();
        this.domainNodeManipulator      = this.userMasterManipulator.getDomainNodeManipulator();
        this.credentialManipulator      = this.userMasterManipulator.getCredentialManipulator();
        this.authorizationManipulator   = this.userMasterManipulator.getAuthorizationManipulator();
        this.privilegeManipulator               = this.userMasterManipulator.getPrivilegeManipulator();
        this.roleManipulator                      = this.userMasterManipulator.getRoleManipulator();

        this.folderManipulators = new ArrayList<>(List.of(this.domainNodeManipulator, this.groupNodeManipulator));
        this.fileManipulators   = new ArrayList<>(List.of(this.userNodeManipulator));

        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );

        this.acNodeAllotment = new GenericACNodeAllotment( this );
    }

    public UniformAccountManager( Processum superiorProcess, KOIMasterManipulator masterManipulator, AccountManager parent, String name ) {
        this( superiorProcess, masterManipulator, parent, name, CascadeInstrument.EmptySuperiorPathScope );
    }

    public UniformAccountManager( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, AccountManager.class.getSimpleName() );
    }

    public UniformAccountManager( KOIMappingDriver driver, AccountManager parent, String name ){
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name );
    }

    public UniformAccountManager( KOIMappingDriver driver ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator() );
    }

    @Override
    public ACNodeAllotment getAllotment() {
        return this.acNodeAllotment;
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public String getPath( GUID guid ) {
        return this.getNS( guid, this.kernelObjectConfig.getPathNameSeparator() );
    }

    @Override
    public String getFullName( GUID guid ) {
        return this.getNS( guid, this.kernelObjectConfig.getFullNameSeparator() );
    }

    @Override
    public ElementNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath(path);
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }

        return null;
    }

    protected ElementNode affirmTreeNodeByPath(String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        ElementNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        ElementNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    Account account = (Account) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    account.setName( parts[i] );
                    GUID guid = this.put( account );
                    return account;
                }
                else {
                    ElementNode element = (ElementNode) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    element.setName( parts[i] );
                    GUID guid = this.put( element );
                    if ( i != 0 ){
                        this.treeMasterManipulator.getTrieTreeManipulator().addChild( guid, parentGuid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = element;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }
    @Override
    public Account affirmAccount(String path) {
        return (Account) this.affirmTreeNodeByPath( path, GenericAccount.class, GenericDomain.class );
    }

    @Override
    public Group affirmGroup(String path) {
        return (Group) this.affirmTreeNodeByPath( path, GenericGroup.class, GenericDomain.class);
    }

    @Override
    public Domain affirmDomain(String path) {
        return (Domain) this.affirmTreeNodeByPath( path, null, GenericDomain.class );
    }

    @Override
    public void insertCredential(Credential credential) {
        this.credentialManipulator.insert( credential );

    }

    @Override
    public void insertRole(Role role) {
        this.roleManipulator.insert( role );
    }

    @Override
    public void addChildren( GUID parentGuid, GUID childrenGuid ) {
        this.treeMasterManipulator.getTrieTreeManipulator().addChild( childrenGuid, parentGuid );
    }

    @Override
    public boolean containsChild(GUID parentGuid, String childName) {
        for( GUIDNameManipulator manipulator : this.fileManipulators ) {
            if( this.containsChild( manipulator, parentGuid, childName ) ) {
                return true;
            }
        }

        for( GUIDNameManipulator manipulator : this.folderManipulators ) {
            if( this.containsChild( manipulator, parentGuid, childName ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<GUID> queryAccountGuidByName(String userName) {
        return  this.userNodeManipulator.getGuidsByName( userName );

    }

    @Override
    public boolean queryAccountByGuid(GUID userGuid, String kernelCredential) {
        Account account = this.userNodeManipulator.queryUser( userGuid );
        return account.getKernelCredential().equals(kernelCredential);

    }

    @Override
    public void insertPrivilege(GenericPrivilege privilege) {
        this.privilegeManipulator.insert(privilege);
    }

    @Override
    public void removePrivilege(GUID privilegeGuid) {
        this.privilegeManipulator.remove( privilegeGuid );
    }

    @Override
    public Object queryPrivilege(GUID72 guid72) {
        return null;
    }

    @Override
    public List<GenericPrivilege> queryPrivilegeByName(String name) {
        return null;
    }

    @Override
    public List<GenericPrivilege> queryAllPrivileges() {
        List<GenericPrivilege> privileges = new ArrayList<>();
        for( GenericPrivilege privilege : this.privilegeManipulator.queryAllPrivileges() ) {
            privileges.add( privilege );
        }
        return privileges;
    }

    @Override
    public void updateRole(GenericRole role) {

        this.roleManipulator.updateRole(role);
    }

    @Override
    public GUID queryUserCredentialByGuid(GUID userGuid) {
        return this.credentialManipulator.queryCredential(userGuid).getGuid();
    }

    @Override
    public boolean hasPermission(GUID userGuid, String requiredPrivilegeCode) {
        List<GenericAuthorization> authorizations = this.authorizationManipulator.queryAuthorizationByUserGuid(userGuid);
        for( GenericAuthorization authorization : authorizations ) {
            if( authorization.getPrivilegeToken().contains( requiredPrivilegeCode ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void insertAuthorization(GenericAuthorization authorization) {
        this.authorizationManipulator.insert( authorization );
    }

    @Override
    public void removeAuthorizationByGuid(GUID userGuid) {
        this.authorizationManipulator.remove( userGuid );
    }

    @Override
    public void removeAuthorizationByUserGuid(GUID userGuid) {
        this.authorizationManipulator.removeAuthorizationByUserGuid( userGuid );
    }

    @Override
    public List<GenericAccount> queryAllAccount() {
        return this.userNodeManipulator.queryAllAccount();
    }

    @Override
    public List<GenericDomain> queryAllDomain() {
        return this.domainNodeManipulator.queryAllDomain();
    }

    @Override
    public Group queryGroupByGroupGuid(GUID groupGuid) {
        return this.groupNodeManipulator.queryGroup(groupGuid);
    }

    @Override
    public String queryDomainNameByGuid(GUID domainGuid) {
        return this.domainNodeManipulator.queryDomainNameByGuid(domainGuid);
    }

    @Override
    public List<GenericAuthorization> queryAllAuthorization() {
        return this.authorizationManipulator.queryAllAuthorization();
    }

    @Override
    public List<GenericRole> queryAllRoles() {
        return  this.roleManipulator.queryAllRoles();
    }

    @Override
    public Account queryAccountByName(String userName) {
        return this.userNodeManipulator.queryAccountByName( userName );
    }

    @Override
    public void updateAccount(Account account) {
        this.userNodeManipulator.update( account );
    }

    @Override
    public Account queryAccountByUserGuid(GUID userGuid) {
        return this.userNodeManipulator.queryAccountByUserGuid( userGuid );
    }

    @Override
    public Privilege queryPrivilegeByGuid(GUID guid) {
        return this.privilegeManipulator.queryPrivilege( guid );
    }

    @Override
    public void updatePrivilege(Privilege privilege) {
        this.privilegeManipulator.update(privilege);
    }

    @Override
    public void updateAuthorization(GUID guid72) {
        this.authorizationManipulator.update( guid72 );
    }

    @Override
    public void removeRole(int id) {
        //this.roleManipulator.removeRole( id );
    }

    @Override
    public List<GenericAuthorization> queryAuthorizationByUserGuid(GUID userGuid) {
        return this.authorizationManipulator.queryAuthorizationByUserGuid( userGuid );
    }

    @Override
    public Domain queryDomainByGuid(GUID domainGuid) {
        return this.domainNodeManipulator.queryDomain( domainGuid );
    }

    @Override
    public void updateDomain(Domain domain) {
        this.domainNodeManipulator.update( domain );
    }

    @Override
    public void updateGroup(Group group) {
        this.groupNodeManipulator.update( group );
    }

    protected boolean containsChild( GUIDNameManipulator manipulator, GUID parentGuid, String childName ) {
        List<GUID > guids = manipulator.getGuidsByName( childName );
        for( GUID guid : guids ) {
            List<GUID > ps = this.imperialTree.fetchParentGuids( guid );
            if( ps.contains( parentGuid ) ){
                return true;
            }
        }
        return false;
    }

    protected String getNS(GUID guid, String szSeparator ){
        String path = this.imperialTree.getCachePath(guid);
        if ( path != null ) {
            return path;
        }

        ImperialTreeNode node = this.imperialTree.getNode(guid);
        String assemblePath = this.getNodeName(node);
        while ( !node.getParentGUIDs().isEmpty() && this.allNonNull( node.getParentGUIDs() ) ){
            List<GUID> parentGuids = node.getParentGUIDs();
            for( int i = 0; i < parentGuids.size(); ++i ){
                if ( parentGuids.get(i) != null ){
                    node = this.imperialTree.getNode( parentGuids.get(i) );
                    break;
                }
            }
            String nodeName = this.getNodeName(node);
            assemblePath = nodeName + szSeparator + assemblePath;
        }
        this.imperialTree.insertCachePath( guid, assemblePath );
        return assemblePath;
    }

    private String getNodeName(ImperialTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        TreeNodeOperator operator = this.operatorFactory.getOperator( newInstance.getMetaType() );
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }
}
