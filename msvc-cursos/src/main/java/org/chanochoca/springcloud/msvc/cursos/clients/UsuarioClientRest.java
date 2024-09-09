package org.chanochoca.springcloud.msvc.cursos.clients;

import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FeignClient(name = "msvc-usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Mono<Usuario> detalle(@PathVariable Long id);

    @PostMapping("/")
    Mono<Usuario> crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    Flux<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
