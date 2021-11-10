package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/")
    public String listaLibros(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", usuario.getLibros());
        System.out.println(usuario.getLibros());
        return "mostrar-libros";}
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias", usuario.getMaterias());
        model.addAttribute("libro", new Libro());

        return "agregar-libro";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute("libro") Libro libro, ModelMap model, HttpSession session) {
        try {
            libro.setLeido(false);
            libro.setAlta(true);
            libroServicio.agregarLibro(libro);
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            model.addAttribute("materias", usuario.getMaterias());
            usuarioServicio.agregarLibro(usuario, libro);
            return "redirect:/";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute(e.getMessage());
            return "agregar-libro";
        }
    }

//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
//    @GetMapping("/editar/{libro.id}")
//    public String editarLibro(@PathVariable("libro.id") String id, HttpSession session) {
//        return "agregar-libro";
//    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar/{libro.id}")
    public String editarLibro(@PathVariable("libro.id") String id, HttpSession session,
            ModelMap model) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            libroServicio.editarLibro(id);
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "agregar-libro";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{libro.id}")
    public String eliminarLibro(ModelMap model, @PathVariable("libro.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Libro libro = libroServicio.buscarPorId(id);
//            usuarioServicio.eliminarLibro(libro, usuario);
//            libroServicio.eliminar(libro);
        usuarioServicio.darDeBajaLibro(usuario, libro);
//        libroServicio.darDeBaja(libro);
            return "redirect:/";
        } catch (Exception e) {

            model.addAttribute(e.getMessage());
        }
        return "mostrar-libros";
    }
    

    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar/{libro.id}")
    public String cambiarLeido(ModelMap model,  @PathVariable("libro.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Libro libro = libroServicio.buscarPorId(id);
            usuarioServicio.cambiarLeido(usuario, libro);
            System.out.println(libro.getLeido());
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
        }
        return "redirect:/inicio";
    }

//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
//    @PostMapping("/leido{libro.id}")
//    public String cambiarLeido(ModelMap model, @ModelAttribute("libro") Libro libro) {
//        try {
//            libroServicio.cambiarLeido(libro.getId());
//        } catch (ServiceException e) {
//            model.addAttribute(e.getMessage());
//        }
//        return "cambiar-leido";
//    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/leidos")
    public String listaLeidos(HttpSession session, ModelMap model, @ModelAttribute("materia") Materia materia) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("librosleidos", libroServicio.listaLibrosLeidos(usuario, materia.getNombre()));

        return "libros-leidos";
    }
    
    

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/{materia}")
    public String listaLibros(HttpSession session, ModelMap model,@PathVariable("materia") String materia) {
         Usuario usuario = (Usuario) session.getAttribute("usuariosession");
         model.addAttribute("libros", libroServicio.listaLibrosNoLeidos(usuario, materia));
        return "mostrar-libros";
    }

}
