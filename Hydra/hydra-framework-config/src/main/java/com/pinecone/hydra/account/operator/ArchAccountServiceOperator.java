package com.pinecone.hydra.account.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.entity.ElementNode;
import com.pinecone.hydra.account.source.UserMasterManipulator;

import java.time.LocalDateTime;

public abstract class ArchAccountServiceOperator implements AccountServiceOperator {
    protected AccountManager                accountManager;

    protected AccountServiceOperatorFactory factory;

    protected ImperialTree                  imperialTree;

    protected UserMasterManipulator         userMasterManipulator;

    public ArchAccountServiceOperator(UserMasterManipulator masterManipulator, AccountManager accountManager){
        this.accountManager = accountManager;
        this.userMasterManipulator = masterManipulator;
        this.imperialTree = this.accountManager.getMasterTrieTree();
    }

    protected ImperialTreeNode affirmPreinsertionInitialize(TreeNode node ){
        ElementNode entityNode = (ElementNode) node;
        GUID guid = entityNode.getGuid();
        LocalDateTime now = LocalDateTime.now();
        if( guid == null ) {
            guid = this.accountManager.getGuidAllocator().nextGUID();
            entityNode.setGuid( guid );
        }
        if( entityNode.getCreateTime() == null ) {
            entityNode.setCreateTime( now );
        }
        entityNode.setUpdateTime( now );

        ImperialTreeNode imperialTreeNode = new GUIDImperialTrieNode();
        imperialTreeNode.setGuid( guid );
        imperialTreeNode.setType( UOIUtils.createLocalJavaClass( node.getClass().getName() ) );

        return imperialTreeNode;
    }

    public AccountServiceOperatorFactory getUserOperatorFactory(){
        return this.factory;
    }
    protected String getUserMetaType( TreeNode treeNode ){
        return treeNode.className().replace("Generic","");
    }
}
