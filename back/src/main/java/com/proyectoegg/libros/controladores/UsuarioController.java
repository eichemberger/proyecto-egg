package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return "registroForm";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap model, @ModelAttribute("usuario") Usuario usuario, MultipartFile archivo) {
        try {
            usuarioServicio.guardar(usuario, archivo);
            return "redirect:/";
        } catch (ServiceException | IOException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "registroForm";
        } catch (Exception ex) {
              System.out.println(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/editar")
    public String editarUsuario(ModelMap model, @ModelAttribute("usuario") Usuario usuario) {
        try {
            usuarioServicio.editar(usuario);
            return "inicio";
        } catch (ServiceException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "editar-usuario";
        }
    }

    @GetMapping("/inicio")
    public String inicio(ModelMap model) {
        List<Materia> materias = materiaServicio.listarTodas();
        model.addAttribute("materias", materias);
        return "inicio.html";
    }
    
    @GetMapping("/login")
    public String iniciarSesion() {
        return "iniciar-sesion";
    }

        @GetMapping("/logout")
    public String logout() {
        return "index";
    }
    
}
