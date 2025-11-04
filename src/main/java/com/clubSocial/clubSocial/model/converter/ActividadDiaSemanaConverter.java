package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Actividad;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.text.Normalizer;

@Converter(autoApply = false)
public class ActividadDiaSemanaConverter implements AttributeConverter<Actividad.DiaSemana, String> {
    @Override
    public String convertToDatabaseColumn(Actividad.DiaSemana attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case LUNES -> "lunes";
            case MARTES -> "martes";
            case MIERCOLES -> "miercoles";
            case JUEVES -> "jueves";
            case VIERNES -> "viernes";
            case SABADO -> "sabado";
            case DOMINGO -> "domingo";
        };
    }

    @Override
    public Actividad.DiaSemana convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String normalized = normalize(dbData);
        return switch (normalized) {
            case "lunes" -> Actividad.DiaSemana.LUNES;
            case "martes" -> Actividad.DiaSemana.MARTES;
            case "miercoles" -> Actividad.DiaSemana.MIERCOLES;
            case "jueves" -> Actividad.DiaSemana.JUEVES;
            case "viernes" -> Actividad.DiaSemana.VIERNES;
            case "sabado" -> Actividad.DiaSemana.SABADO;
            case "domingo" -> Actividad.DiaSemana.DOMINGO;
            default -> throw new IllegalArgumentException("Dia de semana desconocido: " + dbData);
        };
    }

    private String normalize(String s){
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}", ""); // quitar acentos
        return n.toLowerCase().trim();
    }
}