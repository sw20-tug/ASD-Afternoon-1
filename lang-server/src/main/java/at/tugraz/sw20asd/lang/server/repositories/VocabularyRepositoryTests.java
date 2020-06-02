package at.tugraz.sw20asd.lang.server.repositories;

import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VocabularyRepositoryTests {

    @Autowired
    private VocabularyRepository _vocabularyRepository;

    @Test
    public void saveVocabulary() {
        _vocabularyRepository.save(new Vocabulary(null, "myVocab", ENGLISH, FRENCH));

        List<Vocabulary> vocabularies = _vocabularyRepository.findAll();
        assertThat(vocabularies.size()).isEqualTo(1);
    }

    @Test
    public void saveVocabulary_ThrowsWhenNameIsNull() {
        assertThrows(
                ConstraintViolationException.class,
                () -> _vocabularyRepository.save(new Vocabulary(null, null, ENGLISH, GERMAN))
        );
    }

    public static Collection<Object[]> invalidLocaleSets() {
        return Arrays.asList(new Object[][]{
                {null, GERMAN},
                {ENGLISH, null},
                {null, null}
        });
    }

    @ParameterizedTest
    @MethodSource("invalidLocaleSets")
    void saveVocabulary_ThrowsWhenLocalesAreNotInitialized(Locale src, Locale dst) {
        assertThrows(
                ConstraintViolationException.class,
                () -> _vocabularyRepository.save(new Vocabulary(null, "testVocab", src, dst))
        );
    }
}
