package com.chanochoca.springcloud.msvc.cursos.repositories;

import com.chanochoca.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CursoUsuarioRepository extends ReactiveCrudRepository<CursoUsuario, Long> {
    Mono<Void> deleteByUsuarioId(Long usuarioId);
    Mono<CursoUsuario> findByCursoIdAndUsuarioId(Long cursoId, Long usuarioId);
    Flux<CursoUsuario> findByCursoId(Long cursoId);
}
