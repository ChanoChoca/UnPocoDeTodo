package org.chanochoca.springcloud.msvc.cursos.repositories;

import org.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CursoRepository extends ReactiveCrudRepository<Curso, Long> { }
