package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.model.Adresse;
import fr.gouv.impots.entreprises.domain.model.EntrepriseIndividuelle;
import fr.gouv.impots.entreprises.domain.model.EntrepriseSAS;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ImpotsServiceTest {

    @Test
    @DisplayName("L'impot des entreprises individuelles doit être égal à 25% du chiffre d'affaire annuel")
    public void entrepriseIndividuelleCalulImpotOK() {
        // Etant donné une entreprise individuelle dont le chiffre d'affaire 2019 est égal à 100 000
        var entreprise = new EntrepriseIndividuelle("12345","World Company", Map.of(2018, 150_000,2019, 100_000));

        // Quand je calcule son impot
        var service = new ImpotService();
        var impot = service.calculerImpot(entreprise, 2019);

        // Alors l'impot est égal à 25 000
        assertThat(impot).isEqualTo(25_000);
    }

    @Test
    @DisplayName("L'impot des entreprises SAS doit être égal à 33% du chiffre d'affaire annuel")
    public void entrepriseSASCalculImpotOK() {
        // Etant donné une entreprise SAS dont le chiffre d'affaire 2019 est égal à 100 000
        var entreprise = new EntrepriseSAS("12345","World Company", new Adresse("35 rue Victor Hugo", "75001", "Paris"), Map.of(2018, 150_000,2019, 100_000));

        // Quand je calcule son impot
        var service = new ImpotService();
        var impot = service.calculerImpot(entreprise, 2019);

        // Alors l'impot est égal à 33 000
        assertThat(impot).isEqualTo(33_000);
    }

    @Test
    @DisplayName("Quand le chiffre d'affaire d'une entreprise est inconnu alors le calcul de l'impot est impossible est renvoie une erreur")
    public void calculImpotKOQuandChiffreAffraireInconnu() {
        // Etant donné une entreprise dont le chiffre d'affaire 2017 n'est pas connu
        var entreprise = new EntrepriseIndividuelle("12345","World Company", Map.of(2018, 150_000,2019, 100_000));

        // Quand je calcule son impot
        var service = new ImpotService();
        var thrown = catchThrowable(() ->service.calculerImpot(entreprise, 2017));

        // Alors j'ai une erreur
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(ChiffreAffaireInconnu.class);
        assertThat(thrown.getMessage()).isEqualTo("le Chiffre d'affaire 2017 pour l'entreprise Entreprise(siret=12345, denomination=World Company) n'est pas disponible");
    }

    @Test
    @DisplayName("Quand le chiffre d'affaire d'une entreprise est égal à zéro alors l'impot est égal à zéro")
    public void calculImpotOKQuandChiffreAffaireEgalZero() {
        // Etant donné une entreprise dont le chiffre d'affaire 2019 est égal à 0
        var entreprise = new EntrepriseIndividuelle("12345","World Company", Map.of(2018, 150_000,2019, 0));

        // Quand je calcule son impot
        var service = new ImpotService();
        var impot = service.calculerImpot(entreprise, 2019);

        // Alors l'impot est égal à 0
        assertThat(impot).isEqualTo(0);
    }
}
