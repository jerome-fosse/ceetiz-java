package fr.gouv.impots.entreprises.domain.spi;

import fr.gouv.impots.entreprises.domain.model.Entreprise;
import reactor.core.publisher.Mono;

public interface EntrepriseRepository {

    Mono<Entreprise> chargerEntreprise(String siret);
}
