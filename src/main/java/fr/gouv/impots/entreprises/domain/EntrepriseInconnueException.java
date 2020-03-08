package fr.gouv.impots.entreprises.domain;

public class EntrepriseInconnueException extends RuntimeException {

    public EntrepriseInconnueException(String siret) {
        super("L'entreprise " + siret + " est inconnue");
    }
}
