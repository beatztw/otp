package ru.chugunov.otp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SendOtpStatus {

    @JsonProperty("in_process")
    IN_PROCESS,
    @JsonProperty("delivered")
    DELIVERED,
    @JsonProperty("error")
    ERROR
}
