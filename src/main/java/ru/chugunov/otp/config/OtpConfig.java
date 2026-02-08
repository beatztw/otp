package ru.chugunov.otp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.chugunov.otp.model.SendingChannel;
import ru.chugunov.otp.service.sender.OtpSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class OtpConfig {

    private final List<OtpSender> otpSenders;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Map<SendingChannel, OtpSender> otpSenderMap() {
        return otpSenders.stream()
                .collect(Collectors.toUnmodifiableMap(
                        OtpSender::getSendingChannel,
                        Function.identity()
                ));
    }
}
