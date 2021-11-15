package com.proyectoegg.libros.validacion;

import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UsuarioValidador implements Validator {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Usuario usuario = (Usuario) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contrasenia", "empty");
        if(!usuario.getContrasenia().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            errors.rejectValue("contrasenia", "pattern.usuario.contrasenia");
        }

        if(usuarioServicio.getByEmail(usuario.getEmail()) != null){
            errors.rejectValue("email", "usuario.emailrepetido");
        }
    }


}
