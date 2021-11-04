package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.servicios.MateriaServicio;
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

    @PostMapping("/logincheck")
    public String logincheck() {
        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        return "index";
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/info")
    public String info() {
        return "sobre-nosotros.html";
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicioWeb() {
        return "inicio";
    }

}
