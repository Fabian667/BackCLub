package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Noticia;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class NoticiaEstadoConverter implements AttributeConverter<Noticia.Estado, String> {
    @Override
    public String convertToDatabaseColumn(Noticia.Estado attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case BORRADOR -> "borrador";
            case PUBLICADA -> "publicada";
            case ARCHIVADA -> "archivada";
        };
    }

    @Override
    public Noticia.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String v = dbData.toLowerCase().trim();
        return switch (v) {
            case "borrador" -> Noticia.Estado.BORRADOR;
            case "publicada" -> Noticia.Estado.PUBLICADA;
            case "archivada" -> Noticia.Estado.ARCHIVADA;
            default -> throw new IllegalArgumentException("Estado de noticia desconocido: " + dbData);
        };
    }
}