package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.dto.AuthDtos;
import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import com.clubSocial.clubSocial.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthResponse> register(@RequestBody AuthDtos.RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@RequestBody AuthDtos.LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication authentication){
        String email = authentication.getName();
        Usuario u = usuarioRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(u);
    }

    // Endpoint de utilidad temporal para activar usuario y ajustar password y rol
    @PostMapping("/dev/fix-user")
    public ResponseEntity<Usuario> fixUser(@RequestBody AuthDtos.FixUserRequest request){
        Usuario u = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        u.setEstado(Usuario.EstadoUsuario.ACTIVO);
        // Cambiar tipoCuenta si se provee explícitamente
        if (request.getTipoCuenta() != null && !request.getTipoCuenta().isBlank()) {
            switch (request.getTipoCuenta().toUpperCase()) {
                case "ADMIN" -> u.setTipoCuenta(Usuario.TipoCuenta.ADMIN);
                case "SOCIO" -> u.setTipoCuenta(Usuario.TipoCuenta.SOCIO);
                default -> throw new IllegalArgumentException("Tipo de cuenta inválido: " + request.getTipoCuenta());
            }
        } else if (u.getTipoCuenta() == null) {
            // Asegurar valor por defecto
            u.setTipoCuenta(Usuario.TipoCuenta.SOCIO);
        }
        // Actualizar password si viene en la request
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        u = usuarioRepository.save(u);
        return ResponseEntity.ok(u);
    }
}