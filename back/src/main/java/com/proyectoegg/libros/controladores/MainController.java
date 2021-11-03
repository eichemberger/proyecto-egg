package com.proyectoegg.libros.controladores;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/")
public class MainController {

    @GetMapping("")
    public String index(){
        return "index";
    }
    
    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String nombre, @RequestParam(required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "El usuario ingresado o la contraseña son incorrectos");
        }
        if (nombre != null) {
            model.addAttribute("nombre", nombre);
        }
        return "iniciar-sesion";
    }

}
