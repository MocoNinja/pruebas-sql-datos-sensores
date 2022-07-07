package com.example.demo;

import com.example.demo.model.entity.SensorMeasurementEntity;
import com.example.demo.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }


    @Component
    @Slf4j
    @RequiredArgsConstructor
    public class DemoRunner implements CommandLineRunner {

        private final DataService dataService;

        @Override
        public void run(String... args) throws Exception {
            final Random rand = new Random();
            int months = 15;
            List<SensorMeasurementEntity> generatedData = generateUTCInstantsFromNowToXMonthsAgoEveryHour(months).stream().map(instant -> {
                SensorMeasurementEntity measurementEntity = new SensorMeasurementEntity();
                measurementEntity.setUtcTimestamp(instant);
                measurementEntity.setData(rand.nextDouble(25.000, 28.000));
                return measurementEntity;
            }).collect(Collectors.toList());
            dataService.findAllMapped().forEach(System.out::println);
            dataService.findDelta(LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0),
                    LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0, 0)
            ).forEach(System.out::println);
        }

        private Set<Instant> generateUTCInstantsFromNowToXMonthsAgoEveryHour(int months) {
            Set<Instant> instants = new HashSet<>();
            LocalDateTime reference = LocalDateTime.now(Clock.systemUTC());
            for (int month = 0; month <= months; month++) {
                for (int day = 1; day < 28; day++) {
                    for (int hour = 23; hour >= 0; hour--) {
                        LocalDateTime creating = reference
                                .minusMonths(month)
                                .withDayOfMonth(day)
                                .withHour(hour)
                                .withMinute(0)
                                .withSecond(0)
                                .withNano(0);
                        instants.add(creating.toInstant(ZoneOffset.UTC));
                    }
                }
            }
            return instants;
        }
    }
}
