package com.proyectoegg.libros.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Materia implements Serializable {
@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
private String id; 
private String nombre; 
private String idUsuario; 

    public Materia() {
    }

    public Materia(String id, String nombre, String idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Materia{" + "id=" + id + ", nombre=" + nombre + ", usuario=" + idUsuario + '}';
    }

}
