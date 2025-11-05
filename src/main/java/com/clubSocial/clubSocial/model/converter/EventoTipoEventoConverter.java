package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Evento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EventoTipoEventoConverter implements AttributeConverter<Evento.TipoEvento, String> {
    @Override
    public String convertToDatabaseColumn(Evento.TipoEvento attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case PUBLICO -> "publico";
            case SOCIOS -> "socios";
            case PRIVADO -> "privado";
        };
    }

    @Override
    public Evento.TipoEvento convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "publico" -> Evento.TipoEvento.PUBLICO;
            case "socios" -> Evento.TipoEvento.SOCIOS;
            case "privado" -> Evento.TipoEvento.PRIVADO;
            default -> throw new IllegalArgumentException("Tipo de evento desconocido: " + dbData);
        };
    }
}