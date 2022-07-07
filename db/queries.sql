select count(distinct  utc_timestamp) from sensor_data;

select
    avg(data),
    CONCAT(EXTRACT(MONTH from utc_timestamp), '-', EXTRACT(YEAR from utc_timestamp)) as timeframe
    from sensor_data
    GROUP BY CONCAT(EXTRACT(MONTH from utc_timestamp), '-', EXTRACT(YEAR from utc_timestamp))
    ;


delete from sensor_data where 1 > 0;

SELECT * FROM (
(
    select
        utc_timestamp,
        data
    from sensor_data
    where utc_timestamp >= DATE '2022-01-01' AND utc_timestamp <= DATE '2022-01-31'
    ORDER BY utc_timestamp ASC
    LIMIT 1
)
UNION ALL
(
    select
        utc_timestamp,
        data
    from sensor_data
    where utc_timestamp >= DATE '2022-01-01' AND utc_timestamp <= DATE '2022-01-31'
    ORDER BY utc_timestamp DESC
    LIMIT 1
)
) AS DATA;


SELECT begin.utc_timestamp as BEGIN_TIME, begin.data as BEGIN_DATA, endd.utc_timestamp as END_TIME, endd.data as END_DATA, endd.data - begin.data as DELTA, CONCAT(EXTRACT(MONTH from begin.utc_timestamp), '-', EXTRACT(YEAR from begin.utc_timestamp)) AS RANGE
FROM
(
    select
        utc_timestamp,
        data
    from sensor_data
    where utc_timestamp >= DATE '2022-01-01' AND utc_timestamp <= DATE '2022-01-31'
    ORDER BY utc_timestamp ASC
    LIMIT 1
) begin,

(
    select
        utc_timestamp,
        data
    from sensor_data
    where utc_timestamp >= DATE '2022-01-01' AND utc_timestamp <= DATE '2022-01-31'
    ORDER BY utc_timestamp DESC
    LIMIT 1
) endd

