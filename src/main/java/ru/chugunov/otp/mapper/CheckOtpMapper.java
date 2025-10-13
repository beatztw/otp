package ru.chugunov.otp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.model.CheckOtp;

@Mapper(componentModel = "spring")
public interface CheckOtpMapper {

    @Mapping(target = "id", ignore = true)
    CheckOtp fromCheckOtpRequestToEntity(CheckOtpRequest request);

}
