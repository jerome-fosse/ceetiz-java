package fr.gouv.impots.entreprises.application.rest;

import fr.gouv.impots.entreprises.application.rest.handlers.RequestHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class WebfluxConfiguration {

    @Bean
    public RouterFunction<ServerResponse> calculerImpotRouter(@Qualifier("calculerImpotHandler")RequestHandler handler) {
        return route(POST("/impot/calculer"), handler::handleRequest);
    }
}
