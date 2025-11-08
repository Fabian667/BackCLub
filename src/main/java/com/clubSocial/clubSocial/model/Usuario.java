package com.clubSocial.clubSocial.model;

import com.clubSocial.clubSocial.model.converter.EstadoUsuarioConverter;
import com.clubSocial.clubSocial.model.converter.TipoCuentaConverter;
import com.clubSocial.clubSocial.model.converter.TipoSocioConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_usuarios_email", columnList = "email", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;
    private String direccion;

    // Documento nacional de identidad
    private String dni;

    private LocalDate fechaNacimiento;

    private LocalDateTime fechaRegistro;

    @Convert(converter = EstadoUsuarioConverter.class)
    private EstadoUsuario estado;

    @Convert(converter = TipoCuentaConverter.class)
    private TipoCuenta tipoCuenta;

    // Tipo de socio: titular, familiar, invitado
    @Convert(converter = TipoSocioConverter.class)
    private TipoSocio tipoSocio;

    @Column(nullable = false)
    private String passwordHash;

    private String fotoPerfil;

    @PrePersist
    void prePersist(){
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
        if (estado == null) estado = EstadoUsuario.ACTIVO;
        if (tipoCuenta == null) tipoCuenta = TipoCuenta.SOCIO;
        if (tipoSocio == null) tipoSocio = TipoSocio.TITULAR;
    }

    public enum EstadoUsuario { ACTIVO, SUSPENDIDO, ELIMINADO }
    public enum TipoCuenta { SOCIO, ADMIN }
    public enum TipoSocio { TITULAR, FAMILIAR, INVITADO }
}