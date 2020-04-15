package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import at.tugraz.sw20asd.lang.TestUtilities;

import static at.tugraz.sw20asd.lang.TestUtilities.*;
import static org.junit.jupiter.api.Assertions.*;

public class VocabularyDAOTests {
    private VocabularyDAO vocabularyDAO;
    private File workingDirectory;

    private int vocabularyIdCounter;

    @BeforeEach
    private void init() {
        workingDirectory = getRandomWorkingDirectory();
        vocabularyDAO = new VocabularyDAOFileImpl(workingDirectory.getName());
        vocabularyIdCounter = 0;
    }

    @AfterEach()
    private void cleanup() {
        deleteFolder(workingDirectory);
    }

    @Test
    public void testVocabulariesAreInitializedEmpty() {
        assertTrue(vocabularyDAO.findAll().isEmpty());
    }

    @Test
    public void testCanAddVocabulary() {
        assertTrue(vocabularyDAO.addVocabulary(getTestVocabulary()));
    }

    @Test
    public void testAddVocabularyInsertsIntoList() {
        vocabularyDAO.addVocabulary(getTestVocabulary());
        assertEquals(1, vocabularyDAO.findAll().size());
    }

    @Test
    public void testAddVocabularyCreatesFile() {
        vocabularyDAO.addVocabulary(getTestVocabulary());
        assertFalse(isEmptyDirectory(workingDirectory));
    }

    @Test
    public void testAddMultiple() {
        vocabularyDAO.addVocabulary(getTestVocabulary());
        vocabularyDAO.addVocabulary(getTestVocabulary());

        assertAll(
                () -> assertEquals(2, workingDirectory.listFiles().length),
                () -> assertEquals(2, vocabularyDAO.findAll().size()));
    }

    private Vocabulary getTestVocabulary() {
        int vocabularyId = vocabularyIdCounter;
        vocabularyIdCounter++;

        Vocabulary testVocabulary = new Vocabulary();
        testVocabulary.setId(vocabularyId);
        testVocabulary.setName(TestUtilities.getRandomString());
        testVocabulary.setSourceLanguage(Locale.FRENCH);
        testVocabulary.setTargetLanguage(Locale.CHINESE);
        return testVocabulary;
    }
}
