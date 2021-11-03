package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.MateriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/agregar")
    public String agregarMateria(ModelMap model) {
        model.addAttribute("materia", new Materia());
        return "materias.html";
    }

    @PostMapping("/agregar")
    public String agregarMateria(ModelMap model, @ModelAttribute("materia") Materia materia) {
        try {
            //recuperar ID usuario para mandarlo
            materiaServicio.agregarMateria(materia.getNombre(), "f55ac1e5-afd5-4864-b31e-0940326c4cf3");
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materias";
        }
    }

    @GetMapping("/editar")
    public String editarMateria() {
        return "materia-editar";
    }

    @PostMapping("/editar")
    public String editarMateria(ModelMap model, @ModelAttribute("materia") Materia materia) {
        try {
            materiaServicio.editar(materia.getId(), materia.getNombre(), materia.getIdUsuario());
            return "redirect:/usuario/inicio";
        } catch (ServiceException e) {
            model.addAttribute(e.getMessage());
            return "materia-editar";
        }
    }

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
