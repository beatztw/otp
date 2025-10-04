package ru.chugunov.otp.controllers.enums;

import com.fasterxml.jackson.annotation.JsonProperty;


public enum SendingChannel {
    @JsonProperty("telegram")
    TELEGRAM,
    @JsonProperty("console")
    CONSOLE
}
