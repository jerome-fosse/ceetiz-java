package fr.gouv.impots.entreprises;

import fr.gouv.impots.entreprises.domain.DefaultImpotService;
import fr.gouv.impots.entreprises.domain.api.ImpotService;
import fr.gouv.impots.entreprises.domain.spi.EntrepriseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JCalculatorApplication.class, args);
    }

    @Bean
    public ImpotService impotService(EntrepriseRepository entrepriseRepository) {
        return new DefaultImpotService(entrepriseRepository);
    }
}
