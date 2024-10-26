package com.chanochoca.springcloud.msvc.cursos;

import com.chanochoca.springcloud.msvc.cursos.handler.CursoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(CursoHandler handler) {

        return route(GET("cursos"), handler::listar)
                .andRoute(GET("cursos/{id}"), handler::detalle)
                .andRoute(POST("cursos"), handler::crear)
                .andRoute(PUT("cursos/{id}"), handler::editar)
                .andRoute(DELETE("cursos/{id}"), handler::eliminar)
                .andRoute(PUT("cursos/asignar-usuario/{cursoId}"), handler::asignarUsuario)
                .andRoute(POST("cursos/crear-usuario/{cursoId}"), handler::crearUsuario)
                .andRoute(DELETE("cursos/eliminar-usuario/{cursoId}"), handler::eliminarUsuario)
                .andRoute(DELETE("cursos/eliminar-curso-usuario/{id}"), handler::eliminarCursoUsuarioPorId);
    }
}
