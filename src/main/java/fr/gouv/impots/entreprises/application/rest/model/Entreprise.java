package fr.gouv.impots.entreprises.application.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class Entreprise {
    private String siret;
    private String denomination;
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Adresse adresse;

    public Entreprise(String siret, String denomination, String type) {
        this.siret = siret;
        this.denomination = denomination;
        this.type = type;
    }

    public Entreprise(String siret, String denomination, String type, Adresse adresse) {
        this.siret = siret;
        this.denomination = denomination;
        this.type = type;
        this.adresse = adresse;
    }
}
