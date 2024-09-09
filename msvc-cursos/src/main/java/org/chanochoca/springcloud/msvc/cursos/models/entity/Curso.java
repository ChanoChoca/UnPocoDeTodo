package org.chanochoca.springcloud.msvc.cursos.models.entity;

import jakarta.validation.constraints.NotEmpty;
import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name="cursos")
public class Curso {

    @Id
    private Long id;

    @NotEmpty
    private String nombre;

    @MappedCollection(idColumn = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    //Atributo no persistente
    //Transient indica que el atributo no está mapeado a la persistencia
    //Lo utilizaremos para poblar todos los datos de usuarios
    @Transient
    private List<Usuario> usuarios;

    public Curso() {
        this.cursoUsuarios = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addCursoUsuario(CursoUsuario cursoUsuario) {
        this.cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario) {
        this.cursoUsuarios.remove(cursoUsuario);
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
