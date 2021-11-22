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
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull
    private Date fechaLimite;
    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaAlerta;
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

      public Libro(String id, String titulo, String autor, Date fechaLimite, Date fechaAlerta, Integer diasAnticipacion, String descripcion, Boolean alta, Boolean leido, Boolean obligatorio, Materia materia, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaLimite = fechaLimite;
        this.fechaAlerta = fechaAlerta;
        this.diasAnticipacion = diasAnticipacion;
        this.descripcion = descripcion;
        this.alta = alta;
        this.leido = leido;
        this.obligatorio = obligatorio;
        this.materia = materia;
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

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Date getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(Date fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
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

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
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

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", fechaLimite=" + fechaLimite + ", fechaAlerta=" + fechaAlerta + ", diasAnticipacion=" + diasAnticipacion + ", descripcion=" + descripcion + ", alta=" + alta + ", leido=" + leido + ", obligatorio=" + obligatorio + ", materia=" + materia + ", usuario=" + usuario + '}';
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Libro libro = (Libro) o;

        return id.equals(libro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
