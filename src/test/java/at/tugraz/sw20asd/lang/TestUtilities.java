package at.tugraz.sw20asd.lang;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TestUtilities {
    public static String getRandomString() {
        return java.util.UUID.randomUUID().toString();
    }

    public static boolean isEmptyDirectory(File folder) {
        File[] files = folder.listFiles();
        if(files == null) {
            return true;
        }
        return files.length == 0;
    }

    public static File getRandomWorkingDirectory() {
        String directoryName = TestUtilities.getRandomString();
        File workingDir = new File(directoryName);
        assumeFalse(workingDir.exists());
        assumeTrue(workingDir.mkdirs());
        return workingDir;
    }

    public static void deleteFolder(File folder) {
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

    public static boolean deleteVocabularyFile(File directory, int vocabularyIndex) {
        try {
            return Files.deleteIfExists(Paths.get(directory.getPath(), String.format("%d.vocab", vocabularyIndex)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean vocabularyContainsEntry(Vocabulary v, Entry e) {
        if(v.getEntries() == null) {
            return false;
        }

        for (Entry entry : v.getEntries()) {
            if(entry.getPhrase().equals(e.getPhrase())
                    && entry.getTranslation().equals(e.getTranslation())) {
                return true;
            }
        }
        return false;
    }
}
