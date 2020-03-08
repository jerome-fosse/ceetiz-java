package fr.gouv.impots.entreprises.application.rest.handlers;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface RequestHandler {

    Mono<ServerResponse> handleRequest(ServerRequest request);
}
