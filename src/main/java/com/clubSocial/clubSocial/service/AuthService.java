package com.clubSocial.clubSocial.service;

import com.clubSocial.clubSocial.dto.AuthDtos;
import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import com.clubSocial.clubSocial.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request){
        if (usuarioRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Usuario u = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        u = usuarioRepository.save(u);
        String token = jwtService.generateToken(u.getEmail());
        return new AuthDtos.AuthResponse(token, u.getId(), u.getNombre(), u.getApellido(), u.getEmail());
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Usuario u = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(u.getEmail());
        return new AuthDtos.AuthResponse(token, u.getId(), u.getNombre(), u.getApellido(), u.getEmail());
    }
}