package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    private String tipo;

    @Convert(converter = com.clubSocial.clubSocial.model.converter.ActividadDiaSemanaConverter.class)
    private DiaSemana diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    private Integer cupoMaximo;

    private BigDecimal costo;

    @Convert(converter = com.clubSocial.clubSocial.model.converter.ActividadEstadoConverter.class)
    private Estado estado;

    public enum Estado { ACTIVA, INACTIVA }
    public enum DiaSemana { LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO }
}