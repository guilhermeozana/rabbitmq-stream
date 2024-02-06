package com.rabbitmqstream.mqueue;

import com.rabbitmqstream.common.RabbitStreamTemplateSimpleFactory;
import com.rabbitmqstream.common.StreamListener;
import com.rabbitmqstream.common.StreamListenerContainerSimpleFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.SuperStream;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
@RequiredArgsConstructor
public class SuperStreamConfig {

    private static final String SUPER_STREAM = "super.stream";

    @Bean
    SuperStream superStream() {
        return new SuperStream(SUPER_STREAM, 3);
    }

    @Bean
    RabbitStreamTemplate rabbitStreamTemplate(RabbitStreamTemplateSimpleFactory factory) {
        return factory.apply(SUPER_STREAM);
    }

    @Bean
    <T> StreamListenerContainer container(StreamListenerContainerSimpleFactory<T> factory,
                                                 StreamListener<T> superStreamConsumer) {
        return factory.apply(SUPER_STREAM, superStreamConsumer);
    }
}
