package com.chanochoca.springcloud.msvc.usuarios.services;

import com.chanochoca.springcloud.msvc.usuarios.entity.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioService {
    Flux<Usuario> listar();
    Mono<Usuario> porId(Long id);
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Void> eliminar(Long id);
//    Iterable<T> findAllById(Iterable<ID> ids);
    Flux<Usuario> listarPorIds(Iterable<Long> ids);

    Mono<Usuario> porEmail(String email);
    Mono<Boolean> existePorEmail(String email);
}
