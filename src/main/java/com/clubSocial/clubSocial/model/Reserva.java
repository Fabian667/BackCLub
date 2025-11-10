package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import com.clubSocial.clubSocial.model.converter.ReservaEstadoConverter;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instalacion_id")
    private Instalacion instalacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "cantidad_personas")
    private Integer cantidadPersonas;

    @Convert(converter = ReservaEstadoConverter.class)
    @Column(name = "estado")
    private Estado estado;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "costo_total", precision = 38, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @PrePersist
    void prePersist(){
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = Estado.PENDIENTE;
        if (cantidadPersonas == null) cantidadPersonas = 1;
    }

    @PreUpdate
    void preUpdate(){
        fechaActualizacion = LocalDateTime.now();
    }

    public enum Estado { PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA }
}