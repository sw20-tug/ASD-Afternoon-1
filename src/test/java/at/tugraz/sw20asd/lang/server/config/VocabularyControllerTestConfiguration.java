package at.tugraz.sw20asd.lang.server.config;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@Profile("test")
@Configuration
public class VocabularyControllerTestConfiguration {
    public static final String BAD_VOCABULARY_NAME = "BAD";

    @Bean
    @Primary
    public VocabularyDAO vocabularyDAO() {
        Collection<Vocabulary> vocabs = new ArrayList<>();

        vocabs.add(getNewVocabulary(1));
        vocabs.add(getNewVocabulary(2));

        VocabularyDAO mock = Mockito.mock(VocabularyDAO.class);
        // addVocabulary
        doReturn(true).when(mock).addVocabulary(not(argThat(matchesBadVocabularyName())));
        doReturn(false).when(mock).addVocabulary(argThat(matchesBadVocabularyName()));

        // findAll
        doReturn(vocabs).when(mock).findAll();

        // findById
        doReturn(vocabs.toArray()[0]).when(mock).findById(eq(1));

        return mock;
    }

    private static ArgumentMatcher<Vocabulary> matchesBadVocabularyName() {
        return vocabulary -> vocabulary.getName().contains(BAD_VOCABULARY_NAME);
    }

    private static Vocabulary getNewVocabulary(int id) {
        Vocabulary vocab = new Vocabulary(id, "My-Vocab-" + id, Locale.GERMAN, Locale.FRENCH);
        vocab.addPhrase(new Entry("foo", "bar"));
        return vocab;
    }
}
