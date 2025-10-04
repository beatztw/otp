package ru.chugunov.otp.controllers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.chugunov.otp.controllers.OtpControllerApi;
import ru.chugunov.otp.dto.common.CommonRequest;
import ru.chugunov.otp.dto.common.CommonResponse;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;
import ru.chugunov.otp.dto.responses.CheckOtpResponse;
import ru.chugunov.otp.dto.responses.GeneratedOtpResponse;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OtpController implements OtpControllerApi {

    @Override
    public CommonResponse<GeneratedOtpResponse> generateAndSendOtp(CommonRequest<GeneratedOtpRequest> commonRequest) {

        return CommonResponse.<GeneratedOtpResponse>builder()
                .id(UUID.randomUUID())
                .body(null)
                .build();
    }

    @Override
    public CommonResponse<CheckOtpResponse> checkOtp(CommonRequest<CheckOtpRequest> commonRequest) {

        return CommonResponse.<CheckOtpResponse>builder()
                .id(UUID.randomUUID())
                .body(null)
                .build();
    }
}
