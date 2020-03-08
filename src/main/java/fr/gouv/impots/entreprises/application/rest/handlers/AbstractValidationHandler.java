package fr.gouv.impots.entreprises.application.rest.handlers;

import io.netty.handler.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractValidationHandler<T> implements RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractValidationHandler.class);

    @SuppressWarnings("unchecked")
    private final Class<T> bodyType = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private Validator validator;

    public AbstractValidationHandler(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return request.bodyToMono(bodyType)
            .flatMap(body -> {
                var errors = new BeanPropertyBindingResult(body, bodyType.getName());
                validator.validate(body, errors);

                if (!errors.getAllErrors().isEmpty()) {
                    throw new ValidationException(formatErrors(errors));
                }

                return processBody(body, request);
            })
            .doOnError(throwable -> LOG.error("Error while validating request of type {}", bodyType.getName(), throwable))
            .onErrorResume(ValidationException.class, throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage(), throwable)))
            .onErrorResume(ServerWebInputException.class, throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage(), throwable)))
            .onErrorResume(DecoderException.class, throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage(), throwable)))
            ;
    }

    private String formatErrors(Errors errors) {
        return errors.getFieldErrorCount() + " erreur(s) lors de la validation de " + errors.getObjectName() + " : " +
            errors.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .reduce((s1, s2) -> s1 + " - " + s2).orElse("Erreurs inconnues");
    }

    protected abstract Mono<ServerResponse> processBody(T body, ServerRequest request);
}
