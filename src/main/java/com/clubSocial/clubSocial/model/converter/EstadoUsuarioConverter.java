package com.clubSocial.clubSocial.model.converter;

import com.clubSocial.clubSocial.model.Usuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoUsuarioConverter implements AttributeConverter<Usuario.EstadoUsuario, String> {
    @Override
    public String convertToDatabaseColumn(Usuario.EstadoUsuario attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case ACTIVO -> "activo";
            case SUSPENDIDO -> "suspendido";
            case ELIMINADO -> "eliminado";
        };
    }

    @Override
    public Usuario.EstadoUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "activo" -> Usuario.EstadoUsuario.ACTIVO;
            case "suspendido" -> Usuario.EstadoUsuario.SUSPENDIDO;
            case "eliminado" -> Usuario.EstadoUsuario.ELIMINADO;
            default -> throw new IllegalArgumentException("Estado de usuario desconocido: " + dbData);
        };
    }
}