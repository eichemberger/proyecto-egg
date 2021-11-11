package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.MateriaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MateriaServicio {

    @Autowired
    private MateriaRepositorio materiaRepositorio;

    @Transactional
    public Materia agregarMateria(Materia materia) throws ServiceException {
        validar(materia.getNombre());
        materia.setAlta(true);
        return materiaRepositorio.save(materia);
    }

    @Transactional
    public Materia editar(Materia materia) throws ServiceException {
        Optional<Materia> resultado = materiaRepositorio.findById(materia.getId());
        if (resultado.isPresent()) {
            Materia mat = resultado.get();
            validar(materia.getNombre());
            materia.setNombre(materia.getNombre());
            materia.setAlta(true);
            return materiaRepositorio.save(materia);
        } else {
            throw new ServiceException("La materia no se encuentra en el sistema");
        }
    }

    @Transactional
    public void darDeBaja(Materia materia) throws ServiceException {
        try {
            materia.setAlta(false);
            materiaRepositorio.save(materia);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Optional<Materia> resultado = materiaRepositorio.findById(id);
        if (resultado.isPresent()) {
            Materia materia = resultado.get();
            materiaRepositorio.delete(materia);
        } else {
            throw new ServiceException("La materia indicada no se encuentra en el sistema");
        }
    }

    @Transactional
    public void eliminarBD(Usuario usuario, Materia materia){
        materiaRepositorio.eliminar(materia.getId(), usuario);
    }
    
    public List<Materia> listarPorUsuario(Usuario usuario){
        return materiaRepositorio.buscarPorUsuario(usuario);
    }

    public List<Materia> listarActivasPorUsuario(Usuario usuario){
        return materiaRepositorio.buscarActivasPorUsuario(usuario);
    }
    
    public Materia encontrarPorID(String id) {
        return materiaRepositorio.getById(id);
    }

    public Materia encontrarPorNombre(String nombre) {
        return materiaRepositorio.buscarPorNombre(nombre);
    }

    public void validar(String nombre) throws ServiceException, ServiceException {
        if (nombre.isEmpty() || nombre == null || nombre.equals(" ") || nombre.contains("  ")) {
            throw new ServiceException("Debe ingresar el nombre de una materia");
        }
    }

    public boolean materiaConLibros(Materia materia, Usuario usuario) {
        for (Libro aux : usuario.getLibros()) {
            if (aux.getMateria().equalsIgnoreCase(materia.getNombre())) {
                return true;
            }
        }
        return false;
    }

    public boolean materiaConLibrosSinLeer(Materia materia, Usuario usuario) {
        for (Libro aux : usuario.getLibros()) {
            if (aux.getMateria().equalsIgnoreCase(materia.getNombre()) && aux.getLeido() == false) {
                return true;
            }
        }
        return false;
    }
}
