package ru.chugunov.otp.controllers.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.chugunov.otp.AbstractTest;
import ru.chugunov.otp.controllers.enums.SendingChannel;
import ru.chugunov.otp.dto.common.CommonRequest;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OtpController.class)
class OtpControllerTest extends AbstractTest {

    @Test
    void when_postGenerateAndSendOtpWithValidRequest_expect_success() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(UUID.randomUUID())
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("+79123456789")
                .message("1234")
                .length(6)
                .ttl(60)
                .sessionTtl(300)
                .resendAttempts(2)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void when_postGenerateAndSendOtpWithNullProcessId_expect_badRequest() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(null)
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("+79123456789")
                .message("1234")
                .length(6)
                .ttl(60)
                .sessionTtl(300)
                .resendAttempts(2)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_postGenerateAndSendOtpWithInvalidOtpLength_expect_badRequest() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(UUID.randomUUID())
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("+79123456789")
                .message("1234")
                .length(10)
                .ttl(60)
                .sessionTtl(300)
                .resendAttempts(2)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_postGenerateAndSendOtpWithInvalidTtl_expect_badRequest() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(UUID.randomUUID())
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("+79123456789")
                .message("1234")
                .length(6)
                .ttl(20)
                .sessionTtl(300)
                .resendAttempts(2)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_postGenerateAndSendOtpWithInvalidResendAttempts_expect_badRequest() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(UUID.randomUUID())
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("+79123456789")
                .message("1234")
                .length(6)
                .ttl(60)
                .sessionTtl(300)
                .resendAttempts(5)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_postGenerateAndSendOtpWithEmptyTarget_expect_badRequest() throws Exception {
        GeneratedOtpRequest request = GeneratedOtpRequest.builder()
                .processID(UUID.randomUUID())
                .sendingChannel(SendingChannel.TELEGRAM)
                .target("")
                .message("1234")
                .length(6)
                .ttl(60)
                .sessionTtl(300)
                .resendAttempts(2)
                .resendTimeout(60)
                .build();

        CommonRequest<GeneratedOtpRequest> commonRequest = CommonRequest.<GeneratedOtpRequest>builder()
                .body(request)
                .build();

        mockMvc.perform(post("/api/v1/otp/generateAndSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_postCheckOtpWithValidRequest_expect_success() throws Exception {
        CheckOtpRequest checkOtpRequest = CheckOtpRequest.builder()
                .processID(UUID.randomUUID())
                .otp("123456")
                .build();

        CommonRequest<CheckOtpRequest> commonRequest = CommonRequest.<CheckOtpRequest>builder()
                .body(checkOtpRequest)
                .build();

        mockMvc.perform(post("/api/v1/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void when_postCheckOtpWithNullProcessId_expect_badRequest() throws Exception {
        CheckOtpRequest checkOtpRequest = CheckOtpRequest.builder()
                .processID(null)
                .otp("123456")
                .build();

        CommonRequest<CheckOtpRequest> commonRequest = CommonRequest.<CheckOtpRequest>builder()
                .body(checkOtpRequest)
                .build();

        mockMvc.perform(post("/api/v1/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void when_postCheckOtpWithEmptyOtp_expect_badRequest() throws Exception {
        CheckOtpRequest checkOtpRequest = CheckOtpRequest.builder()
                .processID(UUID.randomUUID())
                .otp("")
                .build();

        CommonRequest<CheckOtpRequest> commonRequest = CommonRequest.<CheckOtpRequest>builder()
                .body(checkOtpRequest)
                .build();

        mockMvc.perform(post("/api/v1/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void when_postCheckOtpWithNullOtp_expect_badRequest() throws Exception {
        CheckOtpRequest checkOtpRequest = CheckOtpRequest.builder()
                .processID(UUID.randomUUID())
                .otp(null)
                .build();

        CommonRequest<CheckOtpRequest> commonRequest = CommonRequest.<CheckOtpRequest>builder()
                .body(checkOtpRequest)
                .build();

        mockMvc.perform(post("/api/v1/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void when_postCheckOtpWithAllNullFields_expect_badRequest() throws Exception {
        CheckOtpRequest checkOtpRequest = CheckOtpRequest.builder()
                .processID(null)
                .otp(null)
                .build();

        CommonRequest<CheckOtpRequest> commonRequest = CommonRequest.<CheckOtpRequest>builder()
                .body(checkOtpRequest)
                .build();

        mockMvc.perform(post("/api/v1/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }
}