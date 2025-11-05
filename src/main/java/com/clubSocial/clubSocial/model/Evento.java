package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String titulo;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 255)
    private String lugar;

    @Column(name = "fecha_evento")
    private LocalDateTime fechaEvento;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @ManyToOne
    @JoinColumn(name = "instalacion_id")
    private Instalacion instalacion;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento")
    private TipoEvento tipoEvento;

    @Column(length = 255)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    public enum Estado { ACTIVO, CANCELADO, FINALIZADO }
    public enum TipoEvento { PUBLICO, SOCIOS, PRIVADO }
}