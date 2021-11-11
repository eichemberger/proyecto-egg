package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.util.List;
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
    @GetMapping(value = {"/", ""})
    public String listaLibros(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", usuarioServicio.getAllLibros(usuario));
        System.out.println(usuario.getLibros());
        return "mostrar-libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias", materiaServicio.listarActivasPorUsuario(usuario));
        model.addAttribute("libro", new Libro());
        return "agregar-libro";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute("libro") Libro libro, ModelMap model, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            model.addAttribute("materias", materiaServicio.listarActivasPorUsuario(usuario));
            libro.setUsuario(usuario);
            libro.setAlta(true);
            libro.setLeido(false);
            libroServicio.agregarLibro(libro);
            usuarioServicio.agregarLibro(usuario, libro);
            return "redirect:/libros";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "agregar-libro";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar/{libro.id}")
    public String cambiarLeido(ModelMap model, @PathVariable("libro.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Libro libro = libroServicio.buscarPorId(id);
            usuarioServicio.cambiarLeido(usuario, libro);
            System.out.println(libro.getLeido());
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
        }
        return "redirect:/libros";
    }

//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
//    @GetMapping("/editar/{id}")
//    public String editarLibro(@PathVariable("id") String id, ModelMap model, HttpSession session) {
//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        Libro libro = libroServicio.buscarPorId(id);
//        if (usuario.getLibros().contains(libro)) {
//            model.addAttribute("libro", libro);
//        } else {
//            return "index";
//        }
//        return "editar-libro";
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
//    @PostMapping("/editar/{id}")
//    public String editarlibro(@PathVariable("id") String id, @ModelAttribute("libro") Libro libro, HttpSession session) {
//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        Libro t = libroServicio.buscarPorId(id);
//        if (usuario.getLibros().contains(t)) {
//            try {
//                libroServicio.editarLibro(libro, id);
//                System.out.println(id);
//            } catch (ServiceException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        return "redirect:/materias";
//    }
        
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/basurero/eliminar/{libro.id}")
    public String eliminarLibro(ModelMap model, @PathVariable("libro.id") String id, HttpSession session) {
        try {
            libroServicio.eliminar(id);
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            usuarioServicio.actualizarLibros(usuario);
        } catch (Exception e) {
            model.addAttribute(e.getMessage());
        }
        return "redirect:/materias";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{libro.id}")
    public String cambiarAlta(ModelMap model, @PathVariable("libro.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Libro libro = libroServicio.buscarPorId(id);
            libroServicio.cambiarAlta(libro);
            usuarioServicio.actualizarLibros(usuario);
            System.out.println(libro.getLeido());
        } catch (Exception e) {
            model.addAttribute(e.getMessage());
        }
        return "redirect:/materias";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        libroServicio.eliminarSS(id);
        return "redirect:/materias";
    }

    @ModelAttribute("libros")
    public List<Libro> getLibrosUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        return libroServicio.buscarPorUsuarioId(usuario);
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/leidos")
    public String listaLeidos() {
        return "leidos";
    }

    @GetMapping("/{materia}")
    public String libroPorMateria(@PathVariable("materia") String materia, ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", usuarioServicio.getLibrosMateria(usuario, materia));
        return "mostrar-libros";
    }

}
