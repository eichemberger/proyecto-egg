/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyectoegg.libros.controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Massarelli
 */
@Controller

public class ErrorsController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String mostrarPaginaDeError(Model model, HttpServletRequest httpServletRequest) {
        
        
        String mensajeError = "";
        
        int codigoError = (int) httpServletRequest.getAttribute("javax.servlet.error.status_code");


        switch (codigoError) {
            case 400: {
                mensajeError = "El recurso solicitado no existe";
                break;
            }
            case 403: {
                mensajeError = "No tiene permisos para acceder al recurso";
                break;
            }
            case 401: {
                mensajeError = "No se encuentra autorizado";
                break;
            }
            case 404: {
                mensajeError = "El recurso solicitado no fue encontrado";
                break;
            }
            case 500: {
                mensajeError = "Ocurrio un error interno";
                break;
            }
        }
        
        
        model.addAttribute("codigo", codigoError);
        model.addAttribute("mensaje", mensajeError);
        return "error";
    }
    
       
}
