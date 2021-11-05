package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    LibroServicio libroServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    MateriaServicio materiaServicio;

//    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias", usuario.getMaterias());
        model.addAttribute("libro", new Libro());

        return "agregarLibroForm";
    }

    //@PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute("libro") Libro libro, ModelMap model, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            libroServicio.agregarLibro(libro);
            usuarioServicio.agregarLibro(usuario.getId(), libro.getId());

            
            model.addAttribute("materias", usuario.getMaterias());
            
            return "redirect:/";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute(e.getMessage());
            return "agregarLibroForm";
        }
    }
    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarlibro(@ModelAttribute("libro") Libro libro, ModelMap model, HttpSession session) {
        try {
            libroServicio.editarLibro(libro);
        } catch (Exception e) {
            model.addAttribute(e.getMessage());
        }
        return "agregarLibroForm";
    }


    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/listaLeidos")
    public String listaLeidos() {
        return "libros-leidos";
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/listarLibros")
    public String listaLibros() {
        return "tabla-libros";
    }

}
