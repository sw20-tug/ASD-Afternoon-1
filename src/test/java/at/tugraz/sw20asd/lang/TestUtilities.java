package at.tugraz.sw20asd.lang;

import java.io.File;

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
}
