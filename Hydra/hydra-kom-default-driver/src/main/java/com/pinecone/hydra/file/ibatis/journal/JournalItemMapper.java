package com.pinecone.hydra.file.ibatis.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItemStatus;
import com.pinecone.hydra.storage.file.journal.JournalItemType;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
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
public interface JournalItemMapper extends JournalItemManipulator {
    @Insert( "INSERT INTO `hydra_uofs_journal_item` ( `guid`, `journal_guid`, `item_type`, `item_status`, `target_guid`, `file_guid`, `chunk_guid`, `location_guid`, `volume_guid`, `object_key`, `volume_offset`, `length_bytes`, `ordinal_no`, `old_payload`, `new_payload`, `ext_config` ) VALUES ( #{guid}, #{journalGuid}, #{itemType}, #{itemStatus}, #{targetGuid}, #{fileGuid}, #{chunkGuid}, #{locationGuid}, #{volumeGuid}, #{objectKey}, #{volumeOffset}, #{lengthBytes}, #{ordinalNo}, #{oldPayload}, #{newPayload}, #{extConfig} )" )
    void insert( JournalItem item );

    @Update( "UPDATE `hydra_uofs_journal_item` SET `item_status` = #{itemStatus} WHERE `guid` = #{guid}" )
    void updateStatus( @Param( "guid" ) GUID guid, @Param( "itemStatus" ) JournalItemStatus itemStatus );

    @Select( "SELECT `id`, `guid`, `journal_guid` AS journalGuid, `item_type` AS itemType, `item_status` AS itemStatus, `target_guid` AS targetGuid, `file_guid` AS fileGuid, `chunk_guid` AS chunkGuid, `location_guid` AS locationGuid, `volume_guid` AS volumeGuid, `object_key` AS objectKey, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `ordinal_no` AS ordinalNo, `old_payload` AS oldPayload, `new_payload` AS newPayload, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal_item` WHERE `guid` = #{guid}" )
    GenericJournalItem get( GUID guid );

    @Select( "SELECT `id`, `guid`, `journal_guid` AS journalGuid, `item_type` AS itemType, `item_status` AS itemStatus, `target_guid` AS targetGuid, `file_guid` AS fileGuid, `chunk_guid` AS chunkGuid, `location_guid` AS locationGuid, `volume_guid` AS volumeGuid, `object_key` AS objectKey, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `ordinal_no` AS ordinalNo, `old_payload` AS oldPayload, `new_payload` AS newPayload, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal_item` WHERE `journal_guid` = #{journalGuid} ORDER BY `ordinal_no` ASC" )
    @ResultType( GenericJournalItem.class )
    List<GenericJournalItem> listByJournalGuid( GUID journalGuid );

    @Select( "SELECT `id`, `guid`, `journal_guid` AS journalGuid, `item_type` AS itemType, `item_status` AS itemStatus, `target_guid` AS targetGuid, `file_guid` AS fileGuid, `chunk_guid` AS chunkGuid, `location_guid` AS locationGuid, `volume_guid` AS volumeGuid, `object_key` AS objectKey, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `ordinal_no` AS ordinalNo, `old_payload` AS oldPayload, `new_payload` AS newPayload, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal_item` WHERE `journal_guid` = #{journalGuid} AND `item_status` = #{itemStatus} ORDER BY `ordinal_no` ASC" )
    @ResultType( GenericJournalItem.class )
    List<GenericJournalItem> listByJournalGuidAndStatus( @Param( "journalGuid" ) GUID journalGuid, @Param( "itemStatus" ) JournalItemStatus itemStatus );

    @Select( "SELECT `id`, `guid`, `journal_guid` AS journalGuid, `item_type` AS itemType, `item_status` AS itemStatus, `target_guid` AS targetGuid, `file_guid` AS fileGuid, `chunk_guid` AS chunkGuid, `location_guid` AS locationGuid, `volume_guid` AS volumeGuid, `object_key` AS objectKey, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `ordinal_no` AS ordinalNo, `old_payload` AS oldPayload, `new_payload` AS newPayload, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_journal_item` WHERE `journal_guid` = #{journalGuid} AND `item_type` = #{itemType} ORDER BY `ordinal_no` ASC" )
    @ResultType( GenericJournalItem.class )
    List<GenericJournalItem> listByJournalGuidAndType( @Param( "journalGuid" ) GUID journalGuid, @Param( "itemType" ) JournalItemType itemType );
}
