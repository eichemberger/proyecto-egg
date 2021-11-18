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
import java.util.Date;
import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

 List<Libro> findByUsuario(Usuario usuario);

 List<Libro> findByUsuarioAndMateria(Usuario usuario, Materia materia);

 List<Libro> findByUsuarioAndAltaTrue(Usuario usuario);

 List<Libro> findByUsuarioAndMateriaAndLeidoFalseAndAltaTrue(Usuario usuario, Materia materia);

 List<Libro> findByUsuarioAndAltaFalse(Usuario usuario);

 List<Libro> findByUsuarioAndLeidoTrueAndAltaTrue(Usuario usuario);

 List<Libro> findByFechaLimiteAndAltaTrueAndLeidoFalse(Date fechaLimite);
 
 List<Libro> findByFechaAlertaAndAltaTrueAndLeidoFalse(Date fechaAlerta);
 
 @Modifying
 @Query(value = "DELETE FROM Libro l WHERE l.id= :id")
 void eliminarPorId(@Param("id") String id);

}
