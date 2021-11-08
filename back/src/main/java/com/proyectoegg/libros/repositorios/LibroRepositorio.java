package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
//import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    
 
//      @Query("DELET ")
//    @Query("SELECT l FROM Libro l WHERE l.idUsuario = :idUsuario")
//    public ArrayList<Libro> listaLibros(@Param("idUsuario") String idUsuario);
//
//    @Query("SELECT l FROM Libro l WHERE l.leido = 1 AND l.idUsuario = :idUsuario")
//    public ArrayList<Libro> listaLeidos(@Param("idUsuario") String idUsuario);
//
//    @Query("SELECT l FROM Libro l WHERE l.leido = 0 AND l.idUsuario = :idUsuario")
//    public ArrayList<Libro> listaNoLeidos(@Param("idUsuario") String idUsuario);

}
