package com.rabbitmqstream.common;

import lombok.SneakyThrows;

@FunctionalInterface
public interface FunctionExcept<T, R> {

    R apply(T t) throws Exception;

    @SneakyThrows
    default R sneakyThrows(T args) {
        return apply(args);
    }

}
