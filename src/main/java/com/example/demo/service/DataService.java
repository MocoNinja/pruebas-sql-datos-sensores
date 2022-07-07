package com.example.demo.service;

import com.example.demo.model.entity.SensorMeasurementEntity;
import com.example.demo.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    public List<SensorMeasurementEntity> getAll() {
        List<SensorMeasurementEntity> data = new ArrayList<>();
        dataRepository.findAll().forEach(data::add);
        return data;
    }

    public void addOne(SensorMeasurementEntity data) {
        dataRepository.save(data);
    }

    public void addSeveral(SensorMeasurementEntity...data) {
        for (int i = 0; i < data.length; i++) {
            dataRepository.save(data[i]);
        }
    }

    public void addAll(List<SensorMeasurementEntity> items) {
        dataRepository.saveAll(items);
    }

    public List<String> findAllMapped() {
        return dataRepository.findAllItemsMappedByDateRange();
    }

    public List<String> findDelta(LocalDateTime beginDate, LocalDateTime endDate) {
        return dataRepository.findDelta(beginDate, endDate);
    }
}
