package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Usuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSocioConverter implements AttributeConverter<Usuario.TipoSocio, String> {
    @Override
    public String convertToDatabaseColumn(Usuario.TipoSocio attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case TITULAR -> "titular";
            case FAMILIAR -> "familiar";
            case INVITADO -> "invitado";
        };
    }

    @Override
    public Usuario.TipoSocio convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String v = dbData.toLowerCase().trim();
        return switch (v) {
            case "titular" -> Usuario.TipoSocio.TITULAR;
            case "familiar" -> Usuario.TipoSocio.FAMILIAR;
            case "invitado" -> Usuario.TipoSocio.INVITADO;
            default -> throw new IllegalArgumentException("Tipo de socio desconocido: " + dbData);
        };
    }
}