package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Instalacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class InstalacionEstadoConverter implements AttributeConverter<Instalacion.Estado, String> {
    @Override
    public String convertToDatabaseColumn(Instalacion.Estado attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case ACTIVA -> "disponible";
            case INACTIVA -> "no_disponible";
        };
    }

    @Override
    public Instalacion.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "disponible" -> Instalacion.Estado.ACTIVA;
            case "no_disponible", "no disponible", "inactiva" -> Instalacion.Estado.INACTIVA;
            default -> throw new IllegalArgumentException("Estado de instalacion desconocido: " + dbData);
        };
    }
}