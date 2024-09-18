package org.chanochoca.springcloud.msvc.usuarios.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="usuarios")
public class Usuario {

    @Id
    private Long id;

    //@NotEmpty solo para String, en otros casos @NotNull
    @NotBlank
    private String nombre;

    @NotEmpty
    @Email
//    @Column(unique = true) //Clave subrogada
    @Column("email")
    private String email;

    @NotBlank
    private String password;

    public Usuario() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
