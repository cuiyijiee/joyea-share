create table album
(
    album_id             int auto_increment
        primary key,
    user_id              int                   not null,
    album_name           varchar(255)          not null,
    album_desc           varchar(255)          null,
    created_at           datetime              not null,
    updated_at           datetime              null,
    shared               tinyint(1) default 0  not null,
    user_name            varchar(255)          null,
    refer_num            int        default 0  null,
    share_cover_neid     int                   null,
    share_desc           varchar(255)          null,
    download_num         int        default 0  null,
    like_num             int        default 0  null,
    hangye_tag_id        int        default -1 null,
    xianbie_tag_id       int        default -1 null,
    jixing_tag_id        int        default -1 null,
    jieduan_tag_id       int        default -1 null,
    shichang_tag_id      int        default -1 null,
    share_local_cover_id varchar(50)           null,
    copy_from            int                   null
)
    collate = utf8mb4_bin;

create index uid
    on album (user_id);

create table album_quote
(
    id         int auto_increment
        primary key,
    album_id   int         not null,
    creator    varchar(50) not null,
    quote_by   varchar(50) not null,
    created_at timestamp   not null
);

create table album_src
(
    id            int auto_increment
        primary key,
    album_id      int          not null,
    src_neid      varchar(255) not null,
    src_hash      varchar(255) not null,
    src_rev       varchar(255) not null,
    src_path      varchar(255) null,
    src_type      varchar(255) null,
    src_size      varchar(255) null,
    src_desc      varchar(255) null,
    created_at    datetime     null,
    src_bytes     bigint       null,
    src_file_name varchar(255) null
)
    collate = utf8mb4_bin;

create index aid
    on album_src (album_id);

create table download_record
(
    id            int auto_increment
        primary key,
    file_path     varchar(255) null,
    neid          int          null,
    file_name     varchar(255) null,
    created_at    timestamp    null,
    joyea_user_id varchar(50)  null
)
    collate = utf8mb4_general_ci;

create table joyea_user
(
    id         int auto_increment
        primary key,
    joyea_id   varchar(8)           not null,
    joyea_name varchar(50)          null,
    position   varchar(50)          null,
    department varchar(50)          null,
    update_at  timestamp            null,
    password   varchar(50)          not null,
    is_admin   tinyint(1) default 0 not null
)
    collate = utf8mb4_general_ci;

create table src_collect
(
    id         int auto_increment
        primary key,
    src_neid   int                              not null,
    user_id    int                              not null,
    src_path   varchar(255) collate utf8mb4_bin not null,
    src_type   varchar(255) collate utf8mb4_bin not null,
    src_hash   varchar(255) collate utf8mb4_bin not null,
    src_rev    varchar(255) collate utf8mb4_bin not null,
    src_size   varchar(255) collate utf8mb4_bin not null,
    created_at datetime                         not null
);

create table src_quote
(
    id         int auto_increment
        primary key,
    neid       bigint      null,
    uploader   varchar(50) not null,
    quote_by   varchar(50) not null,
    created_at timestamp   null
);

create table upload_integral
(
    id         int auto_increment
        primary key,
    neid       bigint        not null,
    uploader   varchar(50)   not null,
    integral   int default 1 not null,
    created_at timestamp     not null
);

create table upload_record
(
    id               int auto_increment
        primary key,
    uploader         varchar(50)          null,
    src_neid         varchar(50)          null,
    created_at       timestamp            null,
    checked          tinyint(1)           null,
    checked_at       timestamp            null,
    tags             varchar(255)         null,
    upload_path      varchar(255)         null,
    src_type         varchar(255)         null,
    src_rev          varchar(255)         null,
    src_hash         varchar(255)         null,
    src_name         varchar(255)         null,
    upload_path_neid varchar(50)          null,
    refuse_reason    varchar(255)         null,
    temp_src_name    varchar(100)         null,
    src_desc         varchar(255)         null,
    is_pc_upload     tinyint(1) default 0 null,
    finished         tinyint(1) default 0 null
)
    collate = utf8mb4_general_ci;

