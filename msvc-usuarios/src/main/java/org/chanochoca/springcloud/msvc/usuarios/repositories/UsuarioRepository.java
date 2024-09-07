package org.chanochoca.springcloud.msvc.usuarios.repositories;

import org.chanochoca.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//no es necesario utilizar @Repostory o @Component, porque
//ya es un objeto componente Spring
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    //Dos formas de hacer lo mismo
    Optional<Usuario> findByEmail(String email);

    @Query("select u from Usuario u where u.email=?1")
    Optional<Usuario> porEmail(String email);

    boolean existsByEmail(String email);
}
