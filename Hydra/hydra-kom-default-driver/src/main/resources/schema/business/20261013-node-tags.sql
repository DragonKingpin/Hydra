CREATE TABLE `hydra_business_node_tags` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `node_guid` varchar(82) NOT NULL COMMENT '业务节点GUID',
  `tag` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_node_guid_tag` (`node_guid`, `tag`) USING BTREE,
  KEY `idx_tag_node_guid` (`tag`, `node_guid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='业务节点标签';

ALTER TABLE `hydra_business_nodes`
  MODIFY COLUMN `extra_information` text CHARACTER SET utf8mb4 COMMENT '扩展信息';
