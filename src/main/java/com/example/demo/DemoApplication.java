package com.example.demo;

import com.example.demo.model.entity.SensorMeasurementEntity;
import com.example.demo.service.DataService;
import lombok.RequiredArgsConstructor;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }


    @Component
    @RequiredArgsConstructor
    public class DemoRunner implements CommandLineRunner {

        private final DataService dataService;

        @Override
        public void run(String... args) throws Exception {
            final Random rand = new Random();
            final int years = 50;

            Set<SensorMeasurementEntity> generatedData = generateUTCInstantsFromNowToYearsAgo(years).stream().map(instant -> {
                SensorMeasurementEntity measurementEntity = new SensorMeasurementEntity();
                measurementEntity.setUtcTimestamp(instant);
                measurementEntity.setData(rand.nextDouble(25.000, 28.000));
                return measurementEntity;
            }).collect(Collectors.toSet());

            //generatedData.forEach(dataService::addOne);

            final int monthsToGoBack = years * 12;

            final LocalDateTime reference = LocalDateTime.now(Clock.systemUTC());

            for (int month = 0; month < monthsToGoBack; month++) {
                final LocalDateTime monthReference = reference.minusMonths(month);

                final LocalDateTime beginDate = monthReference
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

                final LocalDateTime endDate = monthReference
                        .withDayOfMonth(monthReference.getMonth().length(monthReference.toLocalDate().isLeapYear()))
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

                dataService.findDelta(beginDate, endDate).forEach(System.out::println);
            }

            /*
            dataService.findAllMapped().forEach(System.out::println);
            dataService.findDelta(LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0),
                    LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0, 0)
            ).forEach(System.out::println);
             */
        }



        private Set<Instant> generateUTCInstantsFromNowToYearsAgo(int years) {
            Set<Instant> instants = new LinkedHashSet<>();

            final int monthsToGoBack = years * 12;
            final LocalDateTime reference = LocalDateTime.now(Clock.systemUTC());

            for (int monthOffset = 1; monthOffset < monthsToGoBack; monthOffset++) {
                final LocalDate calculatingDate = reference.minusMonths(monthOffset).toLocalDate();
                for (int day = 1; day < calculatingDate.getMonth().length(calculatingDate.isLeapYear()); day++) {
                    for (int hour = 0; hour < 24; hour++) {
                        Instant result = calculatingDate
                                .withDayOfMonth(day)
                                .atStartOfDay()
                                .withHour(hour)
                                .toInstant(ZoneOffset.UTC);

                        instants.add(result);
                    }
                }
            }

            return instants;
        }

    }

}
