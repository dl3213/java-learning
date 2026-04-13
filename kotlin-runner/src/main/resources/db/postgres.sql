SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';

create table T_SYS_USER
(
    ID              BIGINT   primary key,
    USERNAME        varchar(255),
    NICKNAME        varchar(255),
    PASSWORD        varchar(255),
    PHONE_NUMBER    varchar(255),
    LAST_LOGIN_TIME TIMESTAMP,
    IS_DELETED      varchar(1) default 0 not null,
    CREATE_TIME     TIMESTAMP,
    CREATE_ID       BIGINT,
    UPDATE_TIME     TIMESTAMP,
    UPDATE_ID       BIGINT
);

create table T_SYS_DATABASE
(
    ID          BIGINT   primary key,
    NAME        varchar(255),
    TYPE        varchar(255),
    HOST        varchar(255),
    PORT        varchar(255),
    USERNAME    varchar(255),
    PASSWORD    varchar(255),
    DATABASE    varchar(255),
    VERSION     INTEGER              default 0,
    IS_DELETED  varchar(1) default '0' not null,
    CREATE_TIME TIMESTAMP,
    CREATE_ID   BIGINT
);

create table t_base_file
(
    id            bigint                                     not null
        primary key,
    file_name     varchar(255),
    real_name     varchar(255),
    type          varchar(64),
    absolute_path varchar(1024),
    relative_path varchar(1024),
    size          bigint,
    suffix        varchar(64),
    serial_number varchar(255),
    sha256        varchar(64),
    code          varchar(64) default ''::character varying  not null,
    width         integer,
    height        integer,
    is_deleted    varchar(1)  default '0'::character varying not null,
    create_time   timestamp,
    create_id     bigint,
    update_time   timestamp,
    update_id     bigint,
    thumbnail     varchar(1024),
    click_count   integer     default 0                      not null
);

create table t_biz_book
(
    id            bigint                                    not null
        primary key,
    name          varchar(255),
    type          varchar(64),
    absolute_path varchar(1024),
    relative_path varchar(1024),
    page_num      bigint,
    serial_number varchar(255),
    code          varchar(64),
    is_deleted    varchar(2) default '0'::character varying not null,
    create_time   timestamp,
    create_id     bigint,
    update_time   timestamp,
    update_id     bigint,
    description   varchar(1024)
);

create table t_sys_config
(
    id            bigint                                     not null
        primary key,
    key    varchar(128)                              not null unique,
    value  text                                      not null default '',
    type   varchar(32)                               not null default 'string',
    name          varchar(128),
    description   varchar(512),
    is_system     boolean                                   not null default false,
    is_deleted    varchar(1)  default '0'::character varying not null,
    create_time   timestamp,
    create_id     bigint,
    update_time   timestamp,
    update_id     bigint
);

-- 示例数据
insert into t_sys_config (id, key, value, type, name, description, is_system, is_deleted, create_time)
values (1, 'site.name', 'My Site', 'string', '网站名称', '网站显示名称', false, '0', now())
    , (2, 'site.logo', '/assets/logo.png', 'string', '网站Logo', '网站Logo URL', false, '0', now())
    , (3, 'feature.ai.enabled', 'true', 'boolean', 'AI功能开关', '是否启用AI功能', false, '0', now())
    , (4, 'upload.max_size', '10485760', 'number', '最大上传大小', '单位：字节，默认10MB', false, '0', now());

ALTER TABLE T_BASE_FILE REPLICA IDENTITY FULL;
ALTER TABLE t_biz_book REPLICA IDENTITY FULL;