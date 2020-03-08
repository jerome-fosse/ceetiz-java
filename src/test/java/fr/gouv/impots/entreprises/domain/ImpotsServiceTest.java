package fr.gouv.impots.entreprises.domain;

import fr.gouv.impots.entreprises.domain.api.ImpotService;
import fr.gouv.impots.entreprises.domain.model.Adresse;
import fr.gouv.impots.entreprises.domain.model.Entreprise;
import fr.gouv.impots.entreprises.domain.model.EntrepriseIndividuelle;
import fr.gouv.impots.entreprises.domain.model.EntrepriseSAS;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseInconnueException;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ImpotsServiceTest {

    private ImpotService service;

    @BeforeEach
    public void beforeEach() {
        var repository = new EntrepriseRepository() {
            @Override
            public Mono<Entreprise> chargerEntreprise(String siret) {
                switch(siret) {
                    case "12345" :
                        return Mono.just(new EntrepriseIndividuelle("12345","World Company", Map.of(2018, 150_000,2019, 100_000)));
                    case "56789" :
                        return Mono.just(new EntrepriseSAS("56789","World Company", new Adresse("35 rue Victor Hugo", "75001", "Paris"), Map.of(2018, 150_000,2019, 100_000)));
                    case "00000":
                        return Mono.just(new EntrepriseIndividuelle("00000","World Company", Map.of(2018, 150_000,2019, 0)));
                    default:
                        return Mono.empty();
                }
            }
        };
        service = new DomainImpotService(repository);
    }

    @Test
    @DisplayName("L'impot des entreprises individuelles doit être égal à 25% du chiffre d'affaire annuel")
    public void entrepriseIndividuelleCalulImpotOK() {
        // Etant donné une entreprise individuelle dont le chiffre d'affaire 2019 est égal à 100 000
        var siret = "12345";

        // Quand je calcule son impot
        var resp = service.calculerImpotEntreprise(siret, 2019).block();

        // Alors l'impot est égal à 25 000
        assertThat(resp.getT2()).isEqualTo(25_000);
    }

    @Test
    @DisplayName("L'impot des entreprises SAS doit être égal à 33% du chiffre d'affaire annuel")
    public void entrepriseSASCalculImpotOK() {
        // Etant donné une entreprise SAS dont le chiffre d'affaire 2019 est égal à 100 000
        var siret = "56789";

        // Quand je calcule son impot
        var resp = service.calculerImpotEntreprise(siret, 2019).block();

        // Alors l'impot est égal à 33 000
        assertThat(resp.getT2()).isEqualTo(33_000);
    }

    @Test
    @DisplayName("Quand le chiffre d'affaire d'une entreprise est inconnu alors le calcul de l'impot est impossible est renvoie une erreur")
    public void calculImpotKOQuandChiffreAffraireInconnu() {
        // Etant donné une entreprise dont le chiffre d'affaire 2017 n'est pas connu
        var siret = "12345";

        // Quand je calcule son impot
        var thrown = catchThrowable(() ->service.calculerImpotEntreprise(siret, 2017).block());

        // Alors j'ai une erreur
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(ChiffreAffaireInconnuException.class);
        assertThat(thrown.getMessage()).isEqualTo("le Chiffre d'affaire 2017 pour l'entreprise Entreprise(siret=12345, denomination=World Company) n'est pas disponible");
    }


    @Test
    @DisplayName("Quand l'entreprise est inconnu alors le calcul de l'impot est impossible est renvoie une erreur")
    public void calculImpotKOQuandEntrepriseInconnu() {
        // Etant donné une entreprise dont le chiffre d'affaire 2017 n'est pas connu
        var siret = "99999";

        // Quand je calcule son impot
        var thrown = catchThrowable(() -> service.calculerImpotEntreprise(siret, 2017).block());

        // Alors j'ai une erreur
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(EntrepriseInconnueException.class);
        assertThat(thrown.getMessage()).isEqualTo("L'entreprise 99999 est inconnue");
    }

    @Test
    @DisplayName("Quand le chiffre d'affaire d'une entreprise est égal à zéro alors l'impot est égal à zéro")
    public void calculImpotOKQuandChiffreAffaireEgalZero() {
        // Etant donné une entreprise dont le chiffre d'affaire 2019 est égal à 0
        var siret = "00000";

        // Quand je calcule son impot
        var resp = service.calculerImpotEntreprise(siret, 2019).block();

        // Alors l'impot est égal à 0
        assertThat(resp.getT2()).isEqualTo(0);
    }
}
