package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Foto;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardar(MultipartFile archivo) throws IOException, ServiceException {
        try {
            if (archivo != null) {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } else {
                throw new ServiceException("El archivo ingresado no pudo ser procesado.");
            }
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Transactional
    public Foto editar(String idFoto, MultipartFile archivo) throws ServiceException, Exception {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                if (idFoto != null) {
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }
                } else {
                    throw new ServiceException("La foto no pudo ser procesada.");
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        } else {
            throw new ServiceException("El archivo ingresado no pudo ser procesado.");
        }
    }
}
