package org.chanochoca.springcloud.msvc.cursos.repositories;

import org.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CursoRepository extends ReactiveCrudRepository<Curso, Long> {

    Mono<Void> deleteCursoUsuarioByUsuarioId(Long id);
}
