package org.chanochoca.springcloud.msvc.usuarios.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CursoClienteRest {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public CursoClienteRest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private static final String BASE_URL = "http://msvc-cursos";

    public Mono<Void> eliminarCursoUsuarioPorId(Long id) {
        return webClientBuilder.build()
                .delete()
                .uri(BASE_URL + "/eliminar-curso-usuario/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
