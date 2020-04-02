package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

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
        testVocabulary.setName(getRandomString());
        testVocabulary.setSourceLanguage(Locale.FRENCH);
        testVocabulary.setTargetLanguage(Locale.CHINESE);
        return testVocabulary;
    }

    private boolean isEmptyDirectory(File folder) {
        File[] files = folder.listFiles();
        if(files == null) {
            return true;
        }
        return files.length == 0;
    }

    private String getRandomString() {
        return java.util.UUID.randomUUID().toString();
    }

    private File getRandomWorkingDirectory() {
        String directoryName = getRandomString();
        File workingDir = new File(directoryName);
        assumeFalse(workingDir.exists());
        assumeTrue(workingDir.mkdirs());
        return workingDir;
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory()){
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
