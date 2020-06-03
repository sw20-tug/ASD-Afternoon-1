package at.tugraz.sw20asd.lang.server.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static at.tugraz.sw20asd.lang.server.util.TestUtilities.getRandomString;
import static org.assertj.core.api.Assertions.assertThat;

public class EntryUnitTest {

    private Entry entry;
    private String initialPhrase;
    private String initialTranslation;


    @BeforeEach
    public void init() {
        initialPhrase = getRandomString(20);
        initialTranslation = getRandomString(20);

        entry = new Entry(initialPhrase, initialTranslation);
    }

    @Test
    public void test_GetPhrase() {
        assertThat(entry.getPhrase()).isEqualTo(initialPhrase);
    }

    @Test
    public void test_GetTranslation() {
        assertThat(entry.getTranslation()).isEqualTo(initialTranslation);
    }

    @Test
    public void test_SetAndGetPhrase() {
        String newPhrase = getRandomString(15);
        entry.setPhrase(newPhrase);

        assertThat(entry.getPhrase()).isEqualTo(newPhrase);
    }

    @Test
    public void test_SetAndGetTranslation() {
        String newTranslation = getRandomString(15);
        entry.setTranslation(newTranslation);

        assertThat(entry.getTranslation()).isEqualTo(newTranslation);
    }
}
