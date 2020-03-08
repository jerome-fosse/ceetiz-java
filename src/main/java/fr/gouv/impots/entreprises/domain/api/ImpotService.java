package fr.gouv.impots.entreprises.domain.api;

import fr.gouv.impots.entreprises.domain.model.Entreprise;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface ImpotService {

    Mono<Tuple2<Entreprise, Integer>> calculerImpotEntreprise(String siret, Integer annee);
}
