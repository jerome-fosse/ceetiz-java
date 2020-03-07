package fr.gouv.impots.entreprises.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(of = {"siret"})
@ToString(of = {"siret", "denomination"})
public abstract class Entreprise {
    @Getter
    private final String siret;
    @Getter
    private final String denomination;
    @Getter
    private final TypeEntreprise type;

    private final Map<Integer, Integer> chiffresAffaire;

    public Entreprise(String siret, String denomination, TypeEntreprise type, Map<Integer, Integer> chiffresAffaire) {
        this.siret = siret;
        this.denomination = denomination;
        this.type = type;
        this.chiffresAffaire = chiffresAffaire;
    }

    public Optional<Integer> getChiffreAffaire(Integer annee) {
        return Optional.ofNullable(chiffresAffaire.get(annee));
    }
}
