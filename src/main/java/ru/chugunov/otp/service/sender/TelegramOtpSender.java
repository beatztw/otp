package ru.chugunov.otp.service.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.chugunov.otp.controllers.enums.SendOtpKafkaStatus;
import ru.chugunov.otp.controllers.enums.SendingChannel;
import ru.chugunov.otp.dto.requests.SendOtpKafkaRequest;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.exception.KafkaSendOtpException;
import ru.chugunov.otp.kafka.producer.SendOtpKafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class TelegramOtpSender implements OtpSender {

    private final SendOtpKafkaProducer kafkaProducer;

    @Override
    public SendingChannel getSendingChannel() {
        return SendingChannel.TELEGRAM;
    }

    @Override
    public void sendOtp(String telegramChatId,
                        String message,
                        String sendMessageKey) {
        SendOtpKafkaRequest kafkaRequest = SendOtpKafkaRequest.builder()
                .id(sendMessageKey)
                .message(message)
                .telegramChatId(telegramChatId)
                .build();

        SendOtpKafkaResponse kafkaResponse = kafkaProducer.sendOtp(kafkaRequest);

        log.info("Сообщение отправлено в сервис отправки с id = {}", kafkaRequest.getId());

        if (SendOtpKafkaStatus.ERROR.equals(kafkaResponse.getStatus())) {
            throw new KafkaSendOtpException(kafkaResponse.getErrorMessage());
        }
    }
}
