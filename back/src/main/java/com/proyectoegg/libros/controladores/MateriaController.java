package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materias")
public class MateriaController {

    MateriaServicio materiaServicio;
    UsuarioServicio usuarioServicio;
    LibroServicio libroServicio;

    @Autowired
    public MateriaController(MateriaServicio materiaServicio, UsuarioServicio usuarioServicio, LibroServicio libroServicio) {
        this.materiaServicio = materiaServicio;
        this.usuarioServicio = usuarioServicio;
        this.libroServicio = libroServicio;
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("")
    public String mostrarMaterias(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("materias",  materiaServicio.getMateriaByUsuarioAlta(usuario));
        return "inicio";
    }

    // ====================== AGREGAR MATERIA =============================

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/agregar")
    public String agregarMateria(ModelMap model) {
        model.addAttribute("materia", new Materia());
        return "materias";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/agregar")
    public String agregarMateria(ModelMap model, @Valid Materia materia, BindingResult result,  HttpSession session) {

        if(result.hasErrors()){
            return "agregar-materia";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        materia.setUsuario(usuario);

        Materia m = materiaServicio.getMateriaByUsuario(usuario, materia.getNombre());

        if(m == null){
            materiaServicio.agregarMateria(materia);
        } else if (m.getAlta()) {
            model.addAttribute("error", "La materia ya se encuentra registrada");
            return "redirect:/materia/agregar";
        } else {
            materiaServicio.cambiarAlta(m);
            materia.setAlta(true);
        }

        return "redirect:/materia";
    }

    // ====================== EDITAR MATERIA =============================

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar/{id}")
    public String editarMateria(@PathVariable("id") String id, ModelMap model) {
        try {
            model.addAttribute(materiaServicio.encontrarPorID(id));
            return "materia-editar";
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/materia";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar")
    public String editarMateria(ModelMap model, @Valid Materia materia, BindingResult result) {

        if(result.hasErrors()){
            return "agregar-materia";
        }

        try {
            materiaServicio.editar(materia.getId(), materia.getNombre());
            return "redirect:/materia";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materia-editar";
        }
    }

    // ====================== ELIMINAR MATERIA =============================

    /*      Si no hay libros asociados a la materia -> Eliminar materia DB
        Si hay libros asociados a la materia, dar de baja la materia
        y dar de baja aquellos que no han sido leidos */

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/{id}")
    public String eliminarMateriaDefinitivamente(ModelMap model, @PathVariable String id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        try {
            Materia materia = materiaServicio.encontrarPorID(id);

            if(libroServicio.getLibrosByMateria(usuario, materia).isEmpty()){
                materiaServicio.eliminar(id);
            } else{
                materiaServicio.darMateriaBaja(materia);
                libroServicio.darBajaLibrosPorMateriaYUsuario(usuario, materia);
            }
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/materia";
    }


}
