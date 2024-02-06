package com.rabbitmqstream.mqueue;

import com.rabbitmqstream.common.StreamListener;
import com.rabbitmqstream.domain.dto.RequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperStreamConsumer extends StreamListener<RequestDTO> implements CommandLineRunner {

    private final RabbitStreamTemplate rabbitStreamTemplate;

    @Override
    public void run(String... args) throws Exception {
        rabbitStreamTemplate.convertAndSend(
                RequestDTO.builder()
                        .username("teste")
                        .password("teste")
                        .build()
        );
    }

    @Override
    @Retryable(interceptor = "streamRetryOperationsInterceptorFactoryBean")
    public void onMessage(RequestDTO payload) {
        log.info("Consumer message: {}", payload);
    }

}
