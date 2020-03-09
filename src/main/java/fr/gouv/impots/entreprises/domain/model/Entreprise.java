package fr.gouv.impots.entreprises.domain.model;

import fr.gouv.impots.entreprises.domain.ChiffreAffaireInconnuException;
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

    private final Map<Integer, Integer> chiffresAffaire;

    public Entreprise(String siret, String denomination, Map<Integer, Integer> chiffresAffaire) {
        this.siret = siret;
        this.denomination = denomination;
        this.chiffresAffaire = chiffresAffaire;
    }

    public Optional<Integer> getChiffreAffaire(Integer annee) {
        return Optional.ofNullable(chiffresAffaire.get(annee));
    }

    public Integer calculerImpot(Integer annee) {
        return getChiffreAffaire(annee)
            .map(ca -> Double.valueOf(ca * getTauxImposition()).intValue())
            .orElseThrow(() -> new ChiffreAffaireInconnuException(this, annee));
    }

    protected abstract double getTauxImposition();
}
