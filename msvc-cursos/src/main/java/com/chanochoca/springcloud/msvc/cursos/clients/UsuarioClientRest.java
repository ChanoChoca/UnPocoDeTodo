package com.chanochoca.springcloud.msvc.cursos.clients;

import com.chanochoca.springcloud.msvc.cursos.models.Usuario;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClientRest {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioClientRest.class);

    @Value("${ubication}")
    private String ubication;

    private WebClient webClient;

    public UsuarioClientRest() {
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://" + ubication + "/usuarios")
                .build();
    }

    public Mono<Usuario> detalle(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Usuario.class)
                .doOnNext(usuario -> {
                    if (usuario.getId() == null) {
                        logger.error("Error: El usuario no tiene un id válido.");
                    } else {
                        logger.info("Usuario obtenido: {}", usuario.getId());
                    }
                })
                .doOnError(e -> logger.error("Error obteniendo usuario: {}", e.getMessage()));
    }

    public Mono<Usuario> crear(Usuario usuario) {
        logger.info("Creando usuario: {} {} {}", usuario.getEmail(), usuario.getNombre(), usuario.getPassword());
        return webClient.post()
                .uri("")
                .bodyValue(usuario)
                .retrieve()
                .bodyToMono(Usuario.class);
    }

    public Flux<Usuario> obtenerAlumnosPorCurso(Flux<Long> ids) {
        return ids
                .map(String::valueOf) // Convertir cada Long a String
                .collectList() // Recopilación de los IDs en una lista
                .flatMapMany(idList -> {
                    // Convertir la lista de Long a una cadena de texto separada por comas
                    String idsParam = String.join(",", idList);

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder.path("/usuarios-por-curso")
                                    .queryParam("ids", idsParam)
                                    .build())
                            .retrieve()
                            .bodyToFlux(Usuario.class);
                });
    }
}
