package com.clubSocial.clubSocial.security;

import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        // Manejar tipoCuenta null, asignar SOCIO por defecto
        Usuario.TipoCuenta tipoCuenta = u.getTipoCuenta();
        if (tipoCuenta == null) {
            tipoCuenta = Usuario.TipoCuenta.SOCIO;
        }
        
        return User.withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles(tipoCuenta.name())
                .disabled(u.getEstado() != Usuario.EstadoUsuario.ACTIVO)
                .build();
    }
}