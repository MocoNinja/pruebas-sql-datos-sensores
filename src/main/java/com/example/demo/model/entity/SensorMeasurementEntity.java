package com.example.demo.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "SENSOR_DATA")
@Data
public class SensorMeasurementEntity {

    @Id()
    @Column(name = "utc_timestamp")
    private Instant utcTimestamp;

    @Column(name = "utc_offset", nullable = false, columnDefinition = "integer default 0")
    private Integer utcOffset = 0;

    @Column(name = "data")
    private Double data;

}
