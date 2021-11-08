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
import org.springframework.web.bind.annotation.PathVariable;
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

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias", usuario.getMaterias());
        model.addAttribute("libro", new Libro());

        return "agregarLibroForm";
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute("libro") Libro libro, ModelMap model, HttpSession session) {
        try {
            libroServicio.agregarLibro(libro);
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            model.addAttribute("materias", usuario.getMaterias());
            usuarioServicio.agregarLibro(usuario.getId(), libro.getId());
            return "redirect:/";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute(e.getMessage());
            return "agregarLibroForm";
        }
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @GetMapping("/editar/{libro.id}")
    public String editarLibro(@PathVariable("materia.id") String id, HttpSession session) {
        return "libro-editar";
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @PostMapping("/editar{libro.id}")
    public String editarLibro(@PathVariable("materia.id") String id, HttpSession session,
            ModelMap model, @ModelAttribute("libro") Libro libro) {
        try {
            libroServicio.editarLibro(libro);
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "libro-editar";
        }
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @PostMapping("/eliminar{libro.id}")
    public String eliminarLibro(ModelMap model, @ModelAttribute("libro") Libro libro) {
        try {
            libroServicio.eliminar(libro.getId());
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
        }
        return "libro-eliminar";
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @GetMapping("/leido")
    public String cambiarLeido() {
        return "cambiar-leido";
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @PostMapping("/leido")
    public String cambiarLeido(ModelMap model, @ModelAttribute("libro") Libro libro) {
        try {
            libroServicio.cambiarLeido(libro.getId());
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
        }
        return "cambiar-leido";
    }

    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @GetMapping("/listaLeidos")
    public String listaLeidos(HttpSession session, ModelMap model) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("librosleidos", libroServicio.listaLibrosLeidos(usuario));

        return "libros-leidos";
    }
    
    @PreAuthorize("hasAuthority('USUARIO_REGISTRADO')")
    @GetMapping("/listarLibros")
    public String listaLibros(HttpSession session, ModelMap model) {
         Usuario usuario = (Usuario) session.getAttribute("usuariosession");
         model.addAttribute("libros", libroServicio.listaLibrosNoLeidos(usuario));
        return "tabla-libros";
    }

}
