package com.rabbitmqstream.service;

import com.rabbitmqstream.domain.dto.RequestDTO;
import com.rabbitmqstream.mqueue.RequestPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestPublisher requestPublisher;
    public void request(RequestDTO requestDTO){
        requestPublisher.publisher(requestDTO);
    }
}
