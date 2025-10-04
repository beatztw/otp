package ru.chugunov.otp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.chugunov.otp.dto.common.CommonRequest;
import ru.chugunov.otp.dto.common.CommonResponse;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;
import ru.chugunov.otp.dto.responses.CheckOtpResponse;
import ru.chugunov.otp.dto.responses.GeneratedOtpResponse;

@RequestMapping("/otp/api/v1/otp")
public interface OtpControllerApi {

    @PostMapping("/generateAndSend")
    @ResponseStatus(HttpStatus.OK)
    CommonResponse<GeneratedOtpResponse> generateAndSendOtp(CommonRequest<GeneratedOtpRequest> commonRequest);

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    CommonResponse<CheckOtpResponse> checkOtp(CommonRequest<CheckOtpRequest> commonRequest);
}
