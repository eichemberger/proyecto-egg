package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    private LibroRepositorio libroRepositorio;

    @Autowired
    public LibroServicio(LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
    }
    // BUSQUEDA

    public List<Libro> buscarPorUsuarioId(Usuario usuario) {
        return libroRepositorio.buscarPorUsuarioId(usuario);
    }

    public Libro buscarPorId(String id) {
        return libroRepositorio.getById(id);
    }

    // AGREGAR

    @Transactional
    public Libro agregarLibro(Libro libro) throws ServiceException {
        libro.setAlta(true);
        libro.setLeido(false);
        validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion());
        return libroRepositorio.save(libro);
    }

    // EDITAR / ELIMINAR UN LIBRO

    @Transactional
    public Libro editarLibro(Libro libro, String id) throws ServiceException {
        Libro libroEditar = verificarLibroId(id);
        validar(libro.getTitulo().trim(), libroEditar.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion());
        libroEditar.setTitulo(libro.getTitulo());
        libroEditar.setAutor(libro.getAutor());
        libroEditar.setDescripcion(libro.getDescripcion());
        libroEditar.setFechaLimite(libro.getFechaLimite());
        libroEditar.setDiasAnticipacion(libro.getDiasAnticipacion());
        libroEditar.setObligatorio(libro.getObligatorio());
        libroRepositorio.save(libroEditar);
        libroRepositorio.flush();
        return libroEditar;
    }

    @Transactional
    public void cambiarAlta(String id) throws ServiceException {
        Libro libro = verificarLibroId(id);
        libro.setAlta(!libro.getAlta());
        libroRepositorio.save(libro);
    }

    @Transactional
    public void eliminarDefinitivo(String id) throws ServiceException{
        verificarLibroId(id);
        libroRepositorio.eliminarPorId(id);
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Libro libro = verificarLibroId(id);
        libroRepositorio.delete(libro);
    }

    @Transactional
    public void cambiarLeido(String id) throws ServiceException{
        Libro libro = verificarLibroId(id);
        libro.setLeido(true);
        System.out.println(libro.getLeido());
        libroRepositorio.save(libro);
    }

    // FILTROS

    public List<Libro> getLibrosMateriaNoLeidos(Usuario usuario, String materia) {
        return libroRepositorio.libroPorMateriaSinLeer(usuario, materia);
    }

    public List<Libro> getLibrosEliminados(Usuario usuario) {
        return libroRepositorio.getLibrosEliminados(usuario);
    }

    public List<Libro> getLibrosLeidos(Usuario usuario) {
        return libroRepositorio.getLibrosLeidos(usuario);
    }

    // VERIFICACIONES

    public Libro verificarLibroId(String id) throws ServiceException {
        Optional<Libro> resultado = libroRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new ServiceException("El libro indicado no se encuentra en el sistema");
        }
    }

    public void validar(String titulo, String materia, Date fechaLimite, Integer diasAnticipacion, String descripcion) throws ServiceException {
        if (titulo == null || titulo.trim().isEmpty()) {
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
        if (materia == null || materia.trim().isEmpty()) {
            throw new ServiceException("Debe ingresar el nombre de la materia a agregar");
        }
    }

}
