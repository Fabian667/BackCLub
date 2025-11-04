package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    private String comprobante;

    private LocalDateTime fechaCreacion;

    @PrePersist
    void pre(){
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
    }

    public enum EstadoPago { PENDIENTE, APROBADO, RECHAZADO }
    public enum Moneda { ARS, USD, EUR }
}