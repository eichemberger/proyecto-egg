package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Foto;
import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.UsuarioRepositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    UsuarioRepositorio usuarioRepositorio;
    LibroServicio libroServcio;
    FotoServicio fotoServicio;
    MateriaServicio materiaServicio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, LibroServicio libroServcio, FotoServicio fotoServicio, MateriaServicio materiaServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.libroServcio = libroServcio;
        this.fotoServicio = fotoServicio;
        this.materiaServicio = materiaServicio;
    }

    public Usuario getByEmail(String email){
        return usuarioRepositorio.findByEmail(email);
    }

    @Transactional
    public Usuario guardar(Usuario usuario, MultipartFile archivo) throws ServiceException {

        usuario.setContrasenia(new BCryptPasswordEncoder().encode(usuario.getContrasenia()));
        usuario.setAlta(true);

//        if (!archivo.isEmpty()) {
//            try {
//                Foto foto = fotoServicio.guardar(archivo);
//                usuario.setFoto(foto);
//            } catch (Exception e) {
//                throw new Exception(e.getMessage());
//            }
//        }

        return usuarioRepositorio.save(usuario);
    }


    @Transactional
    public List<Materia> getAllMaterias(Usuario usuario) {
        return usuario.getMaterias();
    }

    @Transactional
    public Usuario editar(Usuario usuario) throws ServiceException, IOException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(usuario.getId());
        if (resultado.isPresent()) {
            Usuario usuarioEditar = resultado.get();
            usuarioEditar.setNombre(usuario.getNombre());
            usuarioEditar.setEmail(usuario.getEmail());
            usuarioEditar.setContrasenia(new BCryptPasswordEncoder().encode(usuario.getContrasenia()));

//            try {
//fotoServicio.editar(idFoto, archivo);
//} catch (Exception e) {
//        }
//            if (archivo != null) {
//                Foto foto = new Foto();
//                foto.setMime(archivo.getContentType());
//                foto.setContenido(archivo.getBytes());
//                foto.setNombre(archivo.getName());
//                usuario.setFoto(foto);
//            }
//            if (usuario.getFoto() != null) {
//                usuario.setFoto(usuario.getFoto());
//            }
            return usuarioRepositorio.save(usuarioEditar);
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

    public Usuario encontrarPorID(String id) {
        return usuarioRepositorio.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioRepositorio.findByEmail(email);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO"));

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getContrasenia(), authorities);

        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("El usuario solicitado no existe");
        }
    }

    @Transactional
    public void eliminar(String id) throws ServiceException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuario = resultado.get();
            usuario.setAlta(Boolean.FALSE);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    @Transactional
    public void eliminarDefinitivo(String id) throws ServiceException {
        verificarUsuarioId(id);
        usuarioRepositorio.eliminarPorId(id);
    }

    // TODO: Arreglar
    private void validarEdicion(String nombre, String email, String emailViejo, String contrasenia) throws ServiceException {
        if (nombre.isEmpty() || nombre == null || nombre.equals(" ") || nombre.contains("  ")) {
            throw new ServiceException("El nombre del usuario no puede estar vacío");
        }

//        if (usuarioRepositorio.buscarPorNombre(nombre) != null) {
//            throw new ServiceException("Ya existe un usuario registrado con ese nombre");
//        }
        if (email.isEmpty() || email == null || nombre.equals(" ")) {
            throw new ServiceException("El email no puede estar vacío");
        }

        if (!(email.contains("@")) || nombre.contains("  ")) {
            throw new ServiceException("Por favor, verifique que su email esta escrito correctamente");
        }

        if (usuarioRepositorio.buscarPorEmail(email) != null && !email.equals(emailViejo)) {
            throw new ServiceException("El email ingresado ya se encuentra registrado");
        }

        if (contrasenia.isEmpty() || contrasenia == null || contrasenia.contains(" ")) {
            throw new ServiceException("La contraseña no puede estar vacía");
        }

        if (contrasenia.length() < 8 || contrasenia.length() > 20) {
            throw new ServiceException("La contraseña debe tener entre 8 y 20 caracteres.");
        }

//        if (contrasenia2.isEmpty() || contrasenia2 == null || contrasenia2.contains(" ")) {
//            throw new ServiceException("La contraseña no puede estar vacía");
//        }
//
//        if (contrasenia2.length() < 8 || contrasenia2.length() > 20) {
//            throw new ServiceException("La contraseña debe tener entre 8 y 20 caracteres.");
//        }
//
//        if (!contrasenia.equals(contrasenia2)) {
//            throw new ServiceException("Las contraseñas no coinciden. Por favor verifique la información ingresada.");
//        }
    }


}
