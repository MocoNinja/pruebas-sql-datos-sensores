DROP TABLE IF EXISTS data;


create table sensor_data
(
    utc_timestamp timestamp         not null		constraint data_pkey		primary key,
    utc_offset    integer default 0 not null,
    data          double precision
);

alter table sensor_data
    owner to developer;
