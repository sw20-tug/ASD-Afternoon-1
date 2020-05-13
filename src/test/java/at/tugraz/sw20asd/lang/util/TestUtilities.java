package at.tugraz.sw20asd.lang.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TestUtilities {
    public static String getRandomString(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    public static boolean isEmptyDirectory(File folder) {
        File[] files = folder.listFiles();
        if(files == null) {
            return true;
        }
        return files.length == 0;
    }

    public static File getRandomWorkingDirectory() {
        String directoryName = TestUtilities.getRandomString(25);
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


}
