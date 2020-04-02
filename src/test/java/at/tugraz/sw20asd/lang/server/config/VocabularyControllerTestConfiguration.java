package at.tugraz.sw20asd.lang.server.config;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@Profile("test")
@Configuration
public class VocabularyControllerTestConfiguration {
    public static final String BAD_VOCABULARY_NAME = "BAD";

    @Bean
    @Primary
    public VocabularyDAO vocabularyDAO() {
        VocabularyDAO mock = Mockito.mock(VocabularyDAO.class);
        doReturn(true).when(mock).addVocabulary(not(argThat(matchesBadVocabularyName())));
        doReturn(false).when(mock).addVocabulary(argThat(matchesBadVocabularyName()));

        return mock;
    }

    private static ArgumentMatcher<Vocabulary> matchesBadVocabularyName() {
        return vocabulary -> vocabulary.getName().contains(BAD_VOCABULARY_NAME);
    }
}
