create table `option`
(
    `option_id`    char(64) primary key not null,
    `option_name`  varchar(256)         not null,
    `name_desc`    varchar(256),
    `option_value` varchar(256)         not null,
    `value_desc`   varchar(256),
    `create_time`  datetime
);

create table `note`
(
    `note_id`     char(64) primary key not null,
    `title`       text,
    `summary`     text,
    `content`     longtext,
    `create_time` datetime,
    `update_time` datetime,
    `catalog_id`  char(64)
);

create table `catalog`
(
    `catalog_id`  char(64) primary key not null,
    `parent_id`   char(64) default null,
    `name`        text     default not null,
    `type`        char(1),
    `create_time` datetime,
    `update_time` datetime
);

create table `tag`
(
    `tag_id`      char(64) primary key not null,
    `name`        text default not null,
    `create_time` datetime,
    `update_time` datetime
);

create table `note_tag_relation`
(
    `note_tag_id` char(64) primary key not null,
    `note_id` char(64) not null,
    `tag_id` char(64) not null,
    `create_time` datetime,
    `update_time` datetime
);