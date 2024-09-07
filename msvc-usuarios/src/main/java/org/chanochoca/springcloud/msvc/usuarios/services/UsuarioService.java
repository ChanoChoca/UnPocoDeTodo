package org.chanochoca.springcloud.msvc.usuarios.services;

import org.chanochoca.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
//    Iterable<T> findAllById(Iterable<ID> ids);
    List<Usuario> listarPorIds(Iterable<Long> ids);

    Optional<Usuario> porEmail(String email);
    boolean existePorEmail(String email);
}
