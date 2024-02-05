package com.rabbitmqstream.mqueue;

import com.rabbitmqstream.domain.dto.RequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestPublisher {
    private final RabbitStreamTemplate rabbitStreamTemplate;
    public void publisher(RequestDTO requestDTO) {
        log.info("Publisher new request: {}", requestDTO);
        rabbitStreamTemplate.convertAndSend(requestDTO);
    }
}
