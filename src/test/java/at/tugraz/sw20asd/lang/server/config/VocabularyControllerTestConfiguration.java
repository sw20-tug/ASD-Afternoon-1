package at.tugraz.sw20asd.lang.server.config;

import at.tugraz.sw20asd.lang.util.TestUtilities;
import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@Profile("test")
@Configuration
public class VocabularyControllerTestConfiguration {
    private Map<Integer, Vocabulary> vocabularies;

    @Bean
    @Primary
    public VocabularyDAO vocabularyDAO() {
        vocabularies = new HashMap<>();

        vocabularies.put(2, getNewVocabulary(2));
        vocabularies.put(3, getNewVocabulary(3));

        VocabularyDAO mock = Mockito.mock(VocabularyDAO.class);
        // addVocabulary
        doAnswer(inv -> {
            Vocabulary vocab = (Vocabulary) inv.getArguments()[0];
            Integer nextFreeIndex = Collections.max(vocabularies.keySet()) + 1;
            vocabularies.put(nextFreeIndex, vocab);
            return nextFreeIndex;

        }).when(mock).addVocabulary(any(Vocabulary.class));

        // findAll
        doReturn(vocabularies.values()).when(mock).findAll();

        // findById
        doAnswer(inv ->
                vocabularies.get(inv.getArgument(0))).when(mock).findById(anyInt());

        return mock;
    }

    private static Vocabulary getNewVocabulary(int id) {
        Vocabulary vocab = new Vocabulary(
                id,
                "My-Vocab-" + id + "__" + TestUtilities.getRandomString(10),
                Locale.GERMAN,
                Locale.FRENCH);
        vocab.addPhrase(new Entry("foo", "bar"));
        return vocab;
    }
}
