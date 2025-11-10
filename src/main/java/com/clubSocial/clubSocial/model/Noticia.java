package com.clubSocial.clubSocial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.clubSocial.clubSocial.model.converter.NoticiaEstadoConverter;

@Entity
@Table(name = "noticias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String titulo;

    @Column(length = 255)
    private String subtitulo;

    @Column(length = 4000)
    private String contenido;

    @Column(length = 255)
    private String imagen;

    @Column(length = 255)
    private String autor;

    @Column(length = 255)
    private String editor;

    // destacada tinyint(1)
    @Column(name = "destacada")
    private boolean destacada;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Convert(converter = NoticiaEstadoConverter.class)
    @Column(name = "estado")
    private Estado estado;

    public enum Estado { BORRADOR, PUBLICADA, ARCHIVADA }
}