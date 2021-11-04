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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIOLOGUEADO')")
    @GetMapping("/agregar")
    public String agregarLibro(ModelMap model) {
        model.addAttribute("libro", new Libro());
        return "agregarLibroForm";
    }
    

    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute("libro") Libro libro, ModelMap model) {
        try {
            System.out.println(libro.getTitulo());
                     //recuperar ID usuario para mandarlo
           model.addAttribute("materias", materiaServicio.encontrarPorID("f55ac1e5-afd5-4864-b31e-0940326c4cf3"));
            libroServicio.agregarLibro(libro, "f55ac1e5-afd5-4864-b31e-0940326c4cf3");
            return "redirect:/";
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            model.addAttribute(e.getMessage());
            return "agregarLibroForm";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIOLOGUEADO')")
    @GetMapping("/editar")
    public String editarlibro(@ModelAttribute("libro") Libro libro, ModelMap model, @RequestParam Usuario usuario) {
         try {
//            libroServicio.editarLibro(libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getMateria(), libro.getObligatorio(), libro.getFechaLimite(), libro.getDiasAnticipacion(), libro.getDescripcion(), libro.getIdUsuario());
        } catch (Exception e) {
            model.addAttribute(e.getMessage());
        }
        return "agregarLibroForm";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIOLOGUEADO')")
    @GetMapping("/listaLeidos")
    public String listaLeidos() {
        return "libros-leidos";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIOLOGUEADO')")
    @GetMapping("/listarLibros")
    public String listaLibros() {
        return "tabla-libros";
    }

}
