create table `option` -- 配置表
(
    `option_id`    char(64) primary key,  -- 配置id
    `option_name`  varchar(256) not null, -- 配置名称
    `name_desc`    varchar(256),          -- 配置名称描述
    `option_value` varchar(256) not null, -- 配置值
    `value_desc`   varchar(256),          -- 配置值描述
    `create_time`  datetime               -- 创建时间
);

create table `notebook` -- 笔记本表
(
    `notebook_id` char(64) primary key,  -- 笔记本表id
    `parent_id`   char(64) default null, -- 笔记本父id
    `name`        text,                  -- 笔记本名称
    `create_time` datetime,              -- 笔记本创建时间
    `update_time` datetime               -- 笔记本修改时间
);

create table `notebook_area_rel` -- 笔记本与分区关系表
(
    `notebook_area_rel` char(64) primary key, -- 关系表id
    `notebook_id`       char(64),             -- 笔记本id
    `area_id`           char(64)              -- 区域id
);

create table `area`-- 笔记分区表
(
    `area_id`     char(64) primary key,  -- 区域id
    `parent_id`   char(64) default null, -- 区域父id
    `name`        text,                  -- 名称
    `create_time` datetime,              -- 创建时间
    `update_time` datetime               -- 更新时间
);

create table `note` -- 笔记表
(
    `note_id`     char(64) primary key, -- 笔记id
    `title`       text,                 -- 标题
    `summary`     text,                 -- 摘要
    `content`     longtext,             -- 内容
    `create_time` datetime,             -- 创建时间
    `update_time` datetime              -- 更新时间
);

