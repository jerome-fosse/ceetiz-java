package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.model.Entreprise;

public class ChiffreAffaireInconnuException extends RuntimeException {

    public ChiffreAffaireInconnuException(Entreprise entreprise, Integer annee) {
        super("le Chiffre d'affaire " + annee + " pour l'entreprise " + entreprise + " n'est pas disponible");
    }
}
