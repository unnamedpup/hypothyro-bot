package com.github.hypothyro.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultDateChecker {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final int TWO_MONTHS_IN_SECONDS = 5_184_000;
    private final long date;

    public DefaultDateChecker(String dateAsText) {
        this.date = parseDate(dateAsText);
    }

    public DefaultDateChecker(long date) {
        this.date = date;
    }

    private long parseDate(String text) throws DateTimeParseException {
        LocalDate date = LocalDate.parse(text.replace("\\s", "").replace("(-)|(\\/)", "."), formatter);
        log.info("Date ttg: {}", date);
        ZoneId zoneId = ZoneId.systemDefault();
        log.info("ZondeId: {}", zoneId);
        long res = date.atStartOfDay(zoneId).toEpochSecond();
        if (res > Instant.now().getEpochSecond()) {
            throw new DateTimeParseException("Future time", "", 0);
        }
        return res;
    }

    public boolean isEarlierThen2Months() {
        long now = Instant.now().getEpochSecond();
        return now - date < TWO_MONTHS_IN_SECONDS;
    }

    public long getDate() {
        return date;
    }

    public LocalDate getDateAsLocalDate() {
        return LocalDate.ofInstant(
                Instant.ofEpochSecond(date),
               TimeZone.getDefault().toZoneId()
        );
    }

    public String getDateInRuFormat() {
        LocalDate localDate = LocalDate.ofInstant(
                Instant.ofEpochSecond(date),
               TimeZone.getDefault().toZoneId()
        );

        return localDate.format(formatter);
    }
}

