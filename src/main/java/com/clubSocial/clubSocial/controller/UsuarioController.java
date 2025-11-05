package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) { this.repo = repo; }

    public record UsuarioDto(
            Long id,
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            LocalDate fechaNacimiento,
            LocalDateTime fechaRegistro,
            Usuario.EstadoUsuario estado,
            Usuario.TipoCuenta tipoCuenta,
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
                u.getFechaNacimiento(),
                u.getFechaRegistro(),
                u.getEstado(),
                u.getTipoCuenta(),
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
}