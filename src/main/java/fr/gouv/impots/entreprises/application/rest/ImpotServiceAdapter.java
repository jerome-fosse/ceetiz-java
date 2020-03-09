package fr.gouv.impots.entreprises.application.rest;

import fr.gouv.impots.entreprises.application.rest.model.Adresse;
import fr.gouv.impots.entreprises.application.rest.model.Entreprise;
import fr.gouv.impots.entreprises.domain.api.ImpotService;
import fr.gouv.impots.entreprises.domain.model.EntrepriseIndividuelle;
import fr.gouv.impots.entreprises.domain.model.EntrepriseSAS;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class ImpotServiceAdapter {

    private ImpotService service;

    public ImpotServiceAdapter(ImpotService service) {
        this.service = service;
    }

    public Mono<Tuple2<Entreprise, Integer>> calculerImpotEntreprise(String siret, Integer annee) {
        return service.calculerImpotEntreprise(siret, annee)
            .map(tuple -> Tuples.of(mapDomainEntrepriseToRestEntreprise(tuple.getT1()), tuple.getT2()))
            ;
    }

    // Ici on pourrait utiliser le polymorphisme et implémenter la méthode de convertion dans le type réel de l'entreprise
    // (EntrepriseIndividuelle ou EntrepriseSAS) mais comme l'architecture hexagonale interdit à notre domaine d'avoir
    // des dépendances vers l'exterieur, on est obligé d'avoir des instanceof et des cast.
    //
    // Java 14 devrait améliorer les choses.
    private Entreprise mapDomainEntrepriseToRestEntreprise(fr.gouv.impots.entreprises.domain.model.Entreprise entreprise) {
        if (entreprise instanceof EntrepriseIndividuelle) {
            return new Entreprise(entreprise.getSiret(), entreprise.getDenomination(), "INDIVIDUELLE");
        } else {
            var sas = (EntrepriseSAS) entreprise;
            var adresse = new Adresse(sas.getAdresse().getRue(), sas.getAdresse().getCodePostal(), sas.getAdresse().getVille());
            return new Entreprise(sas.getSiret(), sas.getDenomination(), "SAS", adresse);
        }
    }
}
