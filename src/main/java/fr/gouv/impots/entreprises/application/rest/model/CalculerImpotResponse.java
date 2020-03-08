package fr.gouv.impots.entreprises.application.rest.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CalculerImpotResponse {
    private Entreprise entreprise;
    private Integer annee;
    private Integer montant;
}
