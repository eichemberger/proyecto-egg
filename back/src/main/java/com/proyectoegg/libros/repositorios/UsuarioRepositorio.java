package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

   @Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre")
   public Usuario buscarPorNombre(@Param("nombre") String nombre);
    
   @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Modifying
    @Query(value = "DELETE FROM Usuario u WHERE u.id= :id")
    void eliminarPorId(@Param("id") String id);
}
