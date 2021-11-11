package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    MateriaServicio materiaServicio;
    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String email, @RequestParam(required = false) String logout, @RequestParam(required = false) String nombre) {
        if (error != null) {
            model.addAttribute("error", "El email o la contraseña ingresados son incorrectos");
        }
        if (email != null) {
            model.addAttribute("email", email);
        }
        if (nombre != null) {
            model.addAttribute("nombre", nombre);
        }
        if (logout != null) {
            model.addAttribute("logout", "Ha salido correctamente del sitio");
        }
        return "iniciar-sesion";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/logincheck")
    public String logincheck() {
        return "index";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/logout")
    public String logout() {
        return "index";
    }

    @GetMapping("/info")
    public String info() {
        try {
            return "sobre-nosotros.html";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
