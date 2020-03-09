package fr.gouv.impots.entreprises.domain.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class EntrepriseSAS extends Entreprise {
    private final Adresse adresse;

    public EntrepriseSAS(String siret, String denomination, Adresse adresse, Map<Integer, Integer> chiffresAffaire) {
        super(siret, denomination, chiffresAffaire);
        this.adresse = adresse;
    }

    @Override
    protected double getTauxImposition() {
        return 0.33;
    }
}
