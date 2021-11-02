package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Foto;
import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    LibroServicio libroServcio;
    
//    public Libro agregarLibro(Libro libro){
//        
//    }
//    
    
    @Transactional
    public Usuario guardar(String nombre, String email, String contrasenia, String contrasenia2, MultipartFile archivo) throws ServiceException, IOException {
        validar(nombre, email, contrasenia, contrasenia2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContrasenia(contrasenia);
        usuario.setAlta(true);

        if (archivo != null) {
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setContenido(archivo.getBytes());
            foto.setNombre(nombre);
            usuario.setFoto(foto);
        }

        return usuarioRepositorio.save(usuario);
    }

    @Transactional
    public Usuario editar(String id, String nombre, String email, String contrasenia, String contrasenia2, MultipartFile archivo) throws ServiceException, IOException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuario = resultado.get();
            validar(nombre, email, contrasenia, contrasenia2);
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContrasenia(contrasenia);

            if (archivo != null) {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setContenido(archivo.getBytes());
                foto.setNombre(nombre);
                usuario.setFoto(foto);
            }
            return usuarioRepositorio.save(usuario);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    public void eliminar(String id) throws ServiceException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuario = resultado.get();
            usuario.setAlta(Boolean.FALSE);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    private void validar(String nombre, String email, String contrasenia, String contrasenia2) throws ServiceException {

        if (nombre.isEmpty() || nombre == null || nombre.equals(" ") || nombre.contains("  ")) {
            throw new ServiceException("El nombre del usuario no puede estar vacío");
        }

        if (usuarioRepositorio.buscarPorNombre(nombre) != null) {
            throw new ServiceException("Ya existe un usuario registrado con ese nombre");
        }

        if (email.isEmpty() || email == null || nombre.equals(" ")) {
            throw new ServiceException("El email no puede estar vacío");
        }

        if (!(email.contains("@")) || nombre.contains("  ")) {
            throw new ServiceException("Por favor, verifique que su email esta escrito correctamente");
        }

        if (usuarioRepositorio.buscarPorEmail(email) != null) {
            throw new ServiceException("El email ingresado ya se encuentra registrado");
        }

        if (contrasenia.isEmpty() || contrasenia == null || contrasenia.contains(" ")) {
            throw new ServiceException("La contraseña no puede estar vacía");
        }

        if (contrasenia.length() < 8 || contrasenia.length() > 20) {
            throw new ServiceException("La contraseña debe tener entre 8 y 20 caracteres.");
        }

        if (contrasenia2.isEmpty() || contrasenia2 == null || contrasenia2.contains(" ")) {
            throw new ServiceException("La contraseña no puede estar vacía");
        }

        if (contrasenia2.length() < 8 || contrasenia2.length() > 20) {
            throw new ServiceException("La contraseña debe tener entre 8 y 20 caracteres.");
        }

        if (!contrasenia.equals(contrasenia2)) {
            throw new ServiceException("Las contraseñas no coinciden. Por favor verifique la información ingresada.");
        }
    }

    public Usuario encontrarPorID(String id){
        return usuarioRepositorio.getById(id);
    }
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }
    
}
