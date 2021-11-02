package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.MateriaRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MateriaServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MateriaRepositorio materiaRepositorio;

    @Transactional
    public Materia agregarMateria(String nombre, Usuario usuario) throws ServiceException {

        Materia materia = new Materia();

        validar(nombre, usuario);

        materia.setNombre(nombre);
        materia.setUsuario(usuario);

        return materiaRepositorio.save(materia);
    }

    @Transactional
    public Materia editar(String id, String nombre, Usuario usuario) throws ServiceException {
        Optional<Materia> resultado = materiaRepositorio.findById(id);
        if (resultado.isPresent()) {
            Materia materia = resultado.get();
            validar(nombre, usuario);
            materia.setNombre(nombre);
            materia.setUsuario(usuario);

            return materiaRepositorio.save(materia);
        } else {
            throw new ServiceException("La materia no se encuentra en el sistema");
        }
    }
    
    public void eliminar(String id) throws ServiceException {
        Optional<Materia> resultado = materiaRepositorio.findById(id);
        if (resultado.isPresent()) {
            Materia materia = resultado.get();
            materiaRepositorio.delete(materia);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }
    
    public Materia encontrarPorID(String id) {
        return materiaRepositorio.getById(id);
    }
    
    public Materia encontrarPorNombre(String nombre) {
        return materiaRepositorio.buscarPorNombre(nombre);
    }

    public void validar(String nombre, Usuario usuario) throws ServiceException, ServiceException {
        if (nombre.isEmpty() || nombre == null || nombre.equals(" ") || nombre.contains("  ")) {
            throw new ServiceException("Debe ingresar el nombre de una materia");
        }
        if (encontrarPorNombre(nombre) != null) {
            throw new ServiceException("La materia ya existe");
        }
        if (usuarioServicio.encontrarPorID(usuario.getId()) == null) {
            throw new ServiceException("No se encontró el usuario");
        }
    }
}
