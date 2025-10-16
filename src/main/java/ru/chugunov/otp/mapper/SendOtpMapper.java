package ru.chugunov.otp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;
import ru.chugunov.otp.model.SendOtp;

@Mapper(componentModel = "spring")
public interface SendOtpMapper {

    @Mapping(target = "id", ignore = true)
    SendOtp fromGeneratedOtpRequestToEntity(GeneratedOtpRequest request);

}
