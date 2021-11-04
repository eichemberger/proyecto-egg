package com.proyectoegg.libros.controladores;

import com.proyectoegg.libros.servicios.MateriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/")
public class MainController {

    @Autowired
    MateriaServicio materiaServicio;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @PostMapping("/logincheck")
    public String logincheck() {
        return "index.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "index.html";
    }
}
