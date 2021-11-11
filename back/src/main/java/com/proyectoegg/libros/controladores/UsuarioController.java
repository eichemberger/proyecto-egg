package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    MateriaServicio materiaServicio;

    @GetMapping("/registro")
    public String registrarUsuario(ModelMap model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap model, @ModelAttribute("usuario") Usuario usuario, MultipartFile archivo) {
        try {
            usuarioServicio.guardar(usuario, archivo);
            return "redirect:/";
        } catch (ServiceException | IOException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "registro";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarUsuario(ModelMap model, HttpSession session) throws ServiceException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuarioServicio.verificarUsuarioId(usuario.getId()) != null) {
            model.addAttribute("usuario", usuario);
            return "registro";
        } else {
            model.addAttribute("error", "El usuario no existe");
            return "registro";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarUsuario(ModelMap model, HttpSession session, @ModelAttribute("usuario") Usuario usuario) throws ServiceException, IOException {
    
        if (usuarioServicio.verificarUsuarioId(usuario.getId()) != null) {
            usuarioServicio.editar(usuario);
            return "inicio";
        } else {
            model.addAttribute("error", "El usuario no existe");
            return "inicio";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, @ModelAttribute("usuario") Usuario usuario, HttpSession session) {
        usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        return "perfil";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/perfil/{id}")
    public String perfil(ModelMap modelo, @PathVariable String id) {
        modelo.put("usuario", usuarioServicio.encontrarPorID(id));
        return "perfil";
    }

}
