package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Reserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ReservaEstadoConverter implements AttributeConverter<Reserva.Estado, String> {
    @Override
    public String convertToDatabaseColumn(Reserva.Estado attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case PENDIENTE -> "pendiente";
            case CONFIRMADA -> "confirmada";
            case CANCELADA -> "cancelada";
            case COMPLETADA -> "completada";
        };
    }

    @Override
    public Reserva.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String v = dbData.toLowerCase().trim();
        return switch (v) {
            case "pendiente" -> Reserva.Estado.PENDIENTE;
            case "confirmada" -> Reserva.Estado.CONFIRMADA;
            case "cancelada" -> Reserva.Estado.CANCELADA;
            case "completada" -> Reserva.Estado.COMPLETADA;
            default -> throw new IllegalArgumentException("Estado de reserva desconocido: " + dbData);
        };
    }
}