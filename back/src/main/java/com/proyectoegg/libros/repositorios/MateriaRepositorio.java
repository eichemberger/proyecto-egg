package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Materia;
import com.proyectoegg.libros.entidades.Usuario;
import java.util.List;
//import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia, String> {

    @Query("SELECT m FROM Materia m WHERE m.nombre = :nombre")
    public Materia buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT m FROM Materia m WHERE m.alta = 1")
    public List<Materia> buscarActivas();
   
    
//    @Query("SELECT m FROM Materia m WHERE m.usuario_id =:usuario_id")
//    public List<Materia> buscarPorUsuario(String usuario_id);
    
    
//    @Query(value="Select m from Materia m where m.id_usuario = :idUsuario")
//    List<Materia> buscarPorUsuario(@Param("id_usuario") String idUsuario);
    
//    @Query("SELECT m FROM Materia m WHERE m.id_usuario = :idUsuario")
//    public List<Materia> listarPorUsuario(@Param("id_usuario") String idUsuario);
    
//    @Query ("DELETE m FROM usuario_materias m WHERE materias_id =:id AND usuario_id =:idUsuario")
//    public Materia eliminar(@Param("materias_id") String id, @Param("usuario_id") String idUsuario);
    
    
//    @Query("DELETE FROM 'libreria_recordatorios'.'usuario_materias' WHERE 'materias_id' =:id")
//    public Materia eliminar(@Param("materias_id") String id);
    
}
