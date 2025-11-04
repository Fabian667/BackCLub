package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "noticias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String subtitulo;

    @Column(length = 4000)
    private String contenido;

    private String imagen;
    private String autor;
    private String editor;

    private LocalDateTime fechaPublicacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado { BORRADOR, PUBLICADA, OCULTA }
}