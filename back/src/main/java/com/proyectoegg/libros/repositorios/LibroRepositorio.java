package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
import com.proyectoegg.libros.entidades.Usuario;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query(value = "SELECT l FROM Libro l WHERE l.usuario = :usuario")
    ArrayList<Libro> buscarPorUsuario(@Param("usuario") Usuario usuario);

    @Query(value = "SELECT l FROM Libro l WHERE l.materia = :materia AND l.usuario = :usuario ")
    ArrayList<Libro> libroPorMateria(@Param("usuario") Usuario usuario, @Param("materia") String materia);

    @Modifying
    @Query(value = "DELETE FROM Libro l WHERE l.id= :id")
    void eliminarPorId(@Param("id") String id);

}
