//package org.chanochoca.springcloud.msvc.usuarios.controllers;
//
//import jakarta.validation.Valid;
//import org.chanochoca.springcloud.msvc.usuarios.models.entity.Usuario;
//import org.chanochoca.springcloud.msvc.usuarios.services.UsuarioService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//@RestController
//@RequestMapping("/usuarios")
//public class UsuarioController {
//
//    private final UsuarioService usuarioService;
//    private final ApplicationContext context;
//    private final Environment env;
//
//    @Autowired
//    public UsuarioController(UsuarioService usuarioService, ApplicationContext applicationContext, Environment env) {
//        this.usuarioService = usuarioService;
//        this.context = applicationContext;
//        this.env = env;
//    }
//
//    /**
//     * Simula la caída de la aplicación cerrando el contexto de Spring.
//     */
//    @GetMapping("/crash")
//    public void crash() {
//        ((ConfigurableApplicationContext) context).close();
//    }
//
//    /**
//     * Retorna una lista de todos los usuarios.
//     *
//     * @return ResponseEntity con la lista de usuarios, la información del pod,
//     *         y el estado HTTP 200.
//     */
//    @GetMapping
//    public ResponseEntity<?> listar() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("users", usuarioService.listar());
//        body.put("podInfo", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
//        body.put("texto", env.getProperty("config.texto"));
//        return ResponseEntity.ok(body);
//    }
//
//    /**
//     * Obtiene los detalles de un usuario específico por su ID.
//     *
//     * @param id ID del usuario a buscar.
//     * @return ResponseEntity con el usuario encontrado y el estado HTTP 200,
//     *         o HTTP 404 si no se encuentra.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<?> detalle(@PathVariable Long id) {
//        Optional<Usuario> o = usuarioService.porId(id);
//        if (o.isPresent()) {
//            return ResponseEntity.ok(o.get());
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * Obtiene una lista de usuarios por sus IDs.
//     *
//     * @param ids Lista de IDs de los usuarios a buscar.
//     * @return ResponseEntity con la lista de usuarios encontrados y el estado HTTP 200,
//     *         o HTTP 404 si no se encuentran usuarios.
//     */
//    @GetMapping("/usuarios-por-curso")
//    public ResponseEntity<?> obtenerUsuariosPorCurso(@RequestParam List<Long> ids) {
//        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
//    }
//
//    /**
//     * Crea un nuevo usuario.
//     *
//     * @param usuario Usuario a crear.
//     * @param result Contiene los errores de validación si existen.
//     * @return ResponseEntity con el usuario creado y el estado HTTP 201,
//     *         o con los errores de validación y el estado HTTP 400.
//     */
//    @PostMapping
//    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
//        if (result.hasErrors()) {
//            return validar(result);
//        }
//
//        if (!usuario.getEmail().isEmpty() && usuarioService.existePorEmail(usuario.getEmail())) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
//    }
//
//    /**
//     * Actualiza un usuario existente.
//     *
//     * @param usuario Usuario con los nuevos datos.
//     * @param result Contiene los errores de validación si existen.
//     * @param id ID del usuario a actualizar.
//     * @return ResponseEntity con el usuario actualizado y el estado HTTP 201,
//     *         o HTTP 404 si no se encuentra el usuario.
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
//        if (result.hasErrors()) {
//            return validar(result);
//        }
//
//        Optional<Usuario> o = usuarioService.porId(id);
//        if (o.isPresent()) {
//            Usuario usuarioDb = o.get();
//            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())
//                    && usuarioService.existePorEmail(usuario.getEmail())) {
//                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
//            }
//
//            usuarioDb.setNombre(usuario.getNombre());
//            usuarioDb.setEmail(usuario.getEmail());
//            usuarioDb.setPassword(usuario.getPassword());
//            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * Elimina un usuario existente.
//     *
//     * @param id ID del usuario a eliminar.
//     * @return ResponseEntity con el estado HTTP 204 si el usuario se elimina,
//     *         o HTTP 404 si no se encuentra el usuario.
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> eliminar(@PathVariable Long id) {
//        Optional<Usuario> o = usuarioService.porId(id);
//        if (o.isPresent()) {
//            usuarioService.eliminar(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * Valida el resultado de la validación de un objeto.
//     *
//     * @param result Resultado de la validación.
//     * @return ResponseEntity con los errores de validación y el estado HTTP 400.
//     */
//    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
//        Map<String, String> errores = new HashMap<>();
//        result.getFieldErrors().forEach(err ->
//                errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
//        return ResponseEntity.badRequest().body(errores);
//    }
//}
