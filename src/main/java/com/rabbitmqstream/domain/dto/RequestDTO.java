package com.rabbitmqstream.domain.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record RequestDTO(
        String username,
        String password
) {
}
