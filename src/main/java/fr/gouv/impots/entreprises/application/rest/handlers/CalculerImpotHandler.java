package fr.gouv.impots.entreprises.application.rest.handlers;

import fr.gouv.impots.entreprises.application.rest.ImpotServiceAdapter;
import fr.gouv.impots.entreprises.application.rest.model.CalculerImpotRequest;
import fr.gouv.impots.entreprises.application.rest.model.CalculerImpotResponse;
import fr.gouv.impots.entreprises.domain.ChiffreAffaireInconnuException;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseInconnueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class CalculerImpotHandler extends AbstractValidationHandler<CalculerImpotRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(CalculerImpotHandler.class);

    private ImpotServiceAdapter impotServiceAdapter;

    public CalculerImpotHandler(ImpotServiceAdapter impotServiceAdapter, @Qualifier("defaultValidator") Validator validator) {
        super(validator);
        this.impotServiceAdapter = impotServiceAdapter;
    }

    @Override
    protected Mono<ServerResponse> processBody(CalculerImpotRequest body, ServerRequest request) {
        LOG.info("Demande de calcul de l'impot {} pour l'entreprise {}", body.getAnnee(), body.getSiret());

        return impotServiceAdapter.calculerImpotEntreprise(body.getSiret(), body.getAnnee())
            .flatMap(tuple -> ServerResponse.ok().body(BodyInserters.fromValue(new CalculerImpotResponse(tuple.getT1(), body.getAnnee(), tuple.getT2()))))
            .switchIfEmpty(Mono.error(new NullPointerException()))
            .doOnSuccess(response -> LOG.info("L'impot {} pour la société {} a été calculé avec succes", body.getAnnee(), body.getSiret()))
            .doOnError(thrown -> LOG.error(thrown.getMessage()))
            .onErrorResume(ChiffreAffaireInconnuException.class, throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage(), throwable)))
            .onErrorResume(EntrepriseInconnueException.class, throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage(), throwable)))
            ;
    }

}
