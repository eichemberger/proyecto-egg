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
@RequestMapping("/materias")
public class MateriaController {

    @Autowired
    MateriaServicio materiaServicio;
    @Autowired
    UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("")
    public String mostrarMaterias(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("usuario", usuario);
        model.addAttribute("materias",  materiaServicio.listarActivasPorUsuario(usuario));
        return "inicio";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarMateria(ModelMap model) {
        model.addAttribute("materia", new Materia());
        return "materias";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarMateria(ModelMap model, @ModelAttribute("materia") Materia materia, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            if (usuarioServicio.materiaYaExistenteYActiva(materia, usuario)) {
                System.out.println("Error La materia que intenta agregar ya existe en el usuario");
                model.addAttribute("error", "La materia que intenta agregar ya se encuentra en el usuario");
                return "materias";
            } else {
                materia.setAlta(true);
                materia.setUsuario(usuario);
                materiaServicio.agregarMateria(materia);
                usuarioServicio.agregarMateria(usuario, materia);
                return "redirect:/materias";
            }
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
    public String editarMateria(ModelMap model, @RequestParam("materia.id") String idMateria) {
        model.addAttribute("materia", materiaServicio.encontrarPorID(idMateria)); 
        return "materia-editar";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarMateria(ModelMap model, @ModelAttribute("materia") Materia materia) {
        try {
            materiaServicio.editar(materia);
            return "redirect:/materias";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materia-editar";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{materia.id}")
    public String darDeBajaMateria(ModelMap model, @PathVariable("materia.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Materia materia = materiaServicio.encontrarPorID(id);
            if (materiaServicio.materiaConLibrosSinLeer(materia, usuario)) {
                System.out.println("No se puede eliminar una materia con libros sin leer.");
                model.addAttribute("error", "No se puede eliminar una materia con libros sin leer");
                return "redirect:/materias";
            } else {
                materiaServicio.darDeBaja(materia);
                usuarioServicio.guardarMateriasUsuario(usuario);
                return "redirect:/materias";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/materias";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/validarEliminar/{materia.id}")
    public String validarMateria(ModelMap model, @PathVariable("materia.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Materia materia = materiaServicio.encontrarPorID(id);
            if (materiaServicio.materiaConLibrosSinLeer(materia, usuario)) {
                System.out.println("Esta intentando eliminar una materia que tiene libros asociados sin leer.");
                model.addAttribute("alerta", "Esta intentando eliminar una materia que tiene libros asociados sin leer");
//                return "redirect:/inicio";

                //COMO ATRAPAR LA CONFIRMACION? 
                return "/eliminarDefinitivamente/{materia.id}";
            } else {
                return "redirect:/materias";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/materias";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminarDefinitivamente/{materia.id}")
    public String eliminarMateria(ModelMap model, @PathVariable("materia.id") String id, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            Materia materia = materiaServicio.encontrarPorID(id);
            materiaServicio.eliminarBD(usuario, materia);
            usuarioServicio.guardarMateriasUsuario(usuario);
            return "redirect:/materias";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/materias";
        }
    }
}
