package com.pinecone.hydra.file.ibatis.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournal;
import com.pinecone.hydra.storage.file.journal.Journal;
import com.pinecone.hydra.storage.file.journal.JournalStatus;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface JournalMapper extends JournalManipulator {
    @Insert( "INSERT INTO `hydra_uofs_journal` ( `guid`, `journal_type`, `journal_status`, `file_guid`, `path`, `operator_guid`, `begin_time`, `commit_time`, `error_message`, `ext_config` ) VALUES ( #{guid}, #{journalType}, #{journalStatus}, #{fileGuid}, #{path}, #{operatorGuid}, #{beginTime}, #{commitTime}, #{errorMessage}, #{extConfig} )" )
    void insert( Journal journal );

    @Update( "UPDATE `hydra_uofs_journal` SET `journal_status` = #{journalStatus}, `error_message` = #{errorMessage} WHERE `guid` = #{guid}" )
    void updateStatus( @Param( "guid" ) GUID guid, @Param( "journalStatus" ) JournalStatus journalStatus, @Param( "errorMessage" ) String errorMessage );

    @Update( "UPDATE `hydra_uofs_journal` SET `journal_status` = 'COMMITTED', `commit_time` = CURRENT_TIMESTAMP WHERE `guid` = #{guid}" )
    void commit( GUID guid );

    @Select( "SELECT `id`, `guid`, `journal_type` AS journalType, `journal_status` AS journalStatus, `file_guid` AS fileGuid, `path`, `operator_guid` AS operatorGuid, `begin_time` AS beginTime, `commit_time` AS commitTime, `error_message` AS errorMessage, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal` WHERE `guid` = #{guid}" )
    GenericJournal get( GUID guid );

    @Select( "SELECT `id`, `guid`, `journal_type` AS journalType, `journal_status` AS journalStatus, `file_guid` AS fileGuid, `path`, `operator_guid` AS operatorGuid, `begin_time` AS beginTime, `commit_time` AS commitTime, `error_message` AS errorMessage, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal` WHERE `journal_status` = #{journalStatus} ORDER BY `id` ASC" )
    @ResultType( GenericJournal.class )
    List<GenericJournal> listByStatus( JournalStatus journalStatus );
}
