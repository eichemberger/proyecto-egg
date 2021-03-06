package com.proyectoegg.libros.entidades;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NotBlank
    private String nombre;
    @NotEmpty
    @Email(message = "Correo con formato equivocado")
    private String email;
    private String contrasenia;
    private Boolean alta;


    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Libro> libros;
    //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "usuario")
    private List<Materia> materias;

    @OneToOne
    private Foto foto;

    public Usuario() {
        this.materias = new ArrayList<>();
    }

    public Usuario(String id, String nombre, String email, String contrasenia, Boolean alta, List<Libro> libros, List<Materia> materias, Foto foto) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.alta = alta;
        this.libros = libros;
        this.materias = materias;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", email=" + email + ", contrasenia=" + contrasenia + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
