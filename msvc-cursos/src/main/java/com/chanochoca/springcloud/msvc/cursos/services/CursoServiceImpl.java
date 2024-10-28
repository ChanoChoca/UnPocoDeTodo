package com.chanochoca.springcloud.msvc.cursos.services;

import com.chanochoca.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.chanochoca.springcloud.msvc.cursos.models.Usuario;
import com.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import com.chanochoca.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.chanochoca.springcloud.msvc.cursos.repositories.CursoRepository;
import com.chanochoca.springcloud.msvc.cursos.repositories.CursoUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class CursoServiceImpl implements CursoService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioClientRest.class);

    private final CursoRepository cursoRepository;
    private final CursoUsuarioRepository cursoUsuarioRepository;
    private final UsuarioClientRest client;

    public CursoServiceImpl(CursoRepository cursoRepository, CursoUsuarioRepository cursoUsuarioRepository, UsuarioClientRest client) {
        this.cursoRepository = cursoRepository;
        this.cursoUsuarioRepository = cursoUsuarioRepository;
        this.client = client;
    }

    @Override
    public Flux<Curso> listar() {
        // Obtener todos los cursos
        return cursoRepository.findAll()
                .flatMap(curso -> {
                    // Para cada curso, obtenemos sus CursoUsuarios
                    return cursoUsuarioRepository.findByCursoId(curso.getId())
                            .collectList()  // Convertimos a lista
                            .flatMapMany(cursoUsuarios -> {
                                // Agregamos cursoUsuarios a la entidad Curso
                                curso.setCursoUsuarios(cursoUsuarios);

                                // Ahora obtenemos los usuarios basados en los cursoUsuarios
                                Flux<Long> usuarioIds = Flux.fromStream(cursoUsuarios.stream()
                                        .map(CursoUsuario::getUsuarioId));

                                // Convertir usuarioIds a una lista para su uso posterior
                                return usuarioIds
                                        .collectList()
                                        .flatMapMany(ids -> {
                                            if (ids.isEmpty()) {
                                                curso.setUsuarios(Collections.emptyList());
                                                return Flux.just(curso);
                                            }

                                            // Llamar al cliente con la lista de IDs
                                            return client.obtenerAlumnosPorCurso(Flux.fromIterable(ids)) // Cambiado a Flux.fromIterable
                                                    .collectList()
                                                    .map(usuarios -> {
                                                        // Si no se encuentran usuarios, se asigna una lista vacía
                                                        curso.setUsuarios(usuarios.isEmpty() ? Collections.emptyList() : usuarios);
                                                        return curso;
                                                    })
                                                    .onErrorResume(error -> {
                                                        // En caso de error en la llamada al cliente, se asigna una lista vacía de usuarios
                                                        curso.setUsuarios(Collections.emptyList());
                                                        return Mono.just(curso);
                                                    });
                                        });
                            });
                });
    }



    @Override
    public Mono<Curso> porId(Long id) {
        // Obtener el curso por ID
        return cursoRepository.findById(id)
                .flatMap(curso -> {
                    // Obtener los CursoUsuarios relacionados con el curso
                    return cursoUsuarioRepository.findByCursoId(curso.getId())
                            .collectList()
                            .map(cursoUsuarios -> {
                                // Asignar los CursoUsuarios al curso
                                curso.setCursoUsuarios(cursoUsuarios);
                                return curso;
                            });
                });
    }

    @Override
    public Mono<Curso> porIdConUsuarios(Long id) {
        // Obtener el curso por id
        return cursoRepository.findById(id)
                .flatMap(curso -> {
                    // Obtener los CursoUsuarios relacionados
                    return cursoUsuarioRepository.findByCursoId(curso.getId())
                            .collectList()
                            .flatMap(cursoUsuarios -> {
                                // Asignar los CursoUsuarios al curso
                                curso.setCursoUsuarios(cursoUsuarios);

                                // Obtener los IDs de los usuarios a partir de CursoUsuarios
                                Flux<Long> usuarioIds = Flux.fromStream(cursoUsuarios.stream()
                                        .map(CursoUsuario::getUsuarioId));

                                // Convertir usuarioIds a una lista para su uso posterior
                                return usuarioIds
                                        .collectList()
                                        .flatMap(ids -> {
                                            // Obtener los usuarios por IDs de forma reactiva
                                            return client.obtenerAlumnosPorCurso(Flux.fromIterable(ids)) // Cambiado a Flux.fromIterable
                                                    .collectList()
                                                    .map(usuarios -> {
                                                        // Asignar los usuarios al curso
                                                        curso.setUsuarios(usuarios);
                                                        return curso;
                                                    })
                                                    .onErrorResume(error -> {
                                                        // En caso de error, se asigna una lista vacía de usuarios
                                                        curso.setUsuarios(Collections.emptyList());
                                                        return Mono.just(curso);
                                                    });
                                        });
                            });
                });
    }

    @Override
    public Mono<Curso> guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return cursoRepository.deleteById(id);
    }

    @Override
    public Mono<Void> eliminarCursoUsuarioPorId(Long id) {
        // Eliminar el CursoUsuario basado en el usuarioId
        return cursoUsuarioRepository.deleteByUsuarioId(id);
    }

    @Override
    public Mono<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.detalle(usuario.getId())
                        .flatMap(usuarioMsvc -> {
                            logger.info("Usuario obtenido: email = {}, id = {}", usuarioMsvc.getEmail(), usuarioMsvc.getId());
                            CursoUsuario cursoUsuario = new CursoUsuario();
                            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
                            cursoUsuario.setCursoId(cursoId);
                            return cursoUsuarioRepository.save(cursoUsuario)
                                    .flatMap(savedCursoUsuario -> {
                                        curso.addCursoUsuario(savedCursoUsuario);
                                        return cursoRepository.save(curso)
                                                .thenReturn(usuarioMsvc);
                                    });
                        })
                )
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al asignar usuario al curso", e)));
    }

    @Override
    public Mono<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.crear(usuario)
                        .flatMap(usuarioNuevoMsvc -> {
                            CursoUsuario cursoUsuario = new CursoUsuario();
                            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());
                            cursoUsuario.setCursoId(cursoId);
                            logger.info("Mensaje: {}{}", cursoUsuario.getCursoId(), cursoUsuario.getUsuarioId());
                            return cursoUsuarioRepository.save(cursoUsuario)
                                    .doOnSuccess(savedCursoUsuario ->
                                            logger.info("Usuario asignado al curso: cursoId = {}, usuarioId = {}", cursoUsuario.getCursoId(), cursoUsuario.getUsuarioId())
                                    )
                                    .thenReturn(usuarioNuevoMsvc);
                        })
                );
    }

    @Override
    public Mono<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.detalle(usuario.getId())
                        .flatMap(usuarioMsvc -> {
                            // Buscar y eliminar el CursoUsuario correspondiente
                            return cursoUsuarioRepository.findByCursoIdAndUsuarioId(cursoId, usuario.getId())
                                    .flatMap(cursoUsuario -> {
                                        curso.removeCursoUsuario(cursoUsuario);
                                        // Guardar el curso actualizado sin ese CursoUsuario
                                        return cursoRepository.save(curso)
                                                .then(cursoUsuarioRepository.delete(cursoUsuario)) // Eliminar la relación en cursoUsuarioRepository
                                                .thenReturn(usuarioMsvc);
                                    });
                        })
                );
    }
}
