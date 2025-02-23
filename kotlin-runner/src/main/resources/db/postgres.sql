SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';

select *
from t_base_file;

alter table t_base_file
    add _desc varchar(1024);

update t_base_file
set real_name='12'
where id = '1';

create table IF NOT EXISTS t_base_file
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY primary key,
    file_name     varchar(255),
    real_name     varchar(255),
    type          varchar(64),
    ABSOLUTE_PATH varchar(1024),
    relative_path varchar(1024),
    size          BIGINT,
    suffix        varchar(64),
    SERIAL_NUMBER varchar(255),
    sha256        varchar(64),
    code          varchar(64),
    width         int,
    height        int,
    is_deleted    varchar(1) default '0' not null,
    CREATE_TIME   TIMESTAMP,
    CREATE_ID     BIGINT,
    UPDATE_TIME   TIMESTAMP,
    UPDATE_ID     BIGINT,
    THUMBNAIL     varchar(1024)
);

create table t_biz_book
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY primary key,
    name          varchar(255),
    type          varchar(64),
    absolute_path varchar(1024),
    relative_path varchar(1024),
    page_num      bigint,
    serial_number varchar(255),
    code          varchar(64),
    description   varchar(1024),
    is_deleted    varchar(1) default '0'::character varying not null,
    create_time   timestamp,
    create_id     bigint,
    update_time   timestamp,
    update_id     bigint
);