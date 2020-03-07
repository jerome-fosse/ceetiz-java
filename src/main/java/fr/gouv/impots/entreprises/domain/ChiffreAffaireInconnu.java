package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.model.Entreprise;

public class ChiffreAffaireInconnu extends RuntimeException {
    private final Entreprise entreprise;
    private final Integer annee;

    public ChiffreAffaireInconnu(Entreprise entreprise, Integer annee) {
        super("le Chiffre d'affaire " + annee + " pour l'entreprise " + entreprise + " n'est pas disponible");
        this.entreprise = entreprise;
        this.annee = annee;
    }
}
