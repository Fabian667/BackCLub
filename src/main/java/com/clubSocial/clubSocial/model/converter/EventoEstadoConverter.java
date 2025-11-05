package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Evento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EventoEstadoConverter implements AttributeConverter<Evento.Estado, String> {
    @Override
    public String convertToDatabaseColumn(Evento.Estado attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case ACTIVO -> "activo";
            case CANCELADO -> "cancelado";
            case FINALIZADO -> "finalizado";
        };
    }

    @Override
    public Evento.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "activo" -> Evento.Estado.ACTIVO;
            case "cancelado" -> Evento.Estado.CANCELADO;
            case "finalizado" -> Evento.Estado.FINALIZADO;
            default -> throw new IllegalArgumentException("Estado de evento desconocido: " + dbData);
        };
    }
}