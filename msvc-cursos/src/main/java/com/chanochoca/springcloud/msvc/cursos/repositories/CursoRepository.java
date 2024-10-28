package com.chanochoca.springcloud.msvc.cursos.repositories;

import com.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CursoRepository extends ReactiveCrudRepository<Curso, Long> { }
