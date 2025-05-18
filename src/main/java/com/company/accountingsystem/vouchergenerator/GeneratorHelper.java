package com.company.accountingsystem.vouchergenerator;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class GeneratorHelper {

    public static LocalDate createRandomDateToLocalDate(Integer startYear, Integer startMonth, Integer startDay,
                                             Integer endYear, Integer endMonth, Integer endDay) {
        Faker faker = new Faker();
        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);
        Date randomDate = faker.date().between(
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        LocalDate localDate = randomDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return localDate;
    }
    public static LocalDateTime createRandomDateToLocalDateTime(Integer startYear, Integer startMonth, Integer startDay,
                                                 Integer endYear, Integer endMonth, Integer endDay) {
        Faker faker = new Faker();
        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);
        Date randomDate = faker.date().between(
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        LocalDateTime localDateTime = randomDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().atStartOfDay();

        return localDateTime;
    }
}
