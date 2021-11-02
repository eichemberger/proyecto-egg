package com.proyectoegg.libros.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Libro implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String titulo;
    private String autor;
    private String materia;
    private Boolean leido;
    private Boolean obligatorio;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaLimite;
    private Integer diasAnticipacion;
    private String descripcion;
    @ManyToOne
    private Usuario usuario; 

    public Libro() {
    }

    public Libro(String id, String titulo, String autor, String materia, Boolean leido, Boolean obligatorio, Date fechaLimite, Integer diasAnticipacion, String descripcion, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.materia = materia;
        this.leido = leido;
        this.obligatorio = obligatorio;
        this.fechaLimite = fechaLimite;
        this.diasAnticipacion = diasAnticipacion;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Integer getDiasAnticipacion() {
        return diasAnticipacion;
    }

    public void setDiasAnticipacion(Integer diasAnticipacion) {
        this.diasAnticipacion = diasAnticipacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", materia=" + materia + ", leido=" + leido + ", obligatorio=" + obligatorio + ", fechaLimite=" + fechaLimite + ", diasAnticipacion=" + diasAnticipacion + ", descripcion=" + descripcion + '}';
    }

    
}
