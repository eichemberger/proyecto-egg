package com.proyectoegg.libros.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
public class Libro implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String autor;
    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(iso=ISO.DATE)
    @NotNull
    private Date fechaLimite;
    @NotNull
    @Min(1)
    @Max(365)
    private Integer diasAnticipacion;
    @Size(max = 250)
    private String descripcion;

    private Boolean alta;
    private Boolean leido;
    private Boolean obligatorio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Libro() {
    }

    public Libro(String id, String titulo, String autor, Materia materia, Boolean leido, Boolean obligatorio, Date fechaLimite, Integer diasAnticipacion, String descripcion, String idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.materia = materia;
        this.leido = leido;
        this.obligatorio = obligatorio;
        this.fechaLimite = fechaLimite;
        this.diasAnticipacion = diasAnticipacion;
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
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

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
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

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", materia=" + materia + ", leido=" + leido + ", obligatorio=" + obligatorio + ", fechaLimite=" + fechaLimite + ", diasAnticipacion=" + diasAnticipacion + ", descripcion=" + descripcion + ", idUsuario=" + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Libro libro = (Libro) o;

        return id.equals(libro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
