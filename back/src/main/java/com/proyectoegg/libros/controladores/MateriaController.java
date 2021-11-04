package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    MateriaServicio materiaServicio;

    UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarMateria(ModelMap model) {
        model.addAttribute("materia", new Materia());
        return "materias.html";
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarMateria(ModelMap model, @ModelAttribute("materia") Materia materia, HttpSession session) {
        try {
            materiaServicio.agregarMateria(materia);
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            usuarioServicio.agregarMateria(usuario.getId(), materia.getId());
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materias";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "materias";
        }
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarMateria() {
        return "materia-editar";
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarMateria(ModelMap model, @ModelAttribute("materia") Materia materia) {
        try {
            materiaServicio.editar(materia.getId(), materia.getNombre());
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materia-editar";
        }
    }

    @PreAuthorize("hasAnyRole('USUARIO_REGISTRADO')")
    @GetMapping("/eliminar")
    public String eliminarMateria(ModelMap model, @RequestParam String id) {
        try {
            materiaServicio.eliminar(id);
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "inicio";
        }
    }

}