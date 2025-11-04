package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;

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
    private Instalacion instalacion;

    @ManyToOne(optional = false)
    private Usuario usuario;

    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private BigDecimal costoTotal;

    private LocalDateTime fechaCancelacion;

    public enum Estado { RESERVADA, CANCELADA, COMPLETADA }
}