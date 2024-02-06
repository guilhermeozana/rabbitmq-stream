package com.rabbitmqstream.common;

import org.springframework.rabbit.stream.listener.StreamListenerContainer;

import java.util.function.BiFunction;

public interface StreamListenerContainerSimpleFactory<T> extends BiFunction<String, StreamListener<T>, StreamListenerContainer> {
}
