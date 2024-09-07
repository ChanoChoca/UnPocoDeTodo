package org.chanochoca.springcloud.msvc.cursos.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * `ObservationConfig` es una clase de configuración de Spring que define los componentes necesarios para habilitar
 * la observación y el monitoreo en la aplicación.
 *
 * Utiliza `Micrometer` para integrar aspectos de observación, lo que permite rastrear y registrar eventos de interés
 * durante la ejecución de la aplicación. Esto es útil para el monitoreo y la recopilación de métricas de rendimiento.
 */
@Configuration
public class ObservationConfig {

    /**
     * Crea un bean `ObservedAspect` que se encarga de aplicar aspectos de observación a los métodos de la aplicación.
     *
     * @param registry El registro de observación que gestiona las observaciones y métricas en la aplicación.
     * @return Una instancia de `ObservedAspect` que habilita la observación para los métodos anotados.
     *
     * Este método define un bean de tipo `ObservedAspect`, que es utilizado para aplicar la funcionalidad de observación
     * a los métodos de la aplicación. La observación permite medir y rastrear el comportamiento de los métodos, facilitando
     * la monitorización y el análisis del rendimiento. El `ObservationRegistry` es responsable de mantener el estado y
     * las configuraciones de las observaciones.
     */
    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }
}
