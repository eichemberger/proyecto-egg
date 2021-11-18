package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.MateriaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

@Service
public class MateriaServicio {

    @Autowired
    private MateriaRepositorio materiaRepositorio;

    // AGREGAR
    @Transactional
    public Materia agregarMateria(Materia materia) {
        materia.setAlta(true);
        return materiaRepositorio.save(materia);
    }

    // FILTROS
    @Transactional(readOnly = true)
    public Materia getMateriaByUsuario(Usuario usuario, String nombre) {
        return materiaRepositorio.findByUsuarioAndNombre(usuario, nombre);
    }

    @Transactional
    public List<Materia> getMateriaByUsuarioAlta(Usuario usuario) {
        return materiaRepositorio.findByUsuarioAndAltaTrue(usuario);
    }

    @Transactional
    public List<Materia> getMateriasByUsuario(Usuario usuario) throws ServiceException {
        return materiaRepositorio.findByUsuario(usuario);
    }

    // ELIMINAR / DAR DE BAJA
    @Transactional
    public void cambiarAlta(Materia materia) {
        materia.setAlta(!materia.getAlta());
        materiaRepositorio.save(materia);
    }

    @Transactional
    public void darMateriaBaja(Materia materia) {
        materia.setAlta(false);
        materiaRepositorio.save(materia);
    }

    // EDITAR
    @Transactional
    public Materia editar(String id, String nombre) throws ServiceException {
        Optional<Materia> resultado = materiaRepositorio.findById(id);
        if (resultado.isPresent()) {
            Materia materia = resultado.get();
            materia.setNombre(nombre);
            return materiaRepositorio.save(materia);
        } else {
            throw new ServiceException("La materia no se encuentra en el sistema");
        }
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Materia materia = encontrarPorID(id);
        materiaRepositorio.eliminarPorId(id);
    }

    // BUSCAR MATERIA
    @Transactional
    public Materia encontrarPorID(String id) throws ServiceException {
        Optional<Materia> materia = materiaRepositorio.findById(id);
        if (materia.isPresent()) {
            return materiaRepositorio.getById(id);
        } else {
            throw new ServiceException("La materia indicada no se encuentra en el sistema");
        }
    }

    @Transactional
    public Materia getMateriaByUsuarioAndNombre(Usuario usuario, String nombre) throws ServiceException {
        return materiaRepositorio.findByUsuarioAndNombre(usuario, nombre);
    }

}
