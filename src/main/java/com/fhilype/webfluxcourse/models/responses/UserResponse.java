package com.fhilype.webfluxcourse.models.responses;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) {
}
