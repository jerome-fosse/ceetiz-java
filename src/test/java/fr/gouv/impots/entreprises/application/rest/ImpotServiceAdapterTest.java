package fr.gouv.impots.entreprises.application.rest;

import fr.gouv.impots.entreprises.domain.api.ImpotService;
import fr.gouv.impots.entreprises.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ImpotServiceAdapterTest {

    private ImpotServiceAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        var service = new ImpotService() {
            @Override
            public Mono<Tuple2<Entreprise, Integer>> calculerImpotEntreprise(String siret, Integer annee) {
                switch (siret) {
                    case "12345":
                        return Mono.just(Tuples.of(new EntrepriseIndividuelle("12345", "World Company", Map.of(2018, 150_000, 2019, 100_000)), 25_000));
                    case "56789":
                        return Mono.just(Tuples.of(new EntrepriseSAS("56789", "World Company", new Adresse("35 rue Victor Hugo", "75001", "Paris"), Map.of(2018, 150_000, 2019, 100_000)), 33_000));
                    default:
                        return Mono.empty();
                }
            }
        };

        adapter = new ImpotServiceAdapter(service);
    }

    @Test
    @DisplayName("Une entreprise individuelle est bien convertie dans le model de l'API REST")
    public void testConversionEntrepriseIndividuelleOPK() {
        // Quand je calule l'impot d'une société
        var resp = adapter.calculerImpotEntreprise("12345", 2019).block();

        // Alors, la réponse récue utilise le modele de l'API Rest
        assertThat(resp).isNotNull();
        assertThat(resp.getT1()).isInstanceOf(fr.gouv.impots.entreprises.application.rest.model.Entreprise.class);
        assertThat(resp.getT1().getSiret()).isEqualTo("12345");
        assertThat(resp.getT1().getDenomination()).isEqualTo("World Company");
        assertThat(resp.getT1().getType()).isEqualTo(TypeEntreprise.INDIVIDUELLE.name());
        assertThat(resp.getT1().getAdresse()).isNull();
    }

    @Test
    @DisplayName("Une entreprise SAS est bien convertie dans le model de l'API REST")
    public void testConversionEntrepriseSASOPK() {
        // Quand je calule l'impot d'une société
        var resp = adapter.calculerImpotEntreprise("56789", 2019).block();

        // Alors, la réponse récue utilise le modele de l'API Rest
        assertThat(resp).isNotNull();
        assertThat(resp.getT1()).isInstanceOf(fr.gouv.impots.entreprises.application.rest.model.Entreprise.class);
        assertThat(resp.getT1().getSiret()).isEqualTo("56789");
        assertThat(resp.getT1().getDenomination()).isEqualTo("World Company");
        assertThat(resp.getT1().getType()).isEqualTo(TypeEntreprise.SAS.name());
        assertThat(resp.getT1().getAdresse()).isNotNull();
        assertThat(resp.getT1().getAdresse().getRue()).isEqualTo("35 rue Victor Hugo");
        assertThat(resp.getT1().getAdresse().getCodePostal()).isEqualTo("75001");
        assertThat(resp.getT1().getAdresse().getVille()).isEqualTo("Paris");
    }
}
