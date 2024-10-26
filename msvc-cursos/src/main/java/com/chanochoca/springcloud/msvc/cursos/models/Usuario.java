package com.chanochoca.springcloud.msvc.cursos.models;
//POJO Class, para poder obtener los datos dede el otro microservicio
//y poder representarlo como un objeto

public class Usuario {
    private Long id;

    private String nombre;

    private String email;

    private String password;

    public Usuario () {}

    public Usuario (Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
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
