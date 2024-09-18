package org.chanochoca.springcloud.msvc.usuarios.services;

import org.chanochoca.springcloud.msvc.usuarios.clients.CursoClienteRest;
import org.chanochoca.springcloud.msvc.usuarios.entity.Usuario;
import org.chanochoca.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioServiceImpl implements UsuarioService {


//    private final KafkaTemplate<String, Long> kafkaTemplate;
    private final UsuarioRepository usuarioRepository;
    private final CursoClienteRest client;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CursoClienteRest client) {
        this.usuarioRepository = usuarioRepository;
        this.client = client;
    }

//    @Autowired
//    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CursoClienteRest client, KafkaTemplate<String, Long> kafkaTemplate) {
//        this.usuarioRepository = usuarioRepository;
//        this.client = client;
//        this.kafkaTemplate = kafkaTemplate;
//    }

//    private static final String TOPIC = "eliminar_usuario";

    @Override
    public Flux<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Mono<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return usuarioRepository.deleteById(id)
                .then(Mono.fromRunnable(() -> client.eliminarCursoUsuarioPorId(id)));
    }

    //Método para usar Kafka
//    @Override
//    public Mono<Void> eliminar(Long id) {
//        return usuarioRepository.deleteById(id)
//                .then(Mono.fromRunnable(() -> kafkaTemplate.send(TOPIC, id)));
//    }

    @Override
    public Flux<Usuario> listarPorIds(Iterable<Long> ids) {
        return usuarioRepository.findAllById(ids);
    }

    @Override
    public Mono<Usuario> porEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Mono<Boolean> existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
