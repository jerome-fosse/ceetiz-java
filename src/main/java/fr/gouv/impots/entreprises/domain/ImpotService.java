package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.model.Entreprise;

public class ImpotService {

    public Integer calculerImpot(Entreprise entreprise, Integer annee) {
        var taxCalculator = new ImpotCalculator();
        return entreprise.getChiffreAffaire(annee)
            .map(ca -> entreprise.getType().impotsAlgorithme.apply(taxCalculator, ca))
            .orElseThrow(() -> new ChiffreAffaireInconnu(entreprise, annee));
    }
}
