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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {

    LibroServicio libroServicio;
    UsuarioServicio usuarioServicio;
    MateriaServicio materiaServicio;

    @Autowired
    public LibroController(LibroServicio libroServicio, UsuarioServicio usuarioServicio, MateriaServicio materiaServicio) {
        this.libroServicio = libroServicio;
        this.usuarioServicio = usuarioServicio;
        this.materiaServicio = materiaServicio;
    }

    // MUESTRA TODOS LOS LIBROS *
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping(value={"/", ""})
    public String listaLibros() {
        return "mostrar-libros";
    }

    // LIBROS LEIDOS
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/leidos")
    public String listaLeidos(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", libroServicio.getLibrosLeidos(usuario));
        return "leidos";
    }

    // LIBROS ELIMINADOS
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/papelera")
    public String papeleraLibros(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", libroServicio.getLibrosEliminados(usuario));
        return "papelera-libros";
    }

    // LIBROS SEGUN MATERIA (Alta = True && Leido = False)
    @GetMapping("/{materia}")
    public String libroPorMateria(@PathVariable("materia") String materia, ModelMap model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("libros", libroServicio.getLibrosMateriaNoLeidos(usuario, materia));
        model.addAttribute("materia", materia);
        return "mostrar-libros";
    }

    // ====================== AGREGAR LIBRO =============================

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
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            libro.setUsuario(usuario);
            libroServicio.agregarLibro(libro);
            return "redirect:/libros/" + libro.getMateria();
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "agregar-libro";
        }
    }

    // ====================== EDITAR LIBRO =============================

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar/{id}")
    public String editarLibro(@PathVariable("id") String id, ModelMap model, HttpSession session) {
        Libro libro = libroServicio.buscarPorId(id);
        if(getLibrosUsuario(session).contains(libro)){
            model.addAttribute("libro", libro);
        } else {
            model.addAttribute("error", "El libro solicitado no existe");
            return "inicio";
        }
        return "editar-libro";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar/{id}")
    public String editarlibro(@PathVariable("id") String id, @ModelAttribute("libro") Libro libro, HttpSession session, ModelMap model) {
        Libro t = libroServicio.buscarPorId(id);
        if(getLibrosUsuario(session).contains(libro)){
            try {
                libroServicio.editarLibro(libro, id);
            } catch (ServiceException e){
                model.addAttribute("error", e.getMessage());
            }
        }
        return "redirect:/materia";
    }

// ====================== ELIMINAR LIBRO =============================

    // Da de baja el libro
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap model){
        try {
            libroServicio.cambiarAlta(id);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/libros/" + libroServicio.buscarPorId(id).getMateria();
    }

    // Elimina de base de datos
    @GetMapping("/eliminar/definitivo/{id}")
    public String eliminarDefinitivo(@PathVariable String id, ModelMap model){
        try {
            libroServicio.eliminarDefinitivo(id);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/libros/papelera";
    }

// ====================== MARCAR LIBRO COMO LEIDO =============================

    @GetMapping("/{materia}/leido/{id}")
    public String marcarLeido(@PathVariable String materia, @PathVariable String id, ModelMap model){
        try {
            libroServicio.cambiarLeido(id);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/libros/" + materia;
    }

    @ModelAttribute("libros")
    public List<Libro> getLibrosUsuario(HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        return libroServicio.buscarPorUsuarioId(usuario);
    }

}
