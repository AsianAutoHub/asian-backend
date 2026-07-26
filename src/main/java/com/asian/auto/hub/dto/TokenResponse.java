package com.asian.auto.hub.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String tokenType,
        UserRolesDto user
) {

    public static TokenResponse of(String accessToken, String refreshToken, long expiresIn, UserRolesDto user) {
        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
    }

}
