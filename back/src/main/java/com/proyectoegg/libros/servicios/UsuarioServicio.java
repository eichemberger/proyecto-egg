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
import java.util.Random;
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
    private static BCryptPasswordEncoder passwordEcorder = new BCryptPasswordEncoder();

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
    public Usuario guardar(Usuario usuario, MultipartFile archivo) throws ServiceException, Exception {

        usuario.setContrasenia(passwordEcorder.encode(usuario.getContrasenia()));
        usuario.setAlta(true);

        if (!archivo.isEmpty()) {
            try {
                Foto foto = fotoServicio.guardar(archivo);
                usuario.setFoto(foto);
            } catch (ServiceException | IOException e) {
                throw new Exception(e.getMessage());
            }
        }
        return usuarioRepositorio.save(usuario);
    }


    @Transactional
    public List<Materia> getAllMaterias(Usuario usuario) {
        return usuario.getMaterias();
    }

    @Transactional
    public Usuario editar(Usuario usuario, String id, MultipartFile archivo) throws ServiceException, IOException, Exception {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuarioEditar = resultado.get();
            //validarEdicion(usuario.getNombre(), usuario.getEmail(), usuarioEditar.getEmail());
            usuarioEditar.setNombre(usuario.getNombre());
            usuarioEditar.setEmail(usuario.getEmail());

            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }
            Foto foto = fotoServicio.editar(idFoto, archivo);
            if(foto != null){
            usuarioEditar.setFoto(foto);
            }
            return usuarioRepositorio.save(usuarioEditar);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    public Boolean verificarContrasenia(Usuario usuario, String contrasenia) {
        return passwordEcorder.matches(contrasenia, usuario.getContrasenia());

    }

    @Transactional
    public Usuario cambiarContrasenia(String id, String contrasenia) throws ServiceException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuarioEditar = resultado.get();
            usuarioEditar.setContrasenia(passwordEcorder.encode(contrasenia));
            return usuarioRepositorio.save(usuarioEditar);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    protected String randomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // largo del string
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public void eliminar(String id) throws ServiceException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            Usuario usuario = resultado.get();
            usuario.setAlta(Boolean.FALSE);
            usuario.setEmail(randomString()+"@"+randomString()+".com");
            usuarioRepositorio.save(usuario);
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }

    public Usuario encontrarPorID(String id) {
        return usuarioRepositorio.getById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
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
    public void eliminarDefinitivo(String id) throws ServiceException {
        usuarioRepositorio.eliminarPorId(id);
    }

    // TODO: Arreglar
    private void validarEdicion(String nombre, String email, String emailViejo) throws ServiceException {
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

        if (usuarioRepositorio.findByEmail(email) != null && !email.equals(emailViejo)) {
            throw new ServiceException("El email ingresado ya se encuentra registrado");
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
