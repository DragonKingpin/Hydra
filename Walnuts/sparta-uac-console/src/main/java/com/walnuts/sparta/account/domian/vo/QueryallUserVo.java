package com.walnuts.sparta.account.domian.vo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.ArchElementNode;

import java.time.LocalDateTime;

public class QueryallUserVo extends ArchElementNode implements Account{
        protected long enumId;

        protected String name;

        protected GUID guid;

        protected String nickName;

        protected String kernelGroupType;
        protected String role;

        protected LocalDateTime createTime;

        protected LocalDateTime updateTime;

        public QueryallUserVo(
                String name,
                GUID guid,
                String nickName,
                String kernelGroupType,
                String role,
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {
            this.name = name;
            this.guid = guid;
            this.nickName = nickName;
            this.kernelGroupType = kernelGroupType;
            this.role = role;
            this.createTime = createTime;
            this.updateTime = updateTime;
        }


        public QueryallUserVo(){
            super();
        }

        public QueryallUserVo(AccountManager accountManager){
            super(accountManager);
        }


        @Override
        public String getRole() {
            return this.role;
        }

        @Override
        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String getNickName() {
            return this.nickName;
        }

        @Override
        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

    @Override
    public String getKernelCredential() {
        return null;
    }

    @Override
    public void setKernelCredential(String kernelCredential) {

    }

    @Override
    public GUID getCredentialGuid() {
        return null;
    }

    @Override
    public void setCredentialGuid(GUID credentialGuid) {

    }

    @Override
        public String getKernelGroupType() {
            return this.kernelGroupType;
        }

        @Override
        public void setKernelGroupType(String kernelGroupType) {
            this.kernelGroupType = kernelGroupType;
        }

        @Override
        public LocalDateTime getCreateTime() {
            return this.createTime;
        }

        @Override
        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        @Override
        public LocalDateTime getUpdateTime() {
            return this.updateTime;
        }

        @Override
        public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }
        @Override
        public String toJSONString() {
            return BeanJSONEncoder.BasicEncoder.encode( this );
        }

        @Override
        public String toString() {
            return this.toJSONString();
        }

}
