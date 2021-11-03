package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.leido = 1")
public ArrayList<Libro> listaLeidos();

    @Query("SELECT l FROM Libro l WHERE l.leido = 0")
public ArrayList<Libro> listaNoLeidos();



    
}
