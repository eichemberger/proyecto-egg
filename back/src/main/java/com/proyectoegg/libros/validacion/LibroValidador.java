package com.proyectoegg.libros.validacion;

import com.proyectoegg.libros.entidades.Libro;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class LibroValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Libro.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Libro libro = (Libro) target;
        if ((libro.getFechaLimite().before(new Date())) || (libro.getFechaLimite().equals(new Date()))) {
            errors.rejectValue("fechaLimite", "Future.libro.fechaLimite");
        }
    }

}
