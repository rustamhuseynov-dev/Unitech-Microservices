package com.rustam.ms_account.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public class DateMapper {

    @Named("localDateTimeToString")
    public String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null;
    }

    @Named("longToLocalDateTime")
    public LocalDateTime longToLocalDateTime(Long timestamp) {
        return timestamp != null ? LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC) : null;
    }

}
