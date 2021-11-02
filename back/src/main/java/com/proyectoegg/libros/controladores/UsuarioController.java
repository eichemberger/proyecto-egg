package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/registro")
    public String registrarUsuario() {
        return "registroForm";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap model, @RequestParam String nombre, @RequestParam String email, @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam MultipartFile archivo) {
        try {
            
            usuarioServicio.guardar(nombre, email, contrasenia, contrasenia2, archivo);
            return "redirect: /index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registroForm";
        }
    }
    
    @GetMapping("/editar")
    public String editarUsuario(){
       return "editar-usuario"; 
    }
    
    @GetMapping("/login")
    public String iniciarSesion(){
        return "iniciar-sesion";
    }
    
    
}
