package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Usuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCuentaConverter implements AttributeConverter<Usuario.TipoCuenta, String> {
    @Override
    public String convertToDatabaseColumn(Usuario.TipoCuenta attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case SOCIO -> "socio";
            case ADMIN -> "admin";
        };
    }

    @Override
    public Usuario.TipoCuenta convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "socio" -> Usuario.TipoCuenta.SOCIO;
            case "admin" -> Usuario.TipoCuenta.ADMIN;
            default -> throw new IllegalArgumentException("Tipo de cuenta desconocido: " + dbData);
        };
    }
}