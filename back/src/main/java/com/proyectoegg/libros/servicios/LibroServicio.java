package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Transactional
    public Libro agregarLibro(Libro libro) throws ServiceException {
        validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion());
        return libroRepositorio.save(libro);
    }

    @Transactional
    public Libro editarLibro(String idLibro) throws ServiceException {
        Optional<Libro> resultado = libroRepositorio.findById(idLibro);
       if (resultado.isPresent()) {
           Libro libro = resultado.get();
            Libro libroEditar = resultado.get();
           validar(libro.getTitulo(), libro.getMateria(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion());
            libroEditar.setTitulo(libro.getTitulo());
            libroEditar.setAutor(libro.getAutor());
            libroEditar.setDescripcion(libro.getDescripcion());
            libroEditar.setFechaLimite(libro.getFechaLimite());
            libroEditar.setDiasAnticipacion(libro.getDiasAnticipacion());
            libroEditar.setMateria(libro.getMateria());
            libroEditar.setObligatorio(libro.getObligatorio());
            return libroRepositorio.save(libroEditar);
        } else {
            throw new ServiceException("El libro indicado no se encuentra en el sistema");
        }

    }
  
//    @Transactional
//    public Libro cambiarLeido(String id) throws ServiceException {
//        Optional<Libro> resultado = libroRepositorio.findById(id);
//
//        if (resultado.isPresent()) {
//            Libro libro = resultado.get();
//            if (libro.getLeido()) {
//                libro.setLeido(Boolean.FALSE);
//            } else {
//                libro.setLeido(Boolean.TRUE);
//            }
//
//            return libroRepositorio.save(libro);
//        } else {
//            throw new ServiceException("El libro indicado no se encuentra en el sistema");
//        }
//    }

//    @Transactional
//    public Libro cambiarLeido(Libro libro) throws ServiceException{
//        try{
//            if (libro.getLeido()) {
//                libro.setLeido(Boolean.FALSE);
//            } else {
//                libro.setLeido(Boolean.TRUE);}
//            }catch(Exception e){
//                    throw new ServiceException("No pudo cambiarse el estado del libro");
//                    }
//        return libroRepositorio.save(libro);}
    
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

    public void validar(String titulo, String materia, Date fechaLimite, Integer diasAnticipacion, String descripcion) throws ServiceException {
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
        if (materia == null) {
            throw new ServiceException("Debe ingresar el nombre de la materia a agregar");
        }
    }
    
    @Transactional
    public void eliminar(Libro libro){
        try{
            libroRepositorio.delete(libro);
        }
        catch(Exception e)
        {System.out.println("No se pudo eliminar el libro");
            System.out.println(e.getMessage());
        }
    }

//    @Transactional
//    public void darDeBaja(Libro libro){
//        try{
//            libro.setAlta(false);
//            libroRepositorio.save(libro);
//        } catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        
//    }
    public Libro buscarPorId(String id) {
        return libroRepositorio.getById(id);
    }
    
    public ArrayList<Libro> listaLibrosLeidos(Usuario usuario, String materia){
        
        ArrayList<Libro> libros = new ArrayList<>();
        
        
        for (Libro  libro : usuario.getLibros()) {
            if(libro.getLeido() && libro.getMateria().equals(materia) && libro.getAlta()){
                libros.add(libro);
            }
        }
    return libros;}
    
    public ArrayList<Libro> listaLibrosNoLeidos(Usuario usuario, String materia){
        
        ArrayList<Libro> libros = new ArrayList<>();
        
        
        for (Libro  libro : usuario.getLibros()) {
            if(!libro.getLeido() && libro.getMateria().equals(materia) && libro.getAlta()){
                libros.add(libro);
                System.out.println(libro.getAlta());
            }
        }
        
    return libros;}

}
