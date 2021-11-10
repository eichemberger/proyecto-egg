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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    MateriaServicio materiaServicio;
    @Autowired
    UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarMateria(ModelMap model) {
        model.addAttribute("materia", new Materia());
        return "materias.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarMateria(ModelMap model, @ModelAttribute("materia") Materia materia, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            materia.setAlta(true);
            materiaServicio.agregarMateria(materia, usuario);
            usuarioServicio.agregarMateria(usuario, materia);
            return "redirect:/inicio";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "materias";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "materias";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar")
    public String editarMateria() {
        return "materia-editar";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
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

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{materia.id}")
    public String eliminarMateria(ModelMap model, @PathVariable("materia.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Materia materia = materiaServicio.encontrarPorID(id);
            System.out.println("materia en controlador: "+ materia);
//            usuarioServicio.eliminarMateria(usuario, materia);
//            usuarioServicio.eliminarMateria(usuario.getId(), materia.getId());
            usuarioServicio.darDeBajaMateria(usuario, materia);
            materiaServicio.darDeBaja(materia);
            materiaServicio.eliminar(id);
            return "redirect:/inicio";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/inicio";
        }
    }

}
