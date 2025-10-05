package ru.chugunov.otp.controllers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chugunov.otp.controllers.OtpControllerApi;
import ru.chugunov.otp.dto.common.CommonRequest;
import ru.chugunov.otp.dto.common.CommonResponse;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
public class OtpController implements OtpControllerApi {

    @Override
    public CommonResponse<Void> generateAndSendOtp(CommonRequest<GeneratedOtpRequest> commonRequest) {

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public CommonResponse<Void> checkOtp(CommonRequest<CheckOtpRequest> commonRequest) {

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }
}
