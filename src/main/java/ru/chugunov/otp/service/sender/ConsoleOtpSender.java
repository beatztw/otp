package ru.chugunov.otp.service.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.chugunov.otp.model.SendingChannel;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class ConsoleOtpSender implements OtpSender {

    @Override
    public SendingChannel getSendingChannel() {
        return SendingChannel.CONSOLE;
    }

    @Override
    public void sendOtp(String target, String message, String sendMessageKey) {
        System.out.println(message);

        log.info("Одноразовый пароль был выведен в консоль");
    }
}
