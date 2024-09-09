package org.chanochoca.springcloud.msvc.cursos.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cursos_usuarios")
public class CursoUsuario {

    @Id
    private Long id;

    @Column("usuario_id")
    private Long usuarioId;

    public CursoUsuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { //Si la instancia es igual a la referencia
            return true;
        }
        if (!(obj instanceof CursoUsuario)) { //Si el objeto no es una instancia de CursoUsuario
            return false;
        }
        CursoUsuario o = (CursoUsuario) obj;
        return this.usuarioId != null && this.usuarioId.equals(o.usuarioId);
    }
}
