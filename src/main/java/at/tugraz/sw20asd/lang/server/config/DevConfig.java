package at.tugraz.sw20asd.lang.server.config;

import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import at.tugraz.sw20asd.lang.service.VocabularyDAOFileImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DevConfig {

    @Bean
    public VocabularyDAO vocabularyDAO() {
        return new VocabularyDAOFileImpl("vocabs");
    }
}
