package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.servicios.EmailService;
import com.proyectoegg.libros.servicios.LibroServicio;
import com.proyectoegg.libros.servicios.MateriaServicio;
import com.proyectoegg.libros.servicios.UsuarioServicio;
import java.io.IOException;

import com.proyectoegg.libros.validacion.UsuarioValidador;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    UsuarioServicio usuarioServicio;
    MateriaServicio materiaServicio;
    UsuarioValidador usuarioValidador;
    LibroServicio libroServicio;

    @Autowired
    public UsuarioController(UsuarioServicio usuarioServicio, MateriaServicio materiaServicio, UsuarioValidador usuarioValidador) {
        this.usuarioServicio = usuarioServicio;
        this.materiaServicio = materiaServicio;
        this.usuarioValidador = usuarioValidador;
        this.libroServicio = libroServicio;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(usuarioValidador);
    }

    @GetMapping("/registro")
    public String registrarUsuario(ModelMap model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap model, @Valid Usuario usuario, BindingResult result, MultipartFile archivo) {

        if(result.hasErrors()){
            System.out.println(result.getAllErrors());
            return "registro";
        }
       

        try {
            usuarioServicio.guardar(usuario, archivo);
        } catch (ServiceException e){
            model.addAttribute("error", e);
            return "registro";
        }

        return "redirect:/login";

    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") String id, ModelMap model, HttpSession session) throws ServiceException, IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuarioServicio.listarTodos().contains(usuario)) {
            model.addAttribute("error", "El usuario solicitado no existe");
            return "inicio";
        }
        return "editar-usuario";
    }


    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") String id, ModelMap model, @ModelAttribute("usuario") Usuario usuario) throws IOException {

        try {
            usuarioServicio.editar(usuario, id);
            return "redirect:/materias";
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "editar-usuario";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/perfil/{id}")
    public String perfil(ModelMap modelo, @PathVariable String id) {
        modelo.put("usuario", usuarioServicio.encontrarPorID(id));
        return "perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar/definitivo/{id}")
    public String eliminarDefinitivo(@PathVariable String id, ModelMap model, HttpSession session) throws ServiceException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        //Validación muy primitiva para tener que estar loggeado en la cuenta que vas a borrar
        System.out.println("id usuario sesion: " + usuario.getId());
        System.out.println("id que recibe: " + id);
        if (usuario.getId().equals(id)) {
            try {
                for (Libro libro : usuario.getLibros()) {
                    libroServicio.eliminarDefinitivo(libro.getId());
                }

            } catch (ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }
            try {
                for (Materia materia : usuario.getMaterias()) {
                    //Ver después el método de borrar materia
                    materiaServicio.eliminar(materia.getId());
                }

            } catch (ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }
            try {
                usuarioServicio.eliminarDefinitivo(id);
            } catch (ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }
            return "redirect:/";
        } else {
            throw new ServiceException("No se pudo eliminar el usuario");
        }
    }


}
