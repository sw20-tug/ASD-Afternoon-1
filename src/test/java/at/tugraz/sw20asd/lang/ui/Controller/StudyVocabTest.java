package at.tugraz.sw20asd.lang.ui.Controller;
import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.VocabularyAccessRestImpl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;


@ExtendWith(ApplicationExtension.class)
class StudyVocabTest extends LangUiTestBase {

    private VocabularyAccess vocab;

    @Start
    public void start(Stage stage) throws Exception {
        vocab = new VocabularyAccessRestImpl("localhost", 8080);
        long id = 0;
        Locale src = Locale.GERMAN;
        Locale dest = Locale.ENGLISH;
        VocabularyDetailDto test1 = new VocabularyDetailDto(id,"Pets",src,dest);
        test1.addEntry(new EntryDto("Hund","dog"));
        test1.addEntry(new EntryDto("Katze","cat"));
        test1.addEntry(new EntryDto("Fisch","fish"));
        Parent root = new StudyVocab(vocab, test1, "en");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testStudyInput(FxRobot robot) {
        robot.clickOn("#study0").write("dog");
        robot.clickOn("#study1").write("cat");
        robot.clickOn("#study2").write("fish");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("All answers are correct"));
    }

    @Test
    public void testStudyInput1(FxRobot robot) {
        robot.clickOn("#study0").write("dog");
        robot.clickOn("#study1").write("fish");
        robot.clickOn("#study2").write("cat");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Try again!"));
    }

    @Test
    public void testStudyInput2(FxRobot robot) {
        robot.clickOn("#study0").write("dog");
        robot.clickOn("#study1").write("cat");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Try again!"));
    }
}