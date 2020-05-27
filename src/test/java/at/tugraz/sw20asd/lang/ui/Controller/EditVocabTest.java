package at.tugraz.sw20asd.lang.ui.Controller;
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


@ExtendWith(ApplicationExtension.class)
class EditVocabTest extends LangUiTestBase {

    private VocabularyAccess vocab;

    @Start
    public void start(Stage stage) throws Exception {
        vocab = new VocabularyAccessRestImpl("localhost", 8080);
        Parent root = new EditVocab(vocab, 0, 3);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testEdit(FxRobot robot) {
        robot.clickOn("#add_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#phrase3").write("Apfel");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please leave no textfields empty"));
    }

    @Test
    public void testEdit1(FxRobot robot) {
        robot.clickOn("#category").eraseText(4);
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please fill out the Name of your Vocabulary group"));
    }

    @Test
    public void testEdit2(FxRobot robot) {
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("User Information"));
    }

}