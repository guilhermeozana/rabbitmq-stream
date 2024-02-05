package com.rabbitmqstream.controller;

import com.rabbitmqstream.domain.dto.RequestDTO;
import com.rabbitmqstream.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> request(@RequestBody RequestDTO requestDTO) {
        requestService.request(requestDTO);

        return ResponseEntity.noContent().build();
    }
}
