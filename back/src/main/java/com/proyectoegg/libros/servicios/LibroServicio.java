package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public Libro agregarLibro(Libro libro, String idUsuario) throws ServiceException {
        validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion(), idUsuario);
        libro.setIdUsuario(idUsuario);
        return libroRepositorio.save(libro);
    }

    @Transactional
    public Libro editarLibro(Libro libro) throws ServiceException {
//        Optional<Libro> resultado = libroRepositorio.findById(libro.getId());
//        if (resultado.isPresent()) {
//            Libro libroEditar = resultado.get();
////            validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion(), idUsuario);
////            libro.setIdUsuario(idUsuario);
//            libroEditar.setTitulo(libro.getTitulo());
//            libroEditar.setAutor(libro.getAutor());
//            libroEditar.setDescripcion(libro.getDescripcion());
//            libroEditar.setFechaLimite(libro.getFechaLimite());
//            libroEditar.setDiasAnticipacion(libro.getDiasAnticipacion());
//            libroEditar.setMateria(libro.getMateria());
//            libroEditar.setObligatorio(libro.getObligatorio());
//            return libroRepositorio.save(libroEditar);
//        } else {
//            throw new ServiceException("El libro indicado no se encuentra en el sistema");
//        }
        
                        //EDITAR CON ENTIDAD LIBRO COMO EL REGISTRO?
//        validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion(), idUsuario);
//        libro.setIdUsuario(idUsuario);
//        return libroRepositorio.save(libro);
        return null;
    }

    @Transactional
    public Libro cambiarLeido(String id) throws ServiceException {
        Optional<Libro> resultado = libroRepositorio.findById(id);

        if (resultado.isPresent()) {
            Libro libro = resultado.get();
            if (libro.getLeido()) {
                libro.setLeido(Boolean.FALSE);
            } else {
                libro.setLeido(Boolean.TRUE);
            }

            return libroRepositorio.save(libro);
        } else {
            throw new ServiceException("El libro indicado no se encuentra en el sistema");
        }
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Optional<Libro> resultado = libroRepositorio.findById(id);
        if (resultado.isPresent()) {
            Libro libro = resultado.get();
            libroRepositorio.delete(libro);
        } else {
            throw new ServiceException("La materia indicado no se encuentra en el sistema");
        }
    }

    public void validar(String titulo, String materia, Date fechaLimite, Integer diasAnticipacion, String descripcion, String idUsuario) throws ServiceException {
        if (titulo == null || titulo.isEmpty() || titulo.equals(" ")) {
            throw new ServiceException("Debe escribir un título");
        }
        if ((fechaLimite.before(new Date())) || (fechaLimite.equals(new Date()))) {
            throw new ServiceException("Debe seleccionar una fecha posterior a la actual");
        }
        //Esto después vemos bien
        if (diasAnticipacion < 1) {
            throw new ServiceException("La cantidad de días debe ser mayor uno");
        }
        if (descripcion.length() > 250) {
            throw new ServiceException("La descripción debe tener un máximo de 250 caracteres");
        }
        if (materia == null || materia.isEmpty()) {
            throw new ServiceException("Debe ingresar el nombre de la materia a agregar");
        }

        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new ServiceException("El usuario no puede ser nulo");
        }
    }

    public Libro buscarPorId(String id) {
        return libroRepositorio.getById(id);
    }

}
