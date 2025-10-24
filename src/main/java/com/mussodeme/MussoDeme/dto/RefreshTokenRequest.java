package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    
    @NotBlank(message = "Le refresh token est obligatoire")
    private String refreshToken;

    // Default constructor
    public RefreshTokenRequest() {
    }

    // Getters and Setters
    public @NotBlank(message = "Le refresh token est obligatoire") String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(@NotBlank(message = "Le refresh token est obligatoire") String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshTokenRequest)) return false;
        RefreshTokenRequest that = (RefreshTokenRequest) o;
        return refreshToken != null ? refreshToken.equals(that.refreshToken) : that.refreshToken == null;
    }

    @Override
    public int hashCode() {
        return refreshToken != null ? refreshToken.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}