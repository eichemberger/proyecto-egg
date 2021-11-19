package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

    // BUSCAR LIBRO(S)
    public List<Libro> getLibrosUsuario(Usuario usuario) {
        return libroRepositorio.findByUsuario(usuario);
    }

    public Libro getLibro(String id) throws ServiceException {
        Optional<Libro> libro = libroRepositorio.findById(id);
        if (libro.isPresent()) {
            return libro.get();
        } else {
            throw new ServiceException("El libro no existe");
        }

    }

    // AGREGAR
    @Transactional
    public void agregarLibro(Libro libro) {
        libro.setAlta(true);
        libro.setLeido(false);
        try {
            setearFechaAlarma(libro);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        libroRepositorio.save(libro);
    }

    // EDITAR / ELIMINAR UN LIBRO
    @Transactional
    public void editarLibro(Libro libro, String id) throws ServiceException {
        Libro libroEditar = verificarLibroId(id);

        libroEditar.setTitulo(libro.getTitulo());
        libroEditar.setAutor(libro.getAutor());
        libroEditar.setAlta(true);
        libroEditar.setDescripcion(libro.getDescripcion());
        libroEditar.setFechaLimite(libro.getFechaLimite());
        libroEditar.setDiasAnticipacion(libro.getDiasAnticipacion());
        libroEditar.setObligatorio(libro.getObligatorio());
        try {
            setearFechaAlarma(libroEditar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        libroRepositorio.save(libroEditar);
    }

    @Transactional
    public void eliminarDefinitivo(String id) throws ServiceException {
        verificarLibroId(id);
        libroRepositorio.eliminarPorId(id);
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Libro libro = verificarLibroId(id);
        libroRepositorio.delete(libro);
    }

    // DAR DE BAJA
    // Dar de baja todos los libros de una materia, asociada a un usuario
    public void darBajaLibrosPorMateriaYUsuario(Usuario usuario, Materia materia) {
        for (Libro libro : getLibrosByMateriaSinLeer(usuario, materia)) {
            if (libro.getAlta()) {
                cambiarAlta(libro);
            }
        }
    }

    @Transactional
    public void cambiarAlta(Libro libro) {
        libro.setAlta(!libro.getAlta());
        libroRepositorio.save(libro);
    }

    @Transactional
    public void cambiarLeido(Libro libro) {
        libro.setLeido(!libro.getLeido());
        libroRepositorio.save(libro);
    }

    // FILTROS
    public List<Libro> getAllLibrosAlta(Usuario usuario) {
        return libroRepositorio.findByUsuarioAndAltaTrue(usuario);
    }

    public List<Libro> getLibrosByMateria(Usuario usuario, Materia materia) {
        return libroRepositorio.findByUsuarioAndMateria(usuario, materia);
    }

    // Por materia (leido = false, alta = true)
    public List<Libro> getLibrosByMateriaSinLeer(Usuario usuario, Materia materia) {
        return libroRepositorio.findByUsuarioAndMateriaAndLeidoFalseAndAltaTrue(usuario, materia);
    }

    public List<Libro> getLibrosLeidos(Usuario usuario) {
        return libroRepositorio.findByUsuarioAndLeidoTrueAndAltaTrue(usuario);
    }

    public List<Libro> getLibrosEliminados(Usuario usuario) {
        return libroRepositorio.findByUsuarioAndAltaFalse(usuario);
    }

    public List<Libro> librosFechaLimiteActivoSinLeer(Usuario usuario) {
        return libroRepositorio.findByFechaLimiteAndAltaTrueAndLeidoFalse(new Date());
    }

    public List<Libro> librosFechaAlertaActivosSinLeer() {
        return libroRepositorio.findByFechaAlertaAndAltaTrueAndLeidoFalse(new Date());
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

    // CALCULAR DIAS RESTANTES ENTRE EL ACTUAL Y LA FECHA LIMITE
    public Long diasRestantesLimite(Libro libro) {
        Date limite = libro.getFechaLimite();
        LocalDate fechaLimite = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(limite));
        LocalDate fechaActual = LocalDate.now();
        return ChronoUnit.DAYS.between(fechaActual, fechaLimite);
    }

    // SETEAR FECHA DE ALARMA CONSIDERANDO FECHA LIMITE Y ANTICIPACION
    public Libro setearFechaAlarma(Libro libro) {
        LocalDate limite = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(libro.getFechaLimite()));
        LocalDate alerta = limite.minusDays(libro.getDiasAnticipacion()).plusDays(1);
        Date fechaAlerta = Date.from(alerta.atStartOfDay().toInstant(ZoneOffset.UTC));
        libro.setFechaAlerta(fechaAlerta);
        return libro;
    }
}
