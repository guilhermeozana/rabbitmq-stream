package com.rabbitmqstream.config;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import com.rabbitmq.stream.impl.StreamEnvironmentBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.rabbit.stream.retry.StreamRetryOperationsInterceptorFactoryBean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.unit.DataSize;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class EventStreamConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    Environment env(RabbitProperties rabbitProperties) {
        return new StreamEnvironmentBuilder()
                .host(rabbitProperties.getHost())
                .port(rabbitProperties.getStream().getPort())
                .username(rabbitProperties.getUsername())
                .password(rabbitProperties.getVirtualHost())
                .virtualHost(rabbitProperties.getVirtualHost())
                .build();
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    //Responsável por publicar a mensagem
    @Bean
    RabbitStreamTemplate rabbitStreamTemplate(Environment environment,
                                              Exchange superStream,
                                              Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        var template = new RabbitStreamTemplate(environment, superStream.getName());
        template.setMessageConverter(jackson2JsonMessageConverter);
        template.setProducerCustomizer(
                (s, builder) -> builder
                        .routing(Object::toString)
                        .producerBuilder()
        );

        return template;
    }

    //Responsável por consumir a mensagem

    @Bean
    RabbitListenerContainerFactory<StreamListenerContainer> streamContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);

        factory.setNativeListener(true);

        factory.setConsumerCustomizer(
                (id, builder) ->
                        builder.name(applicationName)
                                .offset(OffsetSpecification.first())
                                .manualTrackingStrategy()
        );

        return factory;
    }

    @Bean
    RetryTemplate basicStreamRetryTemplate() {
        return RetryTemplate.builder()
                .infiniteRetry()
                .exponentialBackoff(
                        TimeUnit.SECONDS.toMillis(10),
                        1.5,
                        TimeUnit.MINUTES.toMillis(5)
                ).build();
    }

    @Bean
    StreamRetryOperationsInterceptorFactoryBean streamRetryOperationsInterceptorFactoryBean(RetryTemplate basicStreamRetryTemplate) {
        var factoryBean = new StreamRetryOperationsInterceptorFactoryBean();
        factoryBean.setRetryOperations(basicStreamRetryTemplate);
        return factoryBean;
    }

    //Super Stream == Exchange
    @Bean
    Exchange superStream() {
        return ExchangeBuilder
                .directExchange(applicationName)
                .build();
    }

    //Partition == Queue
    @Bean
    Queue streamPartition() {
        return QueueBuilder
                .durable("partition-1")
                .stream()
                .withArgument("x-max-age", "7D")
                .withArgument("x-max-length-bytes", DataSize
                        .ofGigabytes(10).toBytes())
                .build();
    }

    // Connect Super Stream with Partition
    @Bean
    Binding streamPartitionBind(Exchange superStream, Queue streamPartition) {
        return BindingBuilder
                .bind(streamPartition)
                .to(superStream)
                .with("")
                .noargs();
    }

}
