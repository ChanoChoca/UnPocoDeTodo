package org.chanochoca.springcloud.msvc.cursos.services;

import org.chanochoca.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import org.chanochoca.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.chanochoca.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioClientRest client;

    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, UsuarioClientRest client) {
        this.cursoRepository = cursoRepository;
        this.client = client;
    }

    @Override
    public Flux<Curso> listar() {
        return cursoRepository.findAll();
    }

    @Override
    public Mono<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    public Mono<Curso> porIdConUsuarios(Long id) {
        return cursoRepository.findById(id)
                .flatMap(curso -> {
                    if (curso.getCursoUsuarios().isEmpty()) {
                        return Mono.just(curso);
                    }

                    // Obtener los IDs de los usuarios
                    Flux<Long> ids = Flux.fromIterable(curso.getCursoUsuarios())
                            .map(CursoUsuario::getUsuarioId);

                    // Usar el Flux de IDs para obtener los usuarios
                    return ids.collectList()
                            .flatMapMany(client::obtenerAlumnosPorCurso)
                            .collectList()
                            .doOnNext(curso::setUsuarios)
                            .thenReturn(curso);
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
        return cursoRepository.deleteCursoUsuarioByUsuarioId(id);
    }

    @Override
    public Mono<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.detalle(usuario.getId())
                        .flatMap(usuarioMsvc -> {
                            CursoUsuario cursoUsuario = new CursoUsuario();
                            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
                            curso.addCursoUsuario(cursoUsuario);
                            return cursoRepository.save(curso)
                                    .thenReturn(usuarioMsvc);
                        })
                );
    }

    @Override
    public Mono<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.crear(usuario)
                        .flatMap(usuarioNuevoMsvc -> {
                            CursoUsuario cursoUsuario = new CursoUsuario();
                            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());
                            curso.addCursoUsuario(cursoUsuario);
                            return cursoRepository.save(curso)
                                    .thenReturn(usuarioNuevoMsvc);
                        })
                );
    }

    @Override
    public Mono<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        return cursoRepository.findById(cursoId)
                .flatMap(curso -> client.detalle(usuario.getId())
                        .flatMap(usuarioMsvc -> {
                            CursoUsuario cursoUsuario = new CursoUsuario();
                            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
                            curso.removeCursoUsuario(cursoUsuario);
                            return cursoRepository.save(curso)
                                    .thenReturn(usuarioMsvc);
                        })
                );
    }

    //Método para Kafka
//    @KafkaListener(topics = "eliminar_usuario", groupId = "grupo-id-cursos")
//    public void recibirEliminarUsuario(Long usuarioId) {
//        // Implementa la lógica para eliminar al usuario de todos los cursos
//        cursoRepository.findAll()
//                .flatMap(curso -> {
//                    Usuario usuario = new Usuario();
//                    usuario.setId(usuarioId);
//                    return eliminarUsuario(usuario, curso.getId());
//                }).subscribe();
//    }
}
