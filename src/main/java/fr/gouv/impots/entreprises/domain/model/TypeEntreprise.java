package fr.gouv.impots.entreprises.domain.model;

import fr.gouv.impots.entreprises.domain.ImpotCalculator;

import java.util.function.BiFunction;

public enum TypeEntreprise {
    INDIVIDUELLE(ImpotCalculator::calculerImpotsEntrpriseIndivuduelle),
    SAS(ImpotCalculator::calculerImpotsSAS);

    /**
     * Algorithme de calcul de l'impot sur les sociétés
     *
     * Stocker cet algorithme dans l'enum permet de s'assurer, lors de la création de nouveau types,
     * que le mode de calcul sera spécifié. Si cet algorithme venait à se compléxifier ou appeler des
     * services externes, il devra être externalisé dans son propre composant.
     */
    public final BiFunction<ImpotCalculator, Integer, Integer> impotsAlgorithme;

    TypeEntreprise(BiFunction<ImpotCalculator, Integer, Integer> impotsAlgorithme) {
        this.impotsAlgorithme = impotsAlgorithme;
    }
}
