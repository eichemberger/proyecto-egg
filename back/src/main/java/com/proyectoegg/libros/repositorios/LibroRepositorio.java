package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
//import java.util.ArrayList;
import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    // *
    @Query(value="SELECT l FROM Libro l WHERE l.usuario = :usuario")
    ArrayList<Libro> buscarPorUsuarioId(@Param("usuario") Usuario usuario);

    @Query(value="SELECT l FROM Libro l WHERE l.materia = :materia AND l.usuario = :usuario AND l.leido = false AND l.alta = true")
    ArrayList<Libro> libroPorMateriaSinLeer(@Param("usuario") Usuario usuario, @Param("materia")String materia);

    @Query("SELECT l FROM Libro l WHERE l.leido = true AND l.usuario = :usuario AND l.alta = true")
    ArrayList<Libro> getLibrosLeidos(@Param("usuario") Usuario usuario);

    @Query("SELECT l FROM Libro l WHERE l.alta = false AND l.usuario = :usuario")
    ArrayList<Libro> getLibrosEliminados(@Param("usuario") Usuario usuario);

    @Modifying
    @Query(value = "DELETE FROM Libro l WHERE l.id= :id")
    void eliminarPorId(@Param("id") String id);

}
