package com.example.demo.repository;

import com.example.demo.model.entity.SensorMeasurementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataRepository extends PagingAndSortingRepository<SensorMeasurementEntity, Instant> {

    String QUERY_AVG_ALL = """
            select avg(data), CONCAT(EXTRACT(MONTH from utc_timestamp), '-', EXTRACT(YEAR from utc_timestamp)) as timeframe from sensor_data GROUP BY CONCAT(EXTRACT(MONTH from utc_timestamp), '-', EXTRACT(YEAR from utc_timestamp))
            """;

    @Query(value = QUERY_AVG_ALL, nativeQuery = true)
    List<String> findAllItemsMappedByDateRange();

    String QUERY_DELTA_RANGE = """
            SELECT begin_datapoint.utc_timestamp as BEGIN_TIME,
                begin_datapoint.data as BEGIN_DATA,
                end_datapoint.utc_timestamp as END_TIME,
                end_datapoint.data as END_DATA,
                end_datapoint.data - begin_datapoint.data as DELTA,
                CONCAT(EXTRACT(MONTH from begin_datapoint.utc_timestamp), '-', EXTRACT(YEAR from begin_datapoint.utc_timestamp)) AS RANGE
            FROM
            (
                select
                    utc_timestamp,
                    data
                from sensor_data
                where utc_timestamp >= :beginDate AND utc_timestamp <= :endDate
                ORDER BY utc_timestamp ASC
                LIMIT 1
            ) begin_datapoint,
                        
            (
                select
                    utc_timestamp,
                    data
                from sensor_data
                where utc_timestamp >= :beginDate AND utc_timestamp <= :endDate
                ORDER BY utc_timestamp DESC
                LIMIT 1
            ) end_datapoint
            """;

    @Query(value = QUERY_DELTA_RANGE, nativeQuery = true)
    List<String> findDelta(@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);

}
