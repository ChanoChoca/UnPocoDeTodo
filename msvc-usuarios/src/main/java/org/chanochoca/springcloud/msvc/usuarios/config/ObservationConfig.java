package org.chanochoca.springcloud.msvc.usuarios.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * La clase `ObservationConfig` es una clase de configuración en Spring que define los beans
 * necesarios para habilitar la observabilidad de métodos en la aplicación.
 *
 * Esta configuración permite la creación de un aspecto (`ObservedAspect`) que intercepta los métodos
 * anotados y registra observaciones, lo que facilita el monitoreo y análisis del comportamiento
 * de la aplicación.
 */
@Configuration
public class ObservationConfig {

    /**
     * Declara un bean de tipo `ObservedAspect` que utiliza un `ObservationRegistry`.
     *
     * @param registry El registro de observaciones que se utiliza para administrar y recopilar
     *                 datos de observación de métodos.
     * @return Una instancia de `ObservedAspect` que está configurada para interceptar y observar
     *         métodos en la aplicación.
     *
     * Este aspecto (`ObservedAspect`) permite la integración de la observabilidad en la aplicación,
     * permitiendo que se registren métricas y datos de seguimiento de los métodos anotados para su
     * monitoreo.
     */
    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }
}
