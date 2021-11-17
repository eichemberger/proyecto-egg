package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.EmailService;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.io.IOException;

import com.proyectoegg.libros.validacion.UsuarioValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    UsuarioServicio usuarioServicio;
    MateriaServicio materiaServicio;
    UsuarioValidador usuarioValidador;

    @Autowired
    public UsuarioController(UsuarioServicio usuarioServicio, MateriaServicio materiaServicio, UsuarioValidador usuarioValidador) {
        this.usuarioServicio = usuarioServicio;
        this.materiaServicio = materiaServicio;
        this.usuarioValidador = usuarioValidador;
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
        }

        return "redirect:/login";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarUsuario(ModelMap model, HttpSession session) throws ServiceException, IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }



    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarUsuario(ModelMap model, @ModelAttribute("usuario") Usuario usuario, @ModelAttribute("nueva-contrasenia") String p1,  HttpSession session) throws IOException {
        Usuario usuarioId = (Usuario) session.getAttribute("usuariosession");
        System.out.println("Id: "+usuarioId.getId());
        System.out.println(p1);
        try {
            usuarioServicio.editar(usuario, usuarioId.getId(), p1);
            return "redirect:/materias";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "editar-usuario";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/password")
    public String cambiarPassword(ModelMap model) throws ServiceException {
        model.addAttribute("usuario", new Usuario());
        return "cambiar-contrasenia";
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/password")
    public String cambiarPassword(ModelMap model, HttpSession session, @RequestParam String contraseniaVieja,@RequestParam String contraseniaNueva ) throws ServiceException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        System.out.println(contraseniaNueva);
        System.out.println(contraseniaVieja);
        if (usuarioServicio.verificarContrasenia(usuario, contraseniaVieja)) {
            usuarioServicio.cambiarContrasenia(usuario.getId(), contraseniaNueva);
            return "perfil";
        } else {
            throw new ServiceException("La contraseña actual no es correcta");
        }
    }


}
