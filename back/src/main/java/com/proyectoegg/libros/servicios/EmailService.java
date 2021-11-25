package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LibroServicio libroServicio;

//    @Async
//    public void enviar(String cuerpo, String titulo, String mail) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mail);
//        message.setFrom("proyectoegg159@gmail.com");
//        message.setSubject(titulo);
//        message.setText(cuerpo);
//
//        mailSender.send(message);
//    }
    public void avisoLibro(Usuario usuario, ArrayList<Libro> libros) {
        String processedHTMLTemplate = this.avisoLibroHTML(usuario, libros);
        MimeMessagePreparator preparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            helper.setFrom("proyectoegg159@gmail.com");
            helper.setTo(usuario.getEmail());
            helper.setSubject("Aviso de libros por leer");
            helper.setText(processedHTMLTemplate, true);
        };
        mailSender.send(preparator);
    }

    private String avisoLibroHTML(Usuario usuario, ArrayList<Libro> libros) {
        Context context = new Context();
        context.setVariable("libros", libros);
        context.setVariable("usuario", usuario);
        return templateEngine.process("mail-alerta", context);
    }

    @Scheduled(cron = "00 32 22 * * *")
    private void enviarAvisosUsuario() {
        if (libroServicio.librosFechaAlertaActivosSinLeer().isEmpty()) {
            return;
        }

        Map<Usuario, ArrayList<Libro>> map = new HashMap<>();
        for (Libro libro : libroServicio.librosFechaAlertaActivosSinLeer()) {
            if (map.containsKey(libro.getUsuario())) {
                map.get(libro.getUsuario()).add(libro);
            } else {
                map.put(libro.getUsuario(), new ArrayList<>(Collections.singletonList(libro)));
            }
        }
        map.forEach((k, v) -> avisoLibro(k, v));
    }

    
    
    public void avisoRegistro(Usuario usuario) {
        String processedHTMLTemplate = this.avisoRegistroHTML();
        MimeMessagePreparator preparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            helper.setFrom("proyectoegg159@gmail.com");
            helper.setTo(usuario.getEmail());
            helper.setSubject("Bienvenid@ a Book O'clock");
            helper.setText(processedHTMLTemplate, true);
        };
        mailSender.send(preparator);
    }

    private String avisoRegistroHTML() {
        Context context = new Context();
        return templateEngine.process("mail-registro", context);
    }
    
    
    
            //ENVIA 1 MAIL X C/ LIBRO    
//        @Scheduled(cron = "00 12 22 * * *")
//    private void enviarAvisosEmail() {
//        System.out.println(new Date());
//        List<Libro> enviarAlerta = libroServicio.librosFechaAlertaActivosSinLeer();
//        for (Libro libro : enviarAlerta) {
//            avisoLibro(libro);
//        }
//    }
    
    //    @Autowired
//    private SimpleMailMessage preConfiguredMessage;
//    public void sendPreConfiguredMail(String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
//        mailMessage.setText(message);
//        mailSender.send(mailMessage);
//    }
}
