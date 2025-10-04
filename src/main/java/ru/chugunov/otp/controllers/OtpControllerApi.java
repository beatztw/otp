package ru.chugunov.otp.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.chugunov.otp.dto.common.CommonRequest;
import ru.chugunov.otp.dto.common.CommonResponse;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;

public interface OtpControllerApi {

    @PostMapping("/generateAndSend")
    CommonResponse<Void> generateAndSendOtp(@RequestBody @Valid CommonRequest<GeneratedOtpRequest> commonRequest);

    @PostMapping("/check")
    CommonResponse<Void> checkOtp(@RequestBody @Valid CommonRequest<CheckOtpRequest> commonRequest);
}
