package com.walnuts.sparta.account.api.controller.v2;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.entity.ACNodeAllotment;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.Domain;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.entity.GenericCredential;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.entity.GenericGroup;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Group;
import com.pinecone.hydra.account.entity.Privilege;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnuts.sparta.account.api.response.BasicResultResponse;
import com.walnuts.sparta.account.domian.vo.AccountLoginVO;
import com.walnuts.sparta.account.domian.vo.UserLoginVO;
import com.walnuts.sparta.account.interceptor.RequiresAuthentication;
import com.walnuts.sparta.account.properties.JwtProperties;
import com.walnuts.sparta.account.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping( "/api/v2/account" )
@CrossOrigin
public class AccountController {
    @Resource
    private AccountManager  primaryAccount;

    private JwtProperties   jwtProperties;
    public  AccountController(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PutMapping("/create/domain")
    @RequiresAuthentication
    public BasicResultResponse<String> createDomain( @RequestParam("doMainName") String doMainName ){
        ACNodeAllotment allotment = this.primaryAccount.getAllotment();
        Domain domain = allotment.newDomain();
        domain.setName( doMainName );
        this.primaryAccount.put(domain);
        return BasicResultResponse.success();
    }

    @PutMapping("/update/domain")
    @RequiresAuthentication
    public BasicResultResponse<Boolean> updateDomain(
            @RequestParam("guid") String guid,
            @RequestParam("name") String name) {
        // 查询域是否存在
        GenericDomain domain = new GenericDomain();
        domain.setGuid(GUIDs.GUID72(guid));
        domain.setName(name); // 更新域名称
        this.primaryAccount.updateDomain(domain); // 保存更新
        return BasicResultResponse.success(true);
    }
    @DeleteMapping("remove/domain")
    @RequiresAuthentication
    public BasicResultResponse<String> removeDomain( @RequestParam("domainGuid") String domainGuid ){

        List<TreeNode> children = this.primaryAccount.getChildren( GUIDs.GUID72(domainGuid));
        for (TreeNode treeNode : children) {
            this.primaryAccount.remove(treeNode.getGuid());
        }
        this.primaryAccount.remove( GUIDs.GUID72(domainGuid) );
        return BasicResultResponse.success();
    }

    @PutMapping("/create/group")
    @RequiresAuthentication
    public BasicResultResponse<String> createGroup( @RequestParam("parentGuid") String parentGuid, @RequestParam("groupName") String groupName ){
        GenericGroup genericGroup = new GenericGroup();
        genericGroup.setName(groupName);
        this.primaryAccount.put( genericGroup );
        this.primaryAccount.addChildren(GUIDs.GUID72(parentGuid), genericGroup.getGuid() );
        return BasicResultResponse.success();
    }
    @DeleteMapping("/remove/group")
    @RequiresAuthentication
    public BasicResultResponse<String> removeGroup( @RequestParam("groupGuid") String groupGuid ){
        List<TreeNode> children = this.primaryAccount.getChildren(GUIDs.GUID72(groupGuid));
        System.out.println(children.isEmpty());
        System.out.println(groupGuid);
        if (children.isEmpty()) {
            this.primaryAccount.remove(GUIDs.GUID72(groupGuid));
            return BasicResultResponse.success("删除成功");
        }
        return BasicResultResponse.error("Group is not empty");
    }

    @PutMapping("/update/group")
    @RequiresAuthentication
    public BasicResultResponse<Boolean> updateGroup(
            @RequestParam("groupGuid") String groupGuid,
            @RequestParam("groupName") String groupName) {
        // 查询组是否存在
        Group group = this.primaryAccount.queryGroupByGroupGuid(GUIDs.GUID72(groupGuid));
        if (group != null) {
            group.setName(groupName); // 更新组名称
            this.primaryAccount.updateGroup(group); // 保存更新
            return BasicResultResponse.success(true);
        } else {
            return BasicResultResponse.error("Group not found");
        }
    }

    @GetMapping("/query/users/byGroup")
    @RequiresAuthentication
    public String queryUsersByGroup(@RequestParam("groupGuid") String groupGuid) {
        List<GenericAccount> accounts = new ArrayList<>();
        List<GUID> guids = this.primaryAccount.fetchChildrenGuids(GUIDs.GUID72(groupGuid));
        for (GUID guid : guids)
        {
            accounts.add((GenericAccount) this.primaryAccount.queryAccountByUserGuid(guid));

        }
        return BasicResultResponse.success(accounts).toJSONString();
    }

    @GetMapping("/query/path")
    @RequiresAuthentication
    public String queryNodeByPath( @RequestParam("path") String path ){
        GUID guid = this.primaryAccount.queryGUIDByPath(path);
        return BasicResultResponse.success(this.primaryAccount.get(guid)).toJSONString();
    }

    @PutMapping("/create/account")
    @RequiresAuthentication
    public String createAccount(
            @RequestParam("userName") String userName,
            @RequestParam("nickName") String nickName,
            @RequestParam("kernelCredential") String kernelCredential,
            @RequestParam("kernelGroupType") String kernelGroupType,
            @RequestParam("role") String role,
            @RequestParam("parentGuid") String parentGuid) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
        System.out.println("Account created at: " + formattedTime);
        GenericAccount account = new GenericAccount(this.primaryAccount);
        account.setName(userName);
        System.out.println(account.getName());
        if (this.primaryAccount.queryAccountGuidByName(account.getName()).isEmpty()) {
            account.setNickName(nickName);
            GenericCredential credential = new GenericCredential(
                    this.primaryAccount.getGuidAllocator().nextGUID(),
                    userName,
                    kernelCredential,
                    now,
                    now,
                    "TextPassword"
            );
            this.primaryAccount.insertCredential(credential);
            account.setCredentialGuid(credential.getGuid());
            account.setKernelCredential(kernelCredential);
            account.setKernelGroupType(kernelGroupType);
            account.setCreateTime(now);
            account.setUpdateTime(now);
            account.setRole(role);
            this.primaryAccount.put(account);
            List<GenericRole> list = this.primaryAccount.queryAllRoles();
            for (GenericRole roles : list) {
                if (roles.getName().equals(role)) {
                    String[] privilegeGuids = roles.getPrivilegeGuids().split(",");
                    for (String privilegeGuid : privilegeGuids) {
                        GenericAuthorization authorization = new GenericAuthorization(
                                account.getGuid(),
                                account.getName(),
                                credential.getGuid(),
                                kernelGroupType,
                                now,
                                now
                        );
                        authorization.setPrivilegeGuid(GUIDs.GUID72(privilegeGuid.trim())); // 去除可能的空格
                        authorization.setGuid(this.primaryAccount.getGuidAllocator().nextGUID());
                        this.primaryAccount.insertAuthorization(authorization);
                    }
                    break;
                }
            }

            this.primaryAccount.addChildren(GUIDs.GUID72(parentGuid), account.getGuid());
            return BasicResultResponse.success(account).toJSONString();
        }
        return BasicResultResponse.error("Account already exists").toJSONString();
    }

    @PutMapping("/update/account")
    @RequiresAuthentication
    public BasicResultResponse<String> updateAccount(
            @RequestParam("userGuid") String userGuid,
            @RequestParam("nickName") String nickName,
            @RequestParam("kernelCredential") String kernelCredential,
            @RequestParam("kernelGroupType") String kernelGroupType,
            @RequestParam("role") String role,
            @RequestParam("newUsername") String newUsername
    ) {
        LocalDateTime now = LocalDateTime.now();
        Account account = this.primaryAccount.queryAccountByUserGuid(GUIDs.GUID72(userGuid));
        if (account != null) {
            account.setNickName(nickName);
            account.setKernelCredential(kernelCredential);
            account.setKernelGroupType(kernelGroupType);
            account.setCreateTime(now);
            account.setUpdateTime(now);
            account.setRole(role);
            account.setName(newUsername); // 允许修改用户名
            account.setGuid(GUIDs.GUID72(userGuid));
            System.out.println(account);
            this.primaryAccount.updateAccount(account);
        }
        return BasicResultResponse.success();
    }

    @DeleteMapping("/remove/account")
    @RequiresAuthentication
    public BasicResultResponse<Boolean> removeAccount( @RequestParam("userGuid") String userGuid ) {
        Account account=this.primaryAccount.queryAccountByUserGuid(GUIDs.GUID72(userGuid));
        List<GenericAuthorization> authorizations = this.primaryAccount.queryAuthorizationByUserGuid(account.getGuid());
        for (GenericAuthorization authorization : authorizations) {
            this.primaryAccount.remove(authorization.getGuid());
        }
        this.primaryAccount.remove(account.getGuid());
        return BasicResultResponse.success(true);
    }

    @PutMapping("/login")
    public String login( @RequestParam("userName") String userName, @RequestParam("kernelCredential") String kernelCredential ) {
        // 查询用户 GUID
        List<GUID> userGuidList = this.primaryAccount.queryAccountGuidByName(userName);
        if (userGuidList == null || userGuidList.isEmpty()) {
            return BasicResultResponse.error("Account not found").toJSONString();
        }

        GUID userGuid = userGuidList.get(0); // 用户名是唯一的
        boolean isLogin = this.primaryAccount.queryAccountByGuid(userGuid, kernelCredential);
        if (!isLogin) {
            return BasicResultResponse.error("Account or kernelCredential error").toJSONString();
        }

        // 用户登录成功，生成 JWT 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userGuid.toString()); // 假设 userGuid 是用户的唯一标识

        // 从配置文件中读取 JWT 配置
        String userSecretKey = "1212121hsodhsdhasdhsaldhsalhdlsahdlsad"; // 替换为实际的密钥
        long userTtl = 3600000; // 替换为实际的过期时间
        System.out.println(this.jwtProperties.getUserSecretKey());
        System.out.println("User Secret Key: " + userSecretKey);
        System.out.println("User TTL: " + userTtl);

        String token = JwtUtil.createJWT(userSecretKey, userTtl, claims);

        UserLoginVO userLoginVo = new UserLoginVO();
        userLoginVo.setUserid(userGuid.toString());
        userLoginVo.setUserName(userName);
        userLoginVo.setUserToken(token);

        System.out.println(userLoginVo);

        return BasicResultResponse.success(userLoginVo).toJSONString();
    }

    @GetMapping("/query/allAccount")
    @RequiresAuthentication
    public BasicResultResponse<String> queryAllAccount() {
        List<GenericAccount> accounts = this.primaryAccount.queryAllAccount();
        return BasicResultResponse.success(accounts.toString());
    }



    @PutMapping("/query/Authorization/ByUserName")
    @RequiresAuthentication
    public String queryAuthorizationByUserName( @RequestParam("userName") String userName ) {
        List<GUID> userGuidList =this.primaryAccount.queryAccountGuidByName(userName);
        if (userGuidList.isEmpty()) {
            return BasicResultResponse.error("Account not found").toJSONString();
        }
        GUID userGuid = userGuidList.get(0); // 假设用户名是唯一的
        List<GenericAuthorization> authorizations = this.primaryAccount.queryAuthorizationByUserGuid(userGuid);
        return BasicResultResponse.success(authorizations).toJSONString();
    }

    @GetMapping("/query/domain")
    @RequiresAuthentication
    public BasicResultResponse<String> queryDomain() {
        List<GenericDomain> domains = this.primaryAccount.queryAllDomain();
        return BasicResultResponse.success(domains.toString());
    }

    @GetMapping("/query/account")
    @RequiresAuthentication
    public BasicResultResponse<String> queryAccount(
            @RequestParam("userName") String userName)
    {
        List<GUID> userGuidList =this.primaryAccount.queryAccountGuidByName(userName);
        System.out.println(userGuidList);
        if (userGuidList.isEmpty()) {
            return BasicResultResponse.error("Account not found");
        }
        GUID userGuid = userGuidList.get(0); // 假设用户名是唯一的
        GenericAccount account = (GenericAccount) this.primaryAccount.get(userGuid);
        AccountLoginVO accountLoginVo = new AccountLoginVO();
        BeanUtils.copyProperties(account,accountLoginVo);
        return BasicResultResponse.success(accountLoginVo.toJSONString());
    }

    @GetMapping("/query/domain/groups")
    @RequiresAuthentication
    public BasicResultResponse<String> queryDomainGroups(
            @RequestParam("domainGuid") String domainGuid) {
        try {
            List<TreeNode> children = this.primaryAccount.getChildren(GUIDs.GUID72(domainGuid));
            List<Map<String, String>> groups = new ArrayList<>();
            for (TreeNode child : children) {
                if (child instanceof Group) {
                    Group group = this.primaryAccount.queryGroupByGroupGuid(child.getGuid());
                    Map<String, String> groupInfo = new HashMap<>();
                    groupInfo.put("domainName", this.primaryAccount.queryDomainNameByGuid(GUIDs.GUID72(domainGuid)));
                    System.out.println(this.primaryAccount.queryDomainNameByGuid(GUIDs.GUID72(domainGuid)));
                    groupInfo.put("groupName", group.getName());
                    groupInfo.put("groupGuid", group.getGuid().toString());
                    groups.add(groupInfo);
                }
            }
            return BasicResultResponse.success(JSON.stringify(groups));
        } catch (Exception e) {
            return BasicResultResponse.error("Failed to query groups: " + e.getMessage());
        }
    }

    @GetMapping("/query/group")
    @RequiresAuthentication
    public BasicResultResponse<String> queryDomainGroup(
            @RequestParam("domainGuid") String domainGuid
    ){
        List<TreeNode> children = this.primaryAccount.getChildren(GUIDs.GUID72(domainGuid));
        List<Group> groups = new ArrayList<>();
        for (TreeNode child : children) {
            if (child instanceof Group) {
                groups.add( this.primaryAccount.queryGroupByGroupGuid(child.getGuid()));
            }
        }
        return BasicResultResponse.success(groups.toString());
    }

    @PutMapping("/create/privilege")
    @RequiresAuthentication
    public BasicResultResponse<String> createPrivilege(
            @RequestParam("token") String token,
            @RequestParam("name") String name,
            @RequestParam("privilegeCode") String privilegeCode,
            @RequestParam("type") String type,
            @RequestParam(value = "parentPrivGuid", required = false) String parentPrivGuid)
    {
        System.out.println(token);
        GenericPrivilege privilege = new GenericPrivilege(
                this.primaryAccount.getGuidAllocator().nextGUID(),
                token,
                name,
                privilegeCode,
                LocalDateTime.now(),
                LocalDateTime.now(),
                type
        );
        // 检查parentPrivGuid是否为空或空字符串
        if (parentPrivGuid != null && !parentPrivGuid.isEmpty()) {
            privilege.setParentPrivGuid(GUIDs.GUID72(parentPrivGuid));
        } else {
            privilege.setParentPrivGuid(null);
        }
        System.out.println(privilege.getParentPrivGuid());
        this.primaryAccount.insertPrivilege(privilege);
        return BasicResultResponse.success();
    }
    @PutMapping("/update/privilege")
    @RequiresAuthentication
    public String updatePrivilege(
            @RequestParam("guid") String guid,
            @RequestParam("name") String name,
            @RequestParam("token") String token,
            @RequestParam("type") String type,
            @RequestParam("privilegeCode") String privilegeCode) {
        Privilege privilege = this.primaryAccount.queryPrivilegeByGuid(GUIDs.GUID72(guid));
        if (privilege != null) {
            privilege.setName(name);
            privilege.setToken(token);
            privilege.setType(type);
            privilege.setPrivilegeCode(privilegeCode);
            this.primaryAccount.updatePrivilege(privilege);
            return BasicResultResponse.success(privilege).toJSONString();
        }
        return BasicResultResponse.error("权限不存在").toJSONString();
    }
    @DeleteMapping("/remove/privilege")
    @RequiresAuthentication
    public BasicResultResponse<String> removePrivilege(
            @RequestParam("privilegeGuid") String privilegeGuid)
    {
        this.primaryAccount.removePrivilege(GUIDs.GUID72(privilegeGuid));
        return BasicResultResponse.success();
    }
    @GetMapping("/List/privilege")
    @RequiresAuthentication
    public String listPrivilege(
    )
    {

        List<GenericPrivilege> privileges = this.primaryAccount.queryAllPrivileges();
        return BasicResultResponse.success(privileges).toJSONString();
    }
    @PutMapping("/create/role")
    @RequiresAuthentication
    public BasicResultResponse<String> createRole(
            @RequestParam("roleName") String roleName,
            @RequestParam("roleType") String roleType,
            @RequestParam("privilegeGuids") String privilegeGuids)
    {
        GenericRole role = new GenericRole(
                roleName,
                privilegeGuids,
                LocalDateTime.now(),
                LocalDateTime.now(),
                roleType
        );
        this.primaryAccount.insertRole(role);
        return BasicResultResponse.success();
    }
    @PutMapping("/update/role")
    @RequiresAuthentication
    public BasicResultResponse<String> updateRole(
            @RequestParam("roleName") String roleName,
            @RequestParam("roleType") String roleType,
            @RequestParam("privilegeGuids") String privilegeGuids)
    {
        GenericRole role = new GenericRole(
                roleName,
                privilegeGuids,
                LocalDateTime.now(),
                LocalDateTime.now(),
                roleType
        );
        this.primaryAccount.updateRole(role);
        return BasicResultResponse.success();
    }
    @GetMapping("/query/all/role")
    @RequiresAuthentication
    public String queryAllRole()
    {
        List<GenericRole> roles = this.primaryAccount.queryAllRoles();
        System.out.println(roles);
        GenericRole role = roles.get(0);
        System.out.println(role.getPrivilegeGuids());
        return BasicResultResponse.success(roles).toJSONString();
    }
    @PutMapping("/create/Authorization")
    @RequiresAuthentication
    public BasicResultResponse<String> createAuthorization(
            @RequestParam("userName") String userName,
            @RequestParam("privilegeToken") String privilegeToken,
            @RequestParam("privilegeGuid") String privilegeGuids)
    {
        List<GUID> userGuidList=this.primaryAccount.queryAccountGuidByName(userName);
        GUID userGuid= userGuidList.get(0);
        Account account = this.primaryAccount.queryAccountByUserGuid(userGuid);
        GUID credentialGuid = account.getCredentialGuid();
        System.out.println(userGuid);
        if (userGuidList.isEmpty()) {
            return BasicResultResponse.error("Account not found");
        }

        GenericAuthorization authorization = new GenericAuthorization(
                userGuid,
                userName,
                credentialGuid,
                privilegeToken,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        authorization.setGuid(this.primaryAccount.getGuidAllocator().nextGUID());
        authorization.setPrivilegeGuid(GUIDs.GUID72(privilegeGuids));
        this.primaryAccount.insertAuthorization(authorization);

        return BasicResultResponse.success();
    }

    @DeleteMapping("/delete/Authorization")
    @RequiresAuthentication
    public BasicResultResponse<String> deleteAuthorization(
            @RequestParam ("authorizationGuid") String Guid)
    {
        this.primaryAccount.removeAuthorizationByGuid(GUIDs.GUID72(Guid));
        return BasicResultResponse.success();
    }


    @DeleteMapping("/remove/role")
    @RequiresAuthentication
    public BasicResultResponse<String> removeRole(
            @RequestParam("id") int id)
    {
        this.primaryAccount.removeRole(id);
        return BasicResultResponse.success();
    }
    @GetMapping("/query/Authorization")
    @RequiresAuthentication
    public String queryAuthorization(
    )
    {
        List<GenericAuthorization> authorizations = this.primaryAccount.queryAllAuthorization();
        return BasicResultResponse.success(authorizations).toJSONString();
    }
    @PutMapping("/update/authorization")
    @RequiresAuthentication
    public BasicResultResponse<String> updateAuthorization(
            @RequestParam("guid") String guid
    ) {
        try {
            // 更新授权信息的逻辑
            this.primaryAccount.updateAuthorization(GUIDs.GUID72(guid));
            return BasicResultResponse.success();
        } catch (Exception e) {
            return BasicResultResponse.error("更新授权失败: " + e.getMessage());
        }
    }
}