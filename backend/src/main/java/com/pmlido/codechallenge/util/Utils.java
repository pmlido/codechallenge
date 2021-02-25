package com.pmlido.codechallenge.util;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.*;

@Component
public class Utils {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd hh:mm");

    public String getCurrentDate() {
        return DATETIME_FORMATTER.format(now());
    }
}
