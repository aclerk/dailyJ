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

create table `note` -- 笔记表
(
    `note_id`     char(64) primary key, -- 笔记id
    `notebook_id` char(64),             -- 笔记本id
    `title`       text,                 -- 标题
    `summary`     text,                 -- 摘要
    `file_name`   text,                 -- 文件名称
    `create_time` datetime,             -- 创建时间
    `update_time` datetime              -- 更新时间
);

