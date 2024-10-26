package com.chanochoca.springcloud.msvc.cursos.handler;

import com.chanochoca.springcloud.msvc.cursos.models.Usuario;
import com.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import com.chanochoca.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

    private final CursoService cursoService;

    @Autowired
    public CursoHandler(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok().body(cursoService.listar(), Curso.class);
    }

    public Mono<ServerResponse> detalle(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return cursoService.porIdConUsuarios(id)
                .flatMap(curso -> ServerResponse.ok().bodyValue(curso))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(Curso.class)
                .flatMap(curso -> cursoService.guardar(curso))
                .flatMap(cursoDb -> ServerResponse.status(201).bodyValue(cursoDb))
                .onErrorResume(e ->
                        ServerResponse.badRequest().bodyValue("Error al crear curso: " + e.getMessage()));
    }

    public Mono<ServerResponse> editar(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(Curso.class)
                .flatMap(curso -> cursoService.porId(id)
                        .flatMap(existingCurso -> {
                            existingCurso.setNombre(curso.getNombre());
                            return cursoService.guardar(existingCurso);
                        })
                        .flatMap(updatedCurso -> ServerResponse.status(201).bodyValue(updatedCurso))
                        .switchIfEmpty(ServerResponse.notFound().build()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error al actualizar curso"));
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return cursoService.porId(id)
                .flatMap(curso -> cursoService.eliminar(curso.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> asignarUsuario(ServerRequest request) {
        Long cursoId = Long.valueOf(request.pathVariable("cursoId"));
        return request.bodyToMono(Usuario.class)
                .flatMap(usuario -> {
                    // Imprimir la información del usuario
                    System.out.println("Usuario recibido: " + usuario);
                    return cursoService.asignarUsuario(usuario, cursoId);
                })
                .flatMap(usuarioAsignado -> ServerResponse.status(201).bodyValue(usuarioAsignado))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error al asignar usuario"));
    }


    public Mono<ServerResponse> crearUsuario(ServerRequest request) {
        Long cursoId = Long.valueOf(request.pathVariable("cursoId"));
        return request.bodyToMono(Usuario.class)
                .flatMap(usuario -> cursoService.crearUsuario(usuario, cursoId))
                .flatMap(usuarioCreado -> ServerResponse.status(201).bodyValue(usuarioCreado))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error al crear usuario: " + e.getMessage()));
    }

    public Mono<ServerResponse> eliminarUsuario(ServerRequest request) {
        Long cursoId = Long.valueOf(request.pathVariable("cursoId"));
        return request.bodyToMono(Usuario.class)
                .flatMap(usuario -> cursoService.eliminarUsuario(usuario, cursoId))
                .flatMap(usuarioEliminado -> ServerResponse.ok().bodyValue(usuarioEliminado))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error al eliminar usuario"));
    }

    public Mono<ServerResponse> eliminarCursoUsuarioPorId(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return cursoService.eliminarCursoUsuarioPorId(id)
                .then(ServerResponse.noContent().build());
    }
}
