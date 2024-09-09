package org.chanochoca.springcloud.msvc.cursos.services;

import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CursoService {

    Flux<Curso> listar();
    Mono<Curso> porId(Long id);
    Mono<Curso> porIdConUsuarios(Long id);
    Mono<Curso> guardar(Curso curso);
    Mono<Void> eliminar(Long id);

    Mono<Void> eliminarCursoUsuarioPorId(Long id);

    Mono<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Mono<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Mono<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}

