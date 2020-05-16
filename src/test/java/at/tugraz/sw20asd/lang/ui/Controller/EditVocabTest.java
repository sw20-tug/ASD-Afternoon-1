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
class EditVocabTest extends LangUiTestBase {

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
        Parent root = new EditVocab(vocab, test1);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
/*
    @Test
    public void testEdit(FxRobot robot) {
        robot.clickOn("#category").write(""); // TODO How do i delete whats in there?
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please fill out the name of your Vocabulary group"));
    }
*/
    @Test
    public void testEdit1(FxRobot robot) {
        robot.clickOn("#add_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#phrase3").write("Hamster");
        robot.clickOn("#translation3").write("hamster");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Vocabulary edited!"));
    }

    @Test
    public void testEdit2(FxRobot robot) {
        robot.clickOn("#add_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#phrase3").write("Hamster");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please check if you have entered" + "\n" + "a matching To for each From in the same line"));
    }
}