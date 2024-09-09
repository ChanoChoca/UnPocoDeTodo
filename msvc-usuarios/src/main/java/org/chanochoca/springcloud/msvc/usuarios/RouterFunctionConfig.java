package org.chanochoca.springcloud.msvc.usuarios;

import org.chanochoca.springcloud.msvc.usuarios.handler.UsuarioHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(UsuarioHandler handler) {

        return route(GET("/usuarios/crash"), handler::crash)
                .andRoute(GET("/usuarios"), handler::listar)
                .andRoute(GET("/usuarios/{id}"), handler::detalle)
                .andRoute(GET("/usuarios/usuarios-por-curso"), handler::obtenerUsuariosPorCurso)
                .andRoute(POST("/usuarios"), handler::crear)
                .andRoute(PUT("/usuarios/{id}"), handler::editar)
                .andRoute(DELETE("/usuarios/{id}"), handler::eliminar);
    }
}
