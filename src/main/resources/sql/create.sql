create table `daily_option`
(
    `option_id` bigint primary key not null ,
    `option_name` varchar(256) not null ,
    `option_desc` varchar(256),
    `param_name` varchar(256) not null ,
    `param_desc` varchar(256),
    `value` varchar(256),
    `value_desc` varchar(256),
    `create_date` datetime
)