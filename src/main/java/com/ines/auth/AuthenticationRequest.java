package com.ines.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
