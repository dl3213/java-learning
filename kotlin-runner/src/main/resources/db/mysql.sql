
create table t_biz_book
(
    id            bigint auto_increment
        primary key,
    name          varchar(255)           null,
    type          varchar(64)            null,
    absolute_path varchar(1024)          null,
    relative_path varchar(1024)          null,
    page_num      mediumtext             null,
    serial_number varchar(255)           null,
    code          varchar(64)            null,
    IS_DELETED    varchar(2) default '0' not null,
    CREATE_TIME   timestamp              null,
    CREATE_ID     bigint                 null,
    UPDATE_TIME   timestamp              null,
    UPDATE_ID     bigint                 null
);
