package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, String> {

 Materia findByNombre(String nombre);

 List<Materia> findByUsuario(Usuario usuario);

 List<Materia> findByUsuarioAndAltaTrue(Usuario usuario);

 Materia findByUsuarioAndNombre(Usuario usuario, String nombre);

 @Modifying
 @Query(value = "DELETE FROM Materia m WHERE m.id= :id")
 void eliminarPorId(@Param("id") String id);


}

