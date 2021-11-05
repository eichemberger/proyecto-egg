package com.proyectoegg.libros.repositorios;

import com.proyectoegg.libros.entidades.Libro;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("DELETE l FROM Libro l WHERE l.id = :l.id")
    public ArrayList<Libro> borrarLibro(@Param("id") String id);

    @Query("DELETE u FROM usuario_libros u WHERE u.libros_id = :id")
    public void borrarRelacionLibro(@Param("id") String id);

    @Query("SELECT l FROM Libro l AND Usuario u WHERE l.leido=0 AND u.id = : u.id")
    public ArrayList<Libro> listaLibrosNoLeidos(@Param("u.id") String id);

    @Query("SELECT l FROM Libro l AND Usuario u WHERE l.leido=1 AND u.id = : u.id")
    public ArrayList<Libro> listaLibrosLeidos(@Param("u.id") String id);

    @Query("SELECT l FROM Libro l AND Usuario u WHERE u.id = :u.id ")
    public ArrayList<Libro> listaLibros(@Param("u.id ") String id);

    
    
}
