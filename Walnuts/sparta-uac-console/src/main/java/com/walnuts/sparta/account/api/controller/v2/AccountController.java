package com.walnuts.sparta.account.api.controller.v2;


import com.alibaba.fastjson.JSON;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.entity.GenericCredential;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.entity.GenericGroup;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Group;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnuts.sparta.account.api.response.BasicResultResponse;
import com.walnuts.sparta.account.domian.vo.AccountLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.image.RasterFormatException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping( "/api/v2/account" )
@CrossOrigin
public class AccountController {
    @Resource
    private AccountManager  primaryAccount;

    @PutMapping("/create/domain")
    public BasicResultResponse<String> createDomain( @RequestParam("doMainName") String doMainName ){
        GenericDomain domain = new GenericDomain();
        domain.setName( doMainName );
        this.primaryAccount.put(domain);
        return BasicResultResponse.success();
    }
    @DeleteMapping("remove/domain")
    public BasicResultResponse<String> removeDomain( @RequestParam("domainGuid") String domainGuid ){

        List<TreeNode> children = this.primaryAccount.getChildren( GUIDs.GUID72(domainGuid));
        for (TreeNode treeNode : children)
            this.primaryAccount.remove(treeNode.getGuid());
        this.primaryAccount.remove( GUIDs.GUID72(domainGuid) );
        return BasicResultResponse.success();
    }
    @PutMapping("/create/group")
    public BasicResultResponse<String> createGroup( @RequestParam("parentGuid") String parentGuid, @RequestParam("groupName") String groupName ){
        GenericGroup genericGroup = new GenericGroup();
        genericGroup.setName(groupName);
        this.primaryAccount.put( genericGroup );
        this.primaryAccount.addChildren(GUIDs.GUID72(parentGuid), genericGroup.getGuid() );
        return BasicResultResponse.success();
    }
    @DeleteMapping("remove/group")
    public BasicResultResponse<String> removeGroup( @RequestParam("groupGuid") String groupGuid ){
        List<TreeNode> children = this.primaryAccount.getChildren(GUIDs.GUID72(groupGuid));
        System.out.println(children.isEmpty());
        System.out.println(groupGuid);
        if (children.isEmpty())
        { this.primaryAccount.remove(GUIDs.GUID72(groupGuid));
            return BasicResultResponse.success("删除成功");}
        return BasicResultResponse.error("Group is not empty");
    }
    @GetMapping("/query/path")
    public String queryNodeByPath( @RequestParam("path") String path ){
        GUID guid = this.primaryAccount.queryGUIDByPath(path);
        return BasicResultResponse.success(this.primaryAccount.get(guid)).toJSONString();
    }
    @PutMapping("create/account")
    public BasicResultResponse<String> createAccount(
            @RequestParam("userName") String userName,
            @RequestParam("nickName") String nickName,
            @RequestParam("kernelCredential") String kernelCredential,
            @RequestParam("kernelGroupType") String kernelGroupType,
            @RequestParam("parentGuid") String parentGuid)
    {
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
            account.setRole("CLIENT");
            account.setKernelCredential(kernelCredential);
            account.setKernelGroupType(kernelGroupType);
            account.setCreateTime(now);
            account.setUpdateTime(now);
            this.primaryAccount.put(account);
            this.primaryAccount.addChildren(GUIDs.GUID72(parentGuid), account.getGuid());
            return BasicResultResponse.success();
        }
        return BasicResultResponse.error("Account already exists");
    }
    @PutMapping("/login")
    public BasicResultResponse<String> login(
            @RequestParam("userName") String userName,
            @RequestParam("kernelCredential") String kernelCredential)
    {
        List<GUID> userGuidList =this.primaryAccount.queryAccountGuidByName(userName);
        if (userGuidList== null)
            return BasicResultResponse.error("Account not found");

        GUID userGuid = userGuidList.get(0); // 用户名是唯一的
        boolean isLogin =this.primaryAccount.queryAccountByGuid(userGuid,kernelCredential);
        if (!isLogin) {
            return BasicResultResponse.error("Account or kernelCredential error ");
        }
        return BasicResultResponse.success("登录成功");
    }
    @GetMapping("/query/allAccount")
    public BasicResultResponse<String> queryAllAccount()
    {
        List<GenericAccount> accounts = this.primaryAccount.queryAllAccount();
        return BasicResultResponse.success(accounts.toString());
    }
    @GetMapping("/query/domain")
    public BasicResultResponse<String> queryDomain()
    {
        List<GenericDomain> domains = this.primaryAccount.queryAllDomain();
        return BasicResultResponse.success(domains.toString());
    }
    @GetMapping("/query/account")
    public BasicResultResponse<String> queryAccount(
            @RequestParam("userName") String userName)
    {
        List<GUID> userGuidList =this.primaryAccount.queryAccountGuidByName(userName);
        System.out.println(userGuidList);
        if (userGuidList.isEmpty())
        {
            return BasicResultResponse.error("Account not found");}
        GUID userGuid = userGuidList.get(0); // 假设用户名是唯一的
        GenericAccount account = (GenericAccount) this.primaryAccount.get(userGuid);
        AccountLoginVo accountLoginVo = new AccountLoginVo();
        BeanUtils.copyProperties(account,accountLoginVo);
        return BasicResultResponse.success(accountLoginVo.toJSONString());
    }
    @GetMapping("/query/domain/groups")
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
            return BasicResultResponse.success(JSON.toJSONString(groups));
        } catch (Exception e) {
            return BasicResultResponse.error("Failed to query groups: " + e.getMessage());
        }
    }
    @GetMapping("/query/group")

    public BasicResultResponse<String> queryDomainGroup(
            @RequestParam("domainGuid") String domainGuid
    ){
        List<TreeNode> children = this.primaryAccount.getChildren(GUIDs.GUID72(domainGuid));
        List<Group> groups = new ArrayList<>();
        for (TreeNode child : children)
            if (child instanceof Group)
                groups.add( this.primaryAccount.queryGroupByGroupGuid(child.getGuid()));
        return BasicResultResponse.success(groups.toString());
    }
    @DeleteMapping("/remove/account")
    public BasicResultResponse<String> removeAccount(
            @RequestParam("userName") String userName)
    {
        List<GUID> userGuidList =this.primaryAccount.queryAccountGuidByName(userName);
        if (userGuidList.isEmpty())
            return BasicResultResponse.error("Account not found");
        GUID userGuid = userGuidList.get(0);
        this.primaryAccount.remove(userGuid);
        this.primaryAccount.removeAuthorizationByUserGuid(userGuid);
        return BasicResultResponse.success("删除成功");
    }
    @PutMapping("/create/privilege")
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
    @DeleteMapping("/remove/privilege")
    public BasicResultResponse<String> removePrivilege(
            @RequestParam("privilegeGuid") String privilegeGuid)
    {
        this.primaryAccount.removePrivilege(GUIDs.GUID72(privilegeGuid));
        return BasicResultResponse.success();
    }
    @GetMapping("/List/privilege")
    public BasicResultResponse<String> listPrivilege(
    )
    {

        List<GenericPrivilege> privileges = this.primaryAccount.queryAllPrivileges();
        return BasicResultResponse.success(JSON.toJSONString(privileges));
    }
    @PutMapping("/create/role")
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
    @PutMapping("/create/Authorization")
    public BasicResultResponse<String> createAuthorization(
            @RequestParam("userName") String userName,
            @RequestParam("CredentialGuid") String CredentialGuid,
            @RequestParam("privilegeToken") String privilegeToken,
            @RequestParam("privilegeGuid") String privilegeGuids)
    {
        List<GUID> userGuidList=this.primaryAccount.queryAccountGuidByName(userName);
        GUID userGuid= userGuidList.get(0);
        System.out.println(userGuid);
        System.out.println(userGuid);
        if (userGuidList.isEmpty())
        {
            return BasicResultResponse.error("Account not found");
        }

        GenericAuthorization authorization = new GenericAuthorization(
                userGuid,
                userName,
                GUIDs.GUID72(CredentialGuid),
                privilegeToken,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        authorization.setGuid(  this.primaryAccount.getGuidAllocator().nextGUID());
        authorization.setPrivilegeGuid(GUIDs.GUID72(privilegeGuids));
        this.primaryAccount.insertAuthorization(authorization);

        return BasicResultResponse.success();
    }

    @DeleteMapping("/delete/Authorization")
    public BasicResultResponse<String> deleteAuthorization(
            @RequestParam ("userGuid") String userGuid)
    {
        this.primaryAccount.removeAuthorizationByUserGuid(GUIDs.GUID72(userGuid));
        return BasicResultResponse.success();
    }


}
