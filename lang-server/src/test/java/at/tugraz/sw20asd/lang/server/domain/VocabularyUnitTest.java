package at.tugraz.sw20asd.lang.server.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import static at.tugraz.sw20asd.lang.server.util.TestUtilities.getRandomString;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

public class VocabularyUnitTest {
    private Vocabulary vocabulary;
    private String initialName;
    private Locale initialSourceLanguage;
    private Locale initialTargetLanguage;

    @BeforeEach
    public void init() {
        initialName = getRandomString(25);
        initialSourceLanguage = GERMAN;
        initialTargetLanguage = FRENCH;

        vocabulary = new Vocabulary(null, initialName, initialSourceLanguage, initialTargetLanguage);
    }

    @Test
    public void test_GetName() {
        assertThat(vocabulary.getName()).isEqualTo(initialName);
    }

    @Test
    public void test_GetSourceLanguage() {
        assertThat(vocabulary.getSourceLanguage()).isEqualTo(initialSourceLanguage);
    }

    @Test
    public void test_GetTargetLanguage() {
        assertThat(vocabulary.getTargetLanguage()).isEqualTo(initialTargetLanguage);
    }

    @Test
    public void test_SetAndGetName() {
        String newName = getRandomString(15);
        vocabulary.setName(newName);

        assertThat(vocabulary.getName()).isEqualTo(newName);
    }

    @Test
    public void test_SetAndGetSourceLanguage() {
        Locale newLocale = ITALIAN;
        vocabulary.setSourceLanguage(newLocale);

        assertThat(vocabulary.getSourceLanguage()).isEqualTo(newLocale);
    }

    @Test
    public void test_SetAndGetTargetLanguage() {
        Locale newLocale = JAPANESE;
        vocabulary.setTargetLanguage(newLocale);

        assertThat(vocabulary.getTargetLanguage()).isEqualTo(newLocale);
    }

    @Test
    public void test_AddEntry() {
        Entry e = getRandomEntry();
        vocabulary.addEntry(e);

        assertThat(vocabulary.getEntries().size()).isEqualTo(1);
    }

    @Test
    public void test_AddEntry_SetsVocabularyReference() {
        Entry e = getRandomEntry();
        assumeThat(e.getVocabulary()).isNull();

        vocabulary.addEntry(e);
        assertThat(e.getVocabulary()).isEqualTo(vocabulary);

        for (Entry storedEntry : vocabulary.getEntries()) {
            assertThat(storedEntry.getVocabulary()).isEqualTo(vocabulary);
        }
    }

    @Test
    public void test_SetEntries_SetsVocabularyReference() {
        vocabulary.setEntries(new HashSet<>(
                Arrays.asList(
                        getRandomEntry(),
                        getRandomEntry(),
                        getRandomEntry()
                )
        ));

        for (Entry storedEntry : vocabulary.getEntries()) {
            assertThat(storedEntry.getVocabulary()).isEqualTo(vocabulary);
        }
    }

    private Entry getRandomEntry() {
        return new Entry(getRandomString(15), getRandomString(15));
    }
}
