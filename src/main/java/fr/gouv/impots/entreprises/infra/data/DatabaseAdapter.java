package fr.gouv.impots.entreprises.infra.data;

import fr.gouv.impots.entreprises.domain.model.Adresse;
import fr.gouv.impots.entreprises.domain.model.Entreprise;
import fr.gouv.impots.entreprises.domain.model.EntrepriseIndividuelle;
import fr.gouv.impots.entreprises.domain.model.EntrepriseSAS;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Dans un cas réel cet adapter appellerais les repository pour charger les données depuis la database.
 * Ici, pour le test il se contente de renvoyer des données en dur.
 */
@Component
public class DatabaseAdapter implements EntrepriseRepository {
    @Override
    public Mono<Entreprise> chargerEntreprise(String siret) {
        switch (siret) {
            case "12345":
                return Mono.just(new EntrepriseIndividuelle("12345", "World Company", Map.of(2018, 150_000, 2019, 100_000)));
            case "56789":
                return Mono.just(new EntrepriseSAS("56789", "World Company", new Adresse("35 rue Victor Hugo", "75001", "Paris"), Map.of(2018, 150_000, 2019, 100_000)));
            default:
                return Mono.empty();
        }
    }
}
