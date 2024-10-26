package com.chanochoca.springcloud.msvc.cursos.clients;

import com.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UsuarioClientRest {

    @Value("${ubication}/usuarios")
    private static String ubication;

    private final WebClient webClient;

    @Autowired
    public UsuarioClientRest(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ubication).build();
    }

    public Mono<Usuario> detalle(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Usuario.class)
                .doOnNext(usuario -> {
                    if (usuario.getId() == null) {
                        System.out.println("Error: El usuario no tiene un id válido.");
                    } else {
                        System.out.println("Usuario obtenido: " + usuario.getId());
                    }
                })
                .doOnError(e -> System.out.println("Error obteniendo usuario: " + e.getMessage()));
    }

    public Mono<Usuario> crear(Usuario usuario) {
        System.out.println("Creando usuario: " + usuario.getEmail() + " " + usuario.getNombre() + " " + usuario.getPassword());
        return webClient.post()
                .uri("")
                .bodyValue(usuario)
                .retrieve()
                .bodyToMono(Usuario.class);
    }

    public Flux<Usuario> obtenerAlumnosPorCurso(Iterable<Long> ids) {
        // Convertir Iterable<Long> a una cadena de texto separada por comas
        String idsParam = StreamSupport.stream(ids.spliterator(), false)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/usuarios-por-curso")
                        .queryParam("ids", idsParam)
                        .build())
                .retrieve()
                .bodyToFlux(Usuario.class);
    }

}
