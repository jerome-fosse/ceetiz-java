package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.model.Entreprise;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseRepository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class DefaultImpotService implements fr.gouv.impots.entreprises.domain.api.ImpotService {

    private final ImpotCalculator calculator = new ImpotCalculator();
    private EntrepriseRepository repository;

    public DefaultImpotService(EntrepriseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Tuple2<Entreprise, Integer>> calculerImpotEntreprise(String siret, Integer annee) {
        return repository.chargerEntreprise(siret)
            .switchIfEmpty(Mono.error(new EntrepriseInconnueException(siret)))
            .map(entreprise -> Tuples.of(entreprise, calculerImpot(entreprise, annee)))
            ;
    }

    private Integer calculerImpot(Entreprise entreprise, Integer annee) {
        return entreprise.getChiffreAffaire(annee)
            .map(ca -> entreprise.getType().impotsAlgorithme.apply(calculator, ca))
            .orElseThrow(() -> new ChiffreAffaireInconnuException(entreprise, annee));
    }
}
