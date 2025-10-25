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

ALTER TABLE T_BASE_FILE REPLICA IDENTITY FULL;
ALTER TABLE t_biz_book REPLICA IDENTITY FULL;