package fr.gouv.impots.entreprises.application.rest.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CalculerImpotRequest {
    @NotBlank
    private String siret;
    @NotNull
    private Integer annee;
}
