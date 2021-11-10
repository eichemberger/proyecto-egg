package com.proyectoegg.libros.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Materia implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private Boolean alta;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Materia() {
    }

    public Materia(String id, String nombre, Boolean alta, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.alta = alta;
        this.usuario = usuario;
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

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Materia{" + "id=" + id + ", nombre=" + nombre + ", alta=" + alta + ", usuario=" + usuario + '}';
    }    
}
