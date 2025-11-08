package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public record UsuarioDto(
            Long id,
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            String dni,
            LocalDate fechaNacimiento,
            LocalDateTime fechaRegistro,
            Usuario.EstadoUsuario estado,
            Usuario.TipoCuenta tipoCuenta,
            Usuario.TipoSocio tipoSocio,
            String fotoPerfil
        ) {}

    private static UsuarioDto toDto(Usuario u){
        return new UsuarioDto(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTelefono(),
                u.getDireccion(),
                u.getDni(),
                u.getFechaNacimiento(),
                u.getFechaRegistro(),
                u.getEstado(),
                u.getTipoCuenta(),
                u.getTipoSocio(),
                u.getFotoPerfil()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDto> all(){
        return repo.findAll().stream().map(UsuarioController::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> byId(@PathVariable Long id){
        return repo.findById(id)
                .map(u -> ResponseEntity.ok(toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Request para creación completa de usuario (ADMIN)
    public record CreateUsuarioRequest(
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            String dni,
            LocalDate fechaNacimiento,
            String estado,       // "activo" | "suspendido" | "eliminado"
            String tipoCuenta,   // "socio" | "admin"
            String tipoSocio,    // "titular" | "familiar" | "invitado"
            String fotoPerfil,
            String password
    ) {}

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> create(@RequestBody CreateUsuarioRequest req){
        if (req.email() == null || req.email().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (repo.existsByEmail(req.email())){
            return ResponseEntity.badRequest().build();
        }

        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setApellido(req.apellido());
        u.setEmail(req.email());
        u.setTelefono(req.telefono());
        u.setDireccion(req.direccion());
        u.setDni(req.dni());
        u.setFechaNacimiento(req.fechaNacimiento());
        u.setFotoPerfil(req.fotoPerfil());

        if (req.estado() != null && !req.estado().isBlank()) {
            u.setEstado(parseEstado(req.estado()));
        }
        if (req.tipoCuenta() != null && !req.tipoCuenta().isBlank()) {
            u.setTipoCuenta(parseTipoCuenta(req.tipoCuenta()));
        }
        if (req.tipoSocio() != null && !req.tipoSocio().isBlank()) {
            u.setTipoSocio(parseTipoSocio(req.tipoSocio()));
        }

        if (req.password() == null || req.password().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        u.setPasswordHash(passwordEncoder.encode(req.password()));

        u = repo.save(u);
        return ResponseEntity.ok(toDto(u));
    }

    // Request para actualización completa (PUT) de usuario
    public record UpdateUsuarioRequest(
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            String dni,
            LocalDate fechaNacimiento,
            String estado,
            String tipoCuenta,
            String tipoSocio,
            String fotoPerfil,
            String password // opcional: si viene, se reemplaza
    ) {}

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> update(@PathVariable Long id, @RequestBody UpdateUsuarioRequest req){
        return repo.findById(id).<ResponseEntity<UsuarioDto>>map(existing -> {
            // email requerido y único
            if (req.email() == null || req.email().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            if (!existing.getEmail().equals(req.email()) && repo.existsByEmail(req.email())) {
                return ResponseEntity.badRequest().build();
            }

            existing.setNombre(req.nombre());
            existing.setApellido(req.apellido());
            existing.setEmail(req.email());
            existing.setTelefono(req.telefono());
            existing.setDireccion(req.direccion());
            existing.setDni(req.dni());
            existing.setFechaNacimiento(req.fechaNacimiento());
            existing.setFotoPerfil(req.fotoPerfil());

            if (req.estado() != null && !req.estado().isBlank()) {
                existing.setEstado(parseEstado(req.estado()));
            } else {
                existing.setEstado(Usuario.EstadoUsuario.ACTIVO);
            }

            if (req.tipoCuenta() != null && !req.tipoCuenta().isBlank()) {
                existing.setTipoCuenta(parseTipoCuenta(req.tipoCuenta()));
            } else {
                existing.setTipoCuenta(Usuario.TipoCuenta.SOCIO);
            }

            if (req.tipoSocio() != null && !req.tipoSocio().isBlank()) {
                existing.setTipoSocio(parseTipoSocio(req.tipoSocio()));
            } else {
                existing.setTipoSocio(Usuario.TipoSocio.TITULAR);
            }

            if (req.password() != null && !req.password().isBlank()) {
                existing.setPasswordHash(passwordEncoder.encode(req.password()));
            }

            Usuario saved = repo.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    private static Usuario.EstadoUsuario parseEstado(String v){
        return switch (v.toLowerCase().trim()){
            case "activo" -> Usuario.EstadoUsuario.ACTIVO;
            case "suspendido" -> Usuario.EstadoUsuario.SUSPENDIDO;
            case "eliminado" -> Usuario.EstadoUsuario.ELIMINADO;
            default -> throw new IllegalArgumentException("Estado inválido: " + v);
        };
    }

    private static Usuario.TipoCuenta parseTipoCuenta(String v){
        return switch (v.toLowerCase().trim()){
            case "socio" -> Usuario.TipoCuenta.SOCIO;
            case "admin" -> Usuario.TipoCuenta.ADMIN;
            default -> throw new IllegalArgumentException("Tipo de cuenta inválido: " + v);
        };
    }

    private static Usuario.TipoSocio parseTipoSocio(String v){
        return switch (v.toLowerCase().trim()){
            case "titular" -> Usuario.TipoSocio.TITULAR;
            case "familiar" -> Usuario.TipoSocio.FAMILIAR;
            case "invitado" -> Usuario.TipoSocio.INVITADO;
            default -> throw new IllegalArgumentException("Tipo de socio inválido: " + v);
        };
    }

    // PATCH: actualización parcial no destructiva
    public record PatchUsuarioRequest(
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            String dni,
            LocalDate fechaNacimiento,
            String estado,
            String tipoCuenta,
            String tipoSocio,
            String fotoPerfil,
            String password
    ) {}

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> patch(@PathVariable Long id, @RequestBody PatchUsuarioRequest req){
        return repo.findById(id).<ResponseEntity<UsuarioDto>>map(existing -> {
            if (req.email() != null && !req.email().isBlank()){
                if (!existing.getEmail().equals(req.email()) && repo.existsByEmail(req.email())){
                    return ResponseEntity.badRequest().build();
                }
                existing.setEmail(req.email());
            }

            if (req.nombre() != null) existing.setNombre(req.nombre());
            if (req.apellido() != null) existing.setApellido(req.apellido());
            if (req.telefono() != null) existing.setTelefono(req.telefono());
            if (req.direccion() != null) existing.setDireccion(req.direccion());
            if (req.dni() != null) existing.setDni(req.dni());
            if (req.fechaNacimiento() != null) existing.setFechaNacimiento(req.fechaNacimiento());
            if (req.fotoPerfil() != null) existing.setFotoPerfil(req.fotoPerfil());

            if (req.estado() != null && !req.estado().isBlank()){
                existing.setEstado(parseEstado(req.estado()));
            }
            if (req.tipoCuenta() != null && !req.tipoCuenta().isBlank()){
                existing.setTipoCuenta(parseTipoCuenta(req.tipoCuenta()));
            }
            if (req.tipoSocio() != null && !req.tipoSocio().isBlank()){
                existing.setTipoSocio(parseTipoSocio(req.tipoSocio()));
            }

            if (req.password() != null && !req.password().isBlank()){
                existing.setPasswordHash(passwordEncoder.encode(req.password()));
            }

            Usuario saved = repo.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }
}