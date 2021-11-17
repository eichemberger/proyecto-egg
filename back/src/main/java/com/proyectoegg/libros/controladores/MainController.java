package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.servicios.EmailService;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyRole;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    MateriaServicio materiaServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    private EmailService emailService;

    @ModelAttribute("usuario")
    public Usuario getUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        return usuario;
    }

    @GetMapping("")
    public String index(HttpSession session, Model model) {
        model.addAttribute("usuario", (Usuario) session.getAttribute("usuariosession"));
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
        return "login";
    }

//    @PostMapping("/logincheck")
//    public String logincheck() {
//        return "inicio";
//    }
    @GetMapping("/logout")
    public String logout() {
        return "index";
    }

    @GetMapping("/info")
    public String info() {
        return "sobre-nosotros.html";
    }

//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
//    @GetMapping("/inicio")
//    public String inicioWeb(ModelMap model, HttpSession session) {
//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        model.addAttribute("materias", usuarioServicio.getAllMaterias(usuario));
//        return "inicio";
//    }
    
    @PostMapping("/enviarmail")
    public String enviarMail(@RequestParam String destinatario, @RequestParam String asunto, @RequestParam String contenido) {
        emailService.enviar(destinatario, contenido, contenido);
        return "index";
    }
    
}
