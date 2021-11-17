package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.io.IOException;
import com.proyectoegg.libros.validacion.UsuarioValidador;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    UsuarioServicio usuarioServicio;
    MateriaServicio materiaServicio;
    UsuarioValidador usuarioValidador;
    LibroServicio libroServicio;

    @Autowired
    public UsuarioController(UsuarioServicio usuarioServicio, MateriaServicio materiaServicio, UsuarioValidador usuarioValidador, LibroServicio libroServicio) {
        this.usuarioServicio = usuarioServicio;
        this.materiaServicio = materiaServicio;
        this.usuarioValidador = usuarioValidador;
        this.libroServicio = libroServicio;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(usuarioValidador);
    }

    @GetMapping("/registro")
    public String registrarUsuario(ModelMap model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap model, @Valid Usuario usuario, BindingResult result, MultipartFile archivo) {

        if(result.hasErrors()){
            return "registro";
        }

        try {
            usuarioServicio.guardar(usuario, archivo);
        } catch (ServiceException e){
            model.addAttribute("error", e);
            return "registro";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "redirect:/login";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarUsuario(ModelMap model, HttpSession session) throws ServiceException, IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuarioServicio.listarTodos().contains(usuario)) {
            model.addAttribute("usuario", usuario);
            return "editar-usuario";
        } else {
            throw new ServiceException("No se encontró el usuario");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarUsuario(ModelMap model, @ModelAttribute("usuario") Usuario usuario, HttpSession session, MultipartFile archivo) throws IOException {
        Usuario usuarioId = (Usuario) session.getAttribute("usuariosession");
        System.out.println("Id: " + usuarioId.getId());
        try {
            usuarioServicio.editar(usuario, usuarioId.getId(), archivo);
            return "redirect:/usuario/perfil";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "editar-usuario";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("HOLAAAAAA");
            return "editar-usuario";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/cambiarContrasenia")
    public String cambiarPassword() throws ServiceException {
        return "cambiar-contrasenia";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/cambiarContrasenia")
    public String cambiarPassword(ModelMap model, HttpSession session, @RequestParam String contraseniaVieja,@RequestParam String contraseniaNueva ) throws ServiceException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        System.out.println(contraseniaNueva);
        System.out.println(contraseniaVieja);
        if (usuarioServicio.verificarContrasenia(usuario, contraseniaVieja)) {
            usuarioServicio.cambiarContrasenia(usuario.getId(), contraseniaNueva);
            return "redirect:/usuario/perfil";
        } else {
            System.out.println("ERROR");
            model.addAttribute("error", "La contraseña actual no es correcta");
           return "cambiar-contrasenia";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuarioServicio.encontrarPorID(usuario.getId()));
        
        List<Libro> leidos = libroServicio.getLibrosLeidos(usuario);
        modelo.put("cantidadLeidos", leidos.size());
        
        return "perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/definitivo")
    public String eliminarDefinitivo(ModelMap model, HttpSession session) throws ServiceException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            for (Libro libro : usuario.getLibros()) {
                libroServicio.eliminarDefinitivo(libro.getId());
            }

        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        try {
            for (Materia materia : usuario.getMaterias()) {
                //Ver después el método de borrar materia
                materiaServicio.eliminar(materia.getId());
            }

        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        try {
            usuarioServicio.eliminarDefinitivo(usuario.getId());
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }
    

}
