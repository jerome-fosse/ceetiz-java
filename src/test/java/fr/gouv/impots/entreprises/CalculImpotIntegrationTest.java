package fr.gouv.impots.entreprises;

import fr.gouv.impots.entreprises.application.rest.model.CalculerImpotResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalculImpotIntegrationTest {

    @LocalServerPort
    private int localPort;

    private WebTestClient webTestClient;

    @BeforeEach
    public void beforeEach() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:" + localPort)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    @Test
    @DisplayName("L'impot pour les sociétés individuelles est de 25% du CA annuel")
    public void calculEntrepriseIndividuelleOK() {
        // Quand je lance le calucl de l'impot pour une société individuelle
        var request = webTestClient.post().uri("/impot/calculer").body(BodyInserters.fromValue("{\"siret\":\"12345\", \"annee\":2019}"));
        var response = request.exchange();

        // Le code retour HTTP est OK
        response.expectStatus().isOk();

        // le body est OK
        var body = response.expectBody(CalculerImpotResponse.class).returnResult().getResponseBody();
        assertThat(body.getEntreprise().getSiret()).isEqualTo("12345");
        assertThat(body.getEntreprise().getDenomination()).isEqualTo("World Company");
        assertThat(body.getEntreprise().getType()).isEqualTo("INDIVIDUELLE");
        assertThat(body.getEntreprise().getAdresse()).isNull();
        assertThat(body.getAnnee()).isEqualTo(2019);
        assertThat(body.getMontant()).isEqualTo(25_000);
    }

    @Test
    @DisplayName("L'impot pour les sociétés SAS est de 33% du CA annuel")
    public void calculEntrepriseSASOK() {
        // Quand je lance le calucl de l'impot pour une société SAS
        var request = webTestClient.post().uri("/impot/calculer").body(BodyInserters.fromValue("{\"siret\":\"56789\", \"annee\":2019}"));
        var response = request.exchange();

        // Le code retour HTTP est OK
        response.expectStatus().isOk();

        // le body est OK
        var body = response.expectBody(CalculerImpotResponse.class).returnResult().getResponseBody();
        assertThat(body.getEntreprise().getSiret()).isEqualTo("56789");
        assertThat(body.getEntreprise().getDenomination()).isEqualTo("World Company");
        assertThat(body.getEntreprise().getType()).isEqualTo("SAS");
        assertThat(body.getEntreprise().getAdresse().getRue()).isEqualTo("35 rue Victor Hugo");
        assertThat(body.getEntreprise().getAdresse().getCodePostal()).isEqualTo("75001");
        assertThat(body.getEntreprise().getAdresse().getVille()).isEqualTo("Paris");
        assertThat(body.getAnnee()).isEqualTo(2019);
        assertThat(body.getMontant()).isEqualTo(33_000);
    }

    @Test
    @DisplayName("Lorsque la société n'existe pas le systeme retourne une erreur")
    public void calculEntrepriseInconnueKO() {
        // Quand je lance le calucl de l'impot pour uve société qui n'existe pas
        var request = webTestClient.post().uri("/impot/calculer").body(BodyInserters.fromValue("{\"siret\":\"00000\", \"annee\":2019}"));
        var response = request.exchange();

        // Le code retour HTTP est KO
        response.expectStatus().isBadRequest();

        // Le message d'erreur indique que la société n'existe pas
        response.expectBody().jsonPath("$.message").isEqualTo("L'entreprise 00000 est inconnue");
    }
}
