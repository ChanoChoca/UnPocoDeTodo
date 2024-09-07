package org.chanochoca.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.chanochoca.springcloud.msvc.cursos.models.Usuario;
import org.chanochoca.springcloud.msvc.cursos.models.entity.Curso;
import org.chanochoca.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * Retorna una lista de todos los cursos disponibles.
     *
     * @return ResponseEntity con la lista de cursos y el estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    /**
     * Obtiene los detalles de un curso específico por su ID.
     *
     * @param id ID del curso a buscar.
     * @return ResponseEntity con el curso encontrado y el estado HTTP 200,
     *         o HTTP 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> o = cursoService.porIdConUsuarios(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuevo curso.
     *
     * @param curso Curso a crear.
     * @param result Contiene los errores de validación si existen.
     * @return ResponseEntity con el curso creado y el estado HTTP 201,
     *         o con los errores de validación y el estado HTTP 400.
     */
    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Curso cursoDb = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    /**
     * Actualiza un curso existente.
     *
     * @param curso Curso con los nuevos datos.
     * @param result Contiene los errores de validación si existen.
     * @param id ID del curso a actualizar.
     * @return ResponseEntity con el curso actualizado y el estado HTTP 201,
     *         o HTTP 404 si no se encuentra el curso.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> o = cursoService.porId(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un curso existente.
     *
     * @param id ID del curso a eliminar.
     * @return ResponseEntity con el estado HTTP 204 si el curso se elimina,
     *         o HTTP 404 si no se encuentra el curso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> o = cursoService.porId(id);
        if (o.isPresent()) {
            cursoService.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Asigna un usuario a un curso.
     *
     * @param usuario Usuario a asignar.
     * @param cursoId ID del curso donde se asignará al usuario.
     * @return ResponseEntity con el usuario asignado y el estado HTTP 201,
     *         o HTTP 404 si ocurre un error.
     */
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.asignarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por " +
                            "el id o error en la comunicación: " + e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea y asigna un nuevo usuario a un curso.
     *
     * @param usuario Usuario a crear y asignar.
     * @param cursoId ID del curso donde se asignará al usuario.
     * @return ResponseEntity con el usuario creado y asignado y el estado HTTP 201,
     *         o HTTP 404 si ocurre un error.
     */
    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicación: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un usuario de un curso.
     *
     * @param usuario Usuario a eliminar del curso.
     * @param cursoId ID del curso del cual se eliminará al usuario.
     * @return ResponseEntity con el usuario eliminado y el estado HTTP 200,
     *         o HTTP 404 si ocurre un error.
     */
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por " +
                            "el id o error en la comunicación: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina todos los usuarios de un curso específico por su ID.
     *
     * @param id ID del curso del cual se eliminarán los usuarios.
     * @return ResponseEntity con el estado HTTP 204 si se eliminan correctamente.
     */
    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Valida el resultado de la validación de un objeto.
     *
     * @param result Resultado de la validación.
     * @return ResponseEntity con los errores de validación y el estado HTTP 400.
     */
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->
                errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
