package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Actividad;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.text.Normalizer;

@Converter(autoApply = false)
public class ActividadEstadoConverter implements AttributeConverter<Actividad.Estado, String> {
    @Override
    public String convertToDatabaseColumn(Actividad.Estado attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case ACTIVA -> "activa";
            case INACTIVA -> "inactiva";
        };
    }

    @Override
    public Actividad.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String normalized = normalize(dbData);
        return switch (normalized) {
            case "activa" -> Actividad.Estado.ACTIVA;
            case "inactiva", "no_disponible", "no disponible" -> Actividad.Estado.INACTIVA;
            default -> throw new IllegalArgumentException("Estado de actividad desconocido: " + dbData);
        };
    }

    private String normalize(String s){
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}", "");
        return n.toLowerCase().trim();
    }
}