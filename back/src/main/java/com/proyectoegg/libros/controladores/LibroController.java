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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // MUESTRA TODOS LOS LIBROS
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping(value = {"/", ""})
    public String listaLibros(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Libro> libros = libroServicio.getAllLibrosAlta(usuario);
        libroServicio.setearDiasRestantes(libros);
        model.addAttribute("libros", libros);
        return "mostrar-libros";
    }

    // LIBROS LEIDOS
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/leidos")
    public String listaLeidos(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Libro> libros = libroServicio.getLibrosLeidos(usuario);
        libroServicio.setearDiasRestantes(libros);
        model.addAttribute("libros", libros);
        return "mostrar-libros-leidos";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/porLeer")
    public String listaPorLeer(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Libro> libros = libroServicio.getLibrosPorLeer(usuario);
        libroServicio.setearDiasRestantes(libros);
        model.addAttribute("libros", libros);
        return "mostrar-libros-sinLeer";
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
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/{materia}")
    public String libroPorMateria(@PathVariable("materia") String materiaId, ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Materia materiaAux = null;
        try {
            materiaAux = materiaServicio.encontrarPorID(materiaId);
        } catch (ServiceException ex) {
            model.addAttribute("error", "La materia no se encuentra en el sistema");
            System.out.println(ex.getMessage());
        }
        try {
            Materia materia = materiaServicio.getMateriaByUsuarioAndNombre(usuario, materiaAux.getNombre());
            model.addAttribute("libros", libroServicio.getLibrosByMateriaSinLeer(usuario, materia));
            model.addAttribute("materia", materiaAux.getNombre());
            return "mostrar-libros-materia";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/materia";
        }
    }

    // ====================== AGREGAR LIBRO =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias", materiaServicio.getMateriaByUsuarioAlta(usuario));
        model.addAttribute("libro", new Libro());
        model.addAttribute("error");
        return "agregar-libro";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarLibro(Libro libro, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        libro.setUsuario(usuario);

        try {
            libroServicio.agregarLibro(libro);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("materias", materiaServicio.getMateriaByUsuarioAlta(usuario));
            return "agregar-libro";
        }

      return "redirect:/libros/" + libro.getMateria().getId();
    }

    // ====================== EDITAR LIBRO =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar/{id}")
    public String editarLibro(@PathVariable("id") String id, ModelMap model, HttpSession session) {

        try {
            Libro libro = libroServicio.getLibro(id);
            if (getLibrosUsuario(session).contains(libro)) {
                model.addAttribute("libro", libro);
            }
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "inicio";
        }

        return "editar-libro";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar/{id}")
    public String editarlibro(@PathVariable("id") String id, @ModelAttribute("libro") Libro libro, BindingResult result, HttpSession session, ModelMap model) {

        if (result.hasErrors()) {
            return "redirect:/libro/editar/" + id;
        }

        if (getLibrosUsuario(session).contains(libro)) {
            try {
                libroServicio.editarLibro(libro, id);
            } catch (ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "redirect:/materia";
    }

    // ====================== ELIMINAR LIBRO =============================
    // Da de baja el libro
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") String id, ModelMap model, @RequestParam(value = "redireccion", required = false) String redireccion) {

        try {
            Libro libro = libroServicio.verificarLibroId(id);
            libroServicio.cambiarAlta(libro);

           if(redireccion!=null){
                return "redirect:/libros";
            }

             return "redirect:/libros/" + libro.getMateria().getId();
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/materia";
        }

    }

    // Elimina de base de datos
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/definitivo/{id}")
    public String eliminarDefinitivo(@PathVariable String id, ModelMap model) {
        try {
            libroServicio.eliminarDefinitivo(id);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/libros/papelera";
    }

    // ====================== RECICLAR LEIDO =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/reciclar/{id}")
    public String reciclar(@PathVariable String id, ModelMap model) {

        try {
            Libro libro = libroServicio.getLibro(id);

            if ((libro.getFechaLimite().before(new Date())) || (libro.getFechaLimite().equals(new Date()))) {
                model.addAttribute("error", "La fecha debe ser en el futuro");
                model.addAttribute("libro", libro);

                return "redirect:/libros/editar/" + libro.getId();
            } else {
                libroServicio.cambiarAlta(libro);

                if (!libro.getMateria().getAlta()) {
                    materiaServicio.cambiarAlta(libro.getMateria());
                }
            }

        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/libros/papelera";
    }

    // ====================== MARCAR LIBRO COMO LEIDO =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/{materia}/leido/{id}")
    public String marcarLeido(@PathVariable("materia") String materia,
             @PathVariable("id") String id,@RequestParam(value = "redireccion", required = false) String redireccion,
            ModelMap model) {

        Libro libro = null;
        try {
              libro = libroServicio.verificarLibroId(id);

            libroServicio.cambiarLeido(libro);

        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }

      if(redireccion!=null){
                return "redirect:/libros";
            }
        return "redirect:/libros/" + libro.getMateria().getId();

    }

    // ====================== MARCAR LIBRO COMO NO LEIDO =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/{materia}/noleido/{id}")
    public String marcarNoLeido(@PathVariable("materia") String materia,
            @PathVariable("id") String id,
            @RequestParam(value = "redireccion", required = false) String redireccion,
            ModelMap model) {
        try {
            Libro libro = libroServicio.verificarLibroId(id);

            libroServicio.cambiarNoLeido(libro);

        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());

            return "index";
        }

        if (redireccion != null) {
            return "redirect:/libros/porLeer";
        }

        return "redirect:/libros/" + materia;
    }

    // ====================== MOSTRAR LIBRO  =============================
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/ver/{id}")
    public String marcarLeido(@PathVariable("id") String id, ModelMap model) {
        try {
            Libro libro = libroServicio.verificarLibroId(id);
            model.addAttribute("libro", libro);
            model.addAttribute("diasRestantes", libroServicio.diasRestantesLimite(libro));
            return "mostrar-libro";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "index";
        }
    }

    @ModelAttribute("libros")
    public List<Libro> getLibrosUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        return libroServicio.getLibrosUsuario(usuario);
    }

    @ModelAttribute("usuario")
    public Usuario getUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuariosession");
    }

}
