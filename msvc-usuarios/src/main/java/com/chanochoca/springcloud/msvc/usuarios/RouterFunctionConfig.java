package com.chanochoca.springcloud.msvc.usuarios;

import com.chanochoca.springcloud.msvc.usuarios.handler.UsuarioHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(UsuarioHandler handler) {

        return route(GET("/usuarios/usuarios-por-curso"), handler::obtenerUsuariosPorCurso)
                .andRoute(GET("/usuarios/crash"), handler::crash)
                .andRoute(GET("/usuarios"), handler::listar)
                .andRoute(GET("/usuarios/{id}"), handler::detalle)
                .andRoute(POST("/usuarios"), handler::crear)
                .andRoute(PUT("/usuarios/{id}"), handler::editar)
                .andRoute(DELETE("/usuarios/{id}"), handler::eliminar);
    }
}
