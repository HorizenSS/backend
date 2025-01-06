package com.ines.dto;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
