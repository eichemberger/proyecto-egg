package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Foto;
import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;
import com.proyectoegg.libros.repositorios.MateriaRepositorio;
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

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    LibroServicio libroServicio;
    @Autowired
    LibroRepositorio libroRepositorio;
    @Autowired
    FotoServicio fotoServicio;
    @Autowired
    MateriaServicio materiaServicio;
    @Autowired
    MateriaRepositorio materiarepositorio;

    @Transactional
    public Usuario guardar(Usuario usuario, MultipartFile archivo) throws ServiceException, IOException, Exception {
        validar(usuario.getNombre(), usuario.getEmail(), usuario.getContrasenia());
        usuario.setContrasenia(new BCryptPasswordEncoder().encode(usuario.getContrasenia()));
        usuario.setAlta(true);

        try {
            Foto foto = fotoServicio.guardar(archivo);
            usuario.setFoto(foto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return usuarioRepositorio.save(usuario);
    }

    @Transactional
    public Usuario editar(Usuario usuario, String id) throws ServiceException, IOException {
            Usuario usuarioEditar = verificarUsuarioId(id);
            validarEdicion(usuario.getNombre(), usuario.getEmail(),usuarioEditar.getEmail(), usuario.getContrasenia());
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
            return usuarioRepositorio.save(usuarioEditar);}


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
    public void actualizarLibros(Usuario usuario) throws ServiceException {
        usuario.setLibros(libroServicio.buscarPorUsuarioId(usuario));
    }

    public void guardarMateriasUsuario(Usuario usuario) {
        try {
            usuario.setMaterias(materiaServicio.listarPorUsuario(usuario));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public Usuario agregarMateria(Usuario usuario, Materia materia) throws ServiceException {
        try {
            usuario.setMaterias(materiaServicio.listarActivasPorUsuario(usuario));
            return usuarioRepositorio.save(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServiceException("La materia indicada no ha podido ser incorporada al usuario");
        }
    }

   @Transactional
    public void agregarLibro(Usuario usuario, Libro libro) throws ServiceException {
        try {
            usuario.getLibros().add(libro);
            usuarioRepositorio.save(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServiceException("No se ha podido agregar el libro");
        }
    }

    
    private void validar(String nombre, String email, String contrasenia) throws ServiceException {
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

        if (usuarioRepositorio.buscarPorEmail(email) != null) {
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
    //BUSQUEDAS
    
    public Usuario buscarPorId(String id) {
        return usuarioRepositorio.getById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    public List<Materia> listarMateriasUsuario(Usuario usuario) {
        return usuario.getMaterias();
    }

    public List<Libro> getLibrosMateria(Usuario usuario, String materia) {
        ArrayList<Libro> libros = new ArrayList<>();
        for (int i = 0; i < usuario.getLibros().size(); i++) {
            Libro aux = usuario.getLibros().get(i);
            if (Objects.equals(aux.getMateria(), materia) && !aux.getLeido() && aux.getAlta()) {
                libros.add(aux);
            }
        }
        return libros;
    }

    public boolean materiaYaExistente(Materia materia, Usuario usuario) {
        for (Materia aux : usuario.getMaterias()) {
            if (aux.getNombre().equalsIgnoreCase(materia.getNombre())) {
                return true;
            }
        }
        return false;
    }

    public boolean materiaYaExistenteYActiva(Materia materia, Usuario usuario) {
        for (Materia aux : usuario.getMaterias()) {
            if (aux.getNombre().equalsIgnoreCase(materia.getNombre()) && aux.getAlta() == true) {
                return true;
            }
        }
        return false;
    }

    public List<Materia> getAllMaterias(Usuario usuario) {
        return usuario.getMaterias();
    }

    public List<Libro> getAllLibros(Usuario usuario) {
        return usuario.getLibros();
    }

    public Usuario verificarUsuarioId(String id) throws ServiceException {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new ServiceException("El usuario indicado no se encuentra en el sistema");
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO"));
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            User user = new User(usuario.getEmail(), usuario.getContrasenia(), authorities);
            return user;

        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("El usuario solicitado no existe ");
        }
    }

}
