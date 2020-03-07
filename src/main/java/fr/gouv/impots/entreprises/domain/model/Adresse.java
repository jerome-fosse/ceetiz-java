package fr.gouv.impots.entreprises.domain.model;

import lombok.Value;

@Value
public class Adresse {
    private String rue;
    private String codePostal;
    private String ville;
}
