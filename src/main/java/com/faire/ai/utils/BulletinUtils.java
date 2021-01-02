package com.faire.ai.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class BulletinUtils {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static String DEFAULT_TIMEZONE = "UTC";

    public static String getDateFromTimestamp(int timestamp){
        return Instant.ofEpochSecond(new Long(timestamp)).atZone(ZoneId.of(DEFAULT_TIMEZONE)).format(formatter);
    }
}
