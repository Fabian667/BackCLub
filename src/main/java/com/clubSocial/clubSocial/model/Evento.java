package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String lugar;

    private LocalDateTime fechaEvento;

    private Integer cupoMaximo;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado { PROGRAMADO, CANCELADO, FINALIZADO }
}