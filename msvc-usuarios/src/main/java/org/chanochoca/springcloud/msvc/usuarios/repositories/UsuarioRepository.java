package org.chanochoca.springcloud.msvc.usuarios.repositories;

import org.chanochoca.springcloud.msvc.usuarios.entity.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

//no es necesario utilizar @Repostory o @Component, porque
//ya es un objeto componente Spring
public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {

    Mono<Usuario> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
}
