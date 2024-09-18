package org.chanochoca.springcloud.msvc.usuarios.handler;

import org.chanochoca.springcloud.msvc.usuarios.entity.Usuario;
import org.chanochoca.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UsuarioHandler {

    private final UsuarioService usuarioService;
    private final Environment env;

    @Autowired
    public UsuarioHandler(UsuarioService usuarioService, Environment env) {
        this.usuarioService = usuarioService;
        this.env = env;
    }

    public Mono<ServerResponse> crash(ServerRequest request) {
        return Mono.defer(() -> {
            // Simulate application crash by closing the context
            Mono<ServerResponse> response = ServerResponse.status(500).bodyValue("Application Crashed");
            ((ConfigurableApplicationContext) ((ApplicationContext) env).getParent()).close();
            return response;
        });
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return usuarioService.listar()
                .collectList()
                .flatMap(users -> {
                    Map<String, Object> body = new HashMap<>();
                    body.put("users", users);
                    body.put("podInfo", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
                    body.put("texto", env.getProperty("config.texto"));
                    return ServerResponse.ok().bodyValue(body);
                });
    }

    public Mono<ServerResponse> detalle(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return usuarioService.porId(id)
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> obtenerUsuariosPorCurso(ServerRequest request) {
        List<Long> ids = request.queryParam("ids")
                .map(idsStr -> List.of(idsStr.split(",")).stream()
                        .map(Long::parseLong)
                        .toList())
                .orElse(Collections.emptyList());

        return usuarioService.listarPorIds(ids)
                .collectList()
                .flatMap(users -> ServerResponse.ok().bodyValue(users))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(Usuario.class)
                .flatMap(usuario -> usuarioService.existePorEmail(usuario.getEmail())
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return ServerResponse.badRequest()
                                        .bodyValue(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
                            } else {
                                return usuarioService.guardar(usuario)
                                        .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                                                .bodyValue(savedUser));
                            }
                        }));
    }

    public Mono<ServerResponse> editar(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return usuarioService.porId(id)
                .flatMap(usuarioDb -> request.bodyToMono(Usuario.class)
                        .flatMap(usuario -> {
                            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())) {
                                return usuarioService.existePorEmail(usuario.getEmail())
                                        .flatMap(existe -> {
                                            if (Boolean.TRUE.equals(existe)) {
                                                return ServerResponse.badRequest()
                                                        .bodyValue(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
                                            } else {
                                                usuarioDb.setNombre(usuario.getNombre());
                                                usuarioDb.setEmail(usuario.getEmail());
                                                usuarioDb.setPassword(usuario.getPassword());
                                                return usuarioService.guardar(usuarioDb)
                                                        .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                                                                .bodyValue(savedUser));
                                            }
                                        });
                            } else {
                                usuarioDb.setNombre(usuario.getNombre());
                                usuarioDb.setEmail(usuario.getEmail());
                                usuarioDb.setPassword(usuario.getPassword());
                                return usuarioService.guardar(usuarioDb)
                                        .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                                                .bodyValue(savedUser));
                            }
                        }))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return usuarioService.porId(id)
                .flatMap(usuario -> usuarioService.eliminar(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> validar(Errors errors) {
        Map<String, String> errores = new HashMap<>();
        errors.getFieldErrors().forEach(err ->
                errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
        return ServerResponse.badRequest().bodyValue(errores);
    }
}
