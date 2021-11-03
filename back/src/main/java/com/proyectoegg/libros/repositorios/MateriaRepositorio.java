package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Materia;
//import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, String> {

    @Query("SELECT m FROM Materia m WHERE m.nombre = :nombre")
    public Materia buscarPorNombre(@Param("nombre") String nombre);
    
//    @Query("SELECT m FROM Materia m WHERE m.id_usuario = :idUsuario")
//    public List<Materia> buscarPorUsuario(@Param("id_usuario") String idUsuario);
    
}

