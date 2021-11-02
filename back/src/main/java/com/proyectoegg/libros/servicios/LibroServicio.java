package com.proyectoegg.libros.servicios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import com.proyectoegg.libros.excepciones.ServiceException;
import com.proyectoegg.libros.repositorios.LibroRepositorio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    private String[] materiasLista = {"Biología", "Geografía", "Historia", "Lengua", "Ciencias Sociales", "Ciencias Naturales", "Matemáticas", "Física", "Química"};
    private ArrayList<String> listaMateria = new ArrayList<>(Arrays.asList(materiasLista));

    
    //Fijarse lo de setearle el libro al usuario por id
    @Transactional
    public Libro agregarLibro(String titulo, String autor, String materia, Boolean obligatorio, Date fechaLimite, Integer diasAnticipacion, String descripcion) throws ServiceException {
        validar(titulo, materia, fechaLimite, diasAnticipacion, descripcion);
        Libro libro = new Libro();

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setDescripcion(descripcion);
        libro.setFechaLimite(fechaLimite);
        libro.setDiasAnticipacion(diasAnticipacion);
        libro.setLeido(false);
        libro.setMateria(materia);
        libro.setObligatorio(obligatorio);
        
        

        return libroRepositorio.save(libro);
    }

    public void validar(String titulo, String materia, Date fechaLimite, Integer diasAnticipacion, String descripcion) throws ServiceException {
        if (titulo == null || titulo.isEmpty() || titulo.equals(" ")) {
            throw new ServiceException("Debe escribir un título");
        }
        if ((fechaLimite.before(new Date())) || (fechaLimite.equals(new Date()))) {
            throw new ServiceException("Debe seleccionar una fecha posterior a la actual");
        }
        //Esto después vemos bien
        if (diasAnticipacion < 1) {
            throw new ServiceException("La cantidad de días debe ser mayor uno");
        }
        if (descripcion.length() > 250) {
            throw new ServiceException("La descripción debe tener un máximo de 250 caracteres");
        }
        if (materia.equals("  ") || materia == null || materia.isEmpty()) {
            throw new ServiceException("Debe ingresar el nombre de la materia a agregar");
        }
    }

//    public List<String> listaMaterias() {
//        
//        String[] materiasLista = {"Biología","Geografía", "Historia", "Lengua", "Ciencias Sociales", "Ciencias Naturales", "Matemáticas", "Física", "Química"};
//        ArrayList<String> listaMateria = new ArrayList<>(Arrays.asList(materiasLista));
//        return listaMateria;
//    }
    
    
    //Esto capaz modifica la global
    public List<String> agregarMateria(ArrayList<String> listaMateria, String materia) throws ServiceException {
        validarMateria(listaMateria, materia);
        listaMateria.add(materia);

        return listaMateria;
    }

    public void validarMateria(ArrayList<String> listaMateria, String materia) throws ServiceException {

        if (materia.equals("  ") || materia == null || materia.isEmpty()) {
            throw new ServiceException("Debe ingresar el nombre de la materia a agregar");
        }
        for (String nombre : listaMateria) {
            if (nombre.equals(materia)) {
                throw new ServiceException("Esa materia ya existe");
            }

        }
    }
}
