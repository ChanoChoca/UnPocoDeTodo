package com.chanochoca.springcloud.msvc.usuarios.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CursoClienteRest {

    @Value("${ubication}/cursos")
    private String ubication;

    private final WebClient webClient;

    @Autowired
    public CursoClienteRest(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ubication).build();
    }

    public Mono<Void> eliminarCursoUsuarioPorId(Long id) {
        return webClient.delete()
                .uri("/eliminar-curso-usuario/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}