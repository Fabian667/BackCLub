package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "informacion_club")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_club", length = 200, nullable = false)
    private String nombreClub;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "telefono_alternativo", length = 20)
    private String telefonoAlternativo;

    @Column(name = "whatsapp", length = 20)
    private String whatsapp;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Column(name = "horario_atencion", columnDefinition = "TEXT")
    private String horarioAtencion;

    @Column(name = "facebook", length = 255)
    private String facebook;

    @Column(name = "instagram", length = 255)
    private String instagram;

    @Column(name = "twitter", length = 255)
    private String twitter;

    @Column(name = "youtube", length = 255)
    private String youtube;

    @Column(name = "tiktok", length = 255)
    private String tiktok;

    @Column(name = "linkedin", length = 255)
    private String linkedin;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "logo", length = 255)
    private String logo;

    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;

    @Column(name = "imagen_galeria_1", length = 255)
    private String imagenGaleria1;

    @Column(name = "imagen_galeria_2", length = 255)
    private String imagenGaleria2;

    @Column(name = "imagen_galeria_3", length = 255)
    private String imagenGaleria3;

    @Column(name = "imagen_galeria_4", length = 255)
    private String imagenGaleria4;

    @Column(name = "imagen_galeria_5", length = 255)
    private String imagenGaleria5;

    @Column(name = "mapa_latitud", precision = 10, scale = 8)
    private BigDecimal mapaLatitud;

    @Column(name = "mapa_longitud", precision = 11, scale = 8)
    private BigDecimal mapaLongitud;

    @Column(name = "fundacion")
    private LocalDate fundacion;

    @Column(name = "mision", columnDefinition = "TEXT")
    private String mision;

    @Column(name = "vision", columnDefinition = "TEXT")
    private String vision;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;
}