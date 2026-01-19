package ru.chugunov.otp.utils;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static final String DEFAULT_DB_USER = "otp";

    public static final Long TTL_AFTER_WRITE_CACHE = 1L;

    public static final Long RESPONSE_KAFKA_COMPLETABLE_FUTURE_TIMEOUT = 5L;

}
