package fr.gouv.impots.entreprises.application.rest.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Adresse {
    private String rue;
    private String codePostal;
    private String ville;
}
