package org.chanochoca.springcloud.msvc.cursos.clients;

//import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@FeignClient(name = "msvc-usuarios")
//public interface UsuarioClientRest {
//
//    @GetMapping("/{id}")
//    Mono<Usuario> detalle(@PathVariable Long id);
//
//    @PostMapping("/")
//    Mono<Usuario> crear(@RequestBody Usuario usuario);
//
//    @GetMapping("/usuarios-por-curso")
//    Flux<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
//}

import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioClientRest {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public UsuarioClientRest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private static final String BASE_URL = "http://msvc-usuarios";

    public Mono<Usuario> detalle(Long id) {
        return webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/{id}", id)
                .retrieve()
                .bodyToMono(Usuario.class);
    }

    public Mono<Usuario> crear(Usuario usuario) {
        return webClientBuilder.build()
                .post()
                .uri(BASE_URL + "/")
                .body(Mono.just(usuario), Usuario.class)
                .retrieve()
                .bodyToMono(Usuario.class);
    }

    public Flux<Usuario> obtenerAlumnosPorCurso(Iterable<Long> ids) {
        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/usuarios-por-curso")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToFlux(Usuario.class);
    }
}
