package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, String> {

   @Query("SELECT m FROM Materia m WHERE m.nombre = :nombre")
    Materia buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT m FROM Materia m WHERE m.usuario = :usuario")
    List<Materia> buscarPorUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT m FROM Materia m WHERE m.usuario = :usuario AND m.alta=1")
    List<Materia> buscarActivasPorUsuario(@Param("usuario") Usuario usuario);

    
    @Modifying
    @Query("DELETE FROM Materia m WHERE m.id =:id AND m.usuario =:usuario")
     void eliminar(@Param("id") String id, @Param("usuario") Usuario usuario);

    @Modifying
    @Query(value = "DELETE FROM Materia m WHERE m.id= :id")
    void eliminarPorId(@Param("id") String id);
}
