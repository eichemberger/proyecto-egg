package com.proyectoegg.libros.servicios;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("emailService")
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviar(String cuerpo, String titulo, String mail){
        SimpleMailMessage message  = new SimpleMailMessage();
        message.setTo(mail);
        message.setFrom("proyectoegg159@gmail.com");
        message.setSubject(titulo);
        message.setText(cuerpo);

        mailSender.send(message);
    }

    //    @Autowired
//    private SimpleMailMessage preConfiguredMessage;

//    public void sendPreConfiguredMail(String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
//        mailMessage.setText(message);
//        mailSender.send(mailMessage);
//    }
}