package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "instalaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instalacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String tipo;

    private Integer capacidadMaxima;
    private BigDecimal precioHora;

    @Column(length = 255)
    private String imagen; // URL de foto de la instalación

    @Convert(converter = com.clubSocial.clubSocial.model.converter.InstalacionEstadoConverter.class)
    private Estado estado;

    public enum Estado { ACTIVA, INACTIVA }
}