package fr.gouv.impots.entreprises.domain.model;

import java.util.Map;

public class EntrepriseIndividuelle extends Entreprise {

    public EntrepriseIndividuelle(String siret, String denomination, Map<Integer, Integer> chiffresAffaire) {
        super(siret, denomination, chiffresAffaire);
    }

    @Override
    protected double getTauxImposition() {
        return 0.25;
    }
}
