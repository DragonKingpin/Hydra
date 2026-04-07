# MYSQL 建表与SQL编码规范
本 Skill 对MYSQL生效，以下是核心风格规范：

## 目标
建立统一、可审计、可扩展、适合高并发 + 分库分表演进的 MySQL DDL 风格。

## 1. 标准结构模板（强制规范）
```sql
CREATE TABLE `{{table_name}}` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',

  -- 业务字段写在此处

  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8 
  ROW_FORMAT=DYNAMIC;
```

### 2. 时间字段规范（强制）
所有业务表必须包含：
```sql
create_time
update_time
```
---
### 3. 存储引擎规范
必须：
```
ENGINE=InnoDB
```

### 4. 字符集规范
默认都使用 utf8，内容类大text字段使用utf8mb4

### 5. 所有字段必须带 COMMENT
禁止无注释字段

### 6. SQL 语法
6.1 禁用 SELECT *
6.2 所有表别名不允许使用单字母
6.3 表别名可以使用别名缩写，不要使用全名，此外对于非联合查询无需使用别名和前缀表名。
6.4 关键字大写。
6.5 冲突Key使用字符'\`' 如 '`key`' 标记。
e.g.
```sql
SELECT `key` FROM table WHERE id > 1234;
```

```sql
SELECT t1.`key`, t2.`k2` FROM table as t1 LEFT JOIN table2 as t2 ON t1.x = t2.x WHERE t1.id > 1234;
```


### 7. Ibatis 
7.1 Param 不要使用匈牙利命名法，如 @Param("guid") GUID guid
7.2 GUID直接用GUID，如 @Param("guid") GUID guid
7.3 XML 中特殊字符使用如：
<![CDATA[ <= ]]>