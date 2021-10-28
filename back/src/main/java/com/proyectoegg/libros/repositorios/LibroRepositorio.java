package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {


    
}
