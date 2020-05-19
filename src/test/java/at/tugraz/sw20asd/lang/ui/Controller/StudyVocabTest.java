package at.tugraz.sw20asd.lang.ui.Controller;
import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
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
        Locale src = Locale.GERMAN;
        Locale dest = Locale.ENGLISH;
        Vocabulary test1 = new Vocabulary(0,"Pets",src,dest);
        test1.addPhrase(new Entry("Hund","dog"));
        test1.addPhrase(new Entry("Katze","cat"));
        test1.addPhrase(new Entry("Fisch","fish"));
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