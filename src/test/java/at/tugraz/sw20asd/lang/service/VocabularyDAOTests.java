package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


import static at.tugraz.sw20asd.lang.TestUtilities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
        assertEquals(0, vocabularyDAO.addVocabulary(getTestVocabulary()));
    }

    @Test
    public void testAddVocabularyInsertsIntoList() {
        vocabularyDAO.addVocabulary(getTestVocabulary());
        assertEquals(1, vocabularyDAO.findAll().size());
    }

    @Test
    public void testAddVocabularyCreatesFile() {
        assumeTrue(isEmptyDirectory(workingDirectory));
        vocabularyDAO.addVocabulary(getTestVocabulary());
        assertFalse(isEmptyDirectory(workingDirectory));
    }

    @Test
    public void testAddMultiple() {
        assumeTrue(0 == vocabularyDAO.addVocabulary(getTestVocabulary()));
        assumeTrue(1 == vocabularyDAO.addVocabulary(getTestVocabulary()));

        assertAll(
                () -> assertEquals(2, workingDirectory.listFiles().length),
                () -> assertEquals(2, vocabularyDAO.findAll().size()));
    }

    @Test
    public void testIndexingWithExistingFiles() {
        List<Integer> indices = new ArrayList<>();

        indices.add(vocabularyDAO.addVocabulary(getTestVocabulary()));
        indices.add(vocabularyDAO.addVocabulary(getTestVocabulary()));

        int highestIndexBeforeReset = Collections.max(indices);

        assertTrue(deleteVocabularyFile(workingDirectory, Collections.min(indices)));

        vocabularyDAO = new VocabularyDAOFileImpl(workingDirectory.getName());

        int addedIndex = vocabularyDAO.addVocabulary(getTestVocabulary());

        assertTrue(addedIndex > highestIndexBeforeReset);
    }

    @Test
    public void testAddPhrase() {
        Entry e = new Entry(getRandomString(), getRandomString());
        int vocabIndex = vocabularyDAO.addVocabulary(getTestVocabulary());

        assumeFalse(vocabularyContainsEntry(vocabularyDAO.findById(vocabIndex), e));

        assertAll(
                () -> assertTrue(vocabularyDAO.addEntryToVocabulary(vocabIndex, e)),
                () -> assertTrue(vocabularyContainsEntry(vocabularyDAO.findById((vocabIndex)), e))
        );
    }

    private Vocabulary getTestVocabulary() {
        int vocabularyId = vocabularyIdCounter;
        vocabularyIdCounter++;

        Vocabulary testVocabulary = new Vocabulary();
        testVocabulary.setId(vocabularyId);
        testVocabulary.setName(getRandomString());
        testVocabulary.setSourceLanguage(Locale.FRENCH);
        testVocabulary.setTargetLanguage(Locale.CHINESE);
        return testVocabulary;
    }
}
