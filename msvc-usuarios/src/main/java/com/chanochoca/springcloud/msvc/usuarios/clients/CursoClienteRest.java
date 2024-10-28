package com.chanochoca.springcloud.msvc.usuarios.clients;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CursoClienteRest {

    @Value("${ubication}")
    private String ubication;

    private WebClient webClient;

    public CursoClienteRest() {
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://" + ubication + "/cursos")
                .build();
    }

    public Mono<Void> eliminarCursoUsuarioPorId(Long id) {
        return webClient.delete()
                .uri("/eliminar-curso-usuario/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
