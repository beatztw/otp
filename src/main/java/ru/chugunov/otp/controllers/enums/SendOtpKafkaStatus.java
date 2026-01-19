package ru.chugunov.otp.controllers.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SendOtpKafkaStatus {

    @JsonProperty("SUCCESS")
    SUCCESS,
    @JsonProperty("ERROR")
    ERROR
}
