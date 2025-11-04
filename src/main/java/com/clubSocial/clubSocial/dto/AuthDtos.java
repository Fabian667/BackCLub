package com.clubSocial.clubSocial.dto;

import lombok.*;

public class AuthDtos {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResponse {
        private String token;
        private Long userId;
        private String nombre;
        private String apellido;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FixUserRequest {
        private String email;
        private String password;
        private String tipoCuenta; // NUEVO: permite cambiar entre SOCIO/ADMIN
    }
}