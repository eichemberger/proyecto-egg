package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    Usuario findByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM Usuario u WHERE u.id= :id")
    void eliminarPorId(@Param("id") String id);

}
