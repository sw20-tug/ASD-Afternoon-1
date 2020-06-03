package at.tugraz.sw20asd.lang.ui.controllers;

import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccessRestImpl;
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
class AddVocabTest extends LangUiTestBase {

    private VocabularyAccess vocab;

    @Start
    public void start(Stage stage) throws Exception {
        vocab = new VocabularyAccessRestImpl("localhost", 8080);
        Parent root = new AddVocab(vocab);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAddInput(FxRobot robot) {
        robot.clickOn("#category").write("Fruits");
        WaitForAsyncUtils.waitForFxEvents(100);
        robot.clickOn("#from_field").write("Apfel");
        robot.clickOn("#to_field").write("apple");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please select from and to language"));
    }

    @Test
    public void testAddInput1(FxRobot robot) {
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please select from and to language"));
    }

    @Test
    public void testAddInput2(FxRobot robot) {
        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
        robot.clickOn("#to_choice").dropTo("English").clickOn("English");
        robot.clickOn("#category").write("Fruits");
        robot.clickOn("#from_field").write("Apfel");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please check if you have entered" + "\n" + "a matching To for each From in the same line"));
    }

    @Test
    public void testAddInput3(FxRobot robot) {
        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
        robot.clickOn("#to_choice").dropTo("English").clickOn("English");
        robot.clickOn("#from_field").write("Apfel");
        robot.clickOn("#to_field").write("apple");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please fill out the Name of your Vocabulary group"));
    }

    @Test
    public void testAddInput4(FxRobot robot) {
        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
        robot.clickOn("#to_choice").dropTo("English").clickOn("English");
        robot.clickOn("#category").write("Fruitsalat");
        robot.clickOn("#from_field").write("Apfel");
        robot.clickOn("#to_field1").write("banana");
        robot.clickOn("#from_field1").write("Kartoffel");
        robot.clickOn("#from_field2").write("Fruchtsalat");
        robot.clickOn("#to_field3").write("potato");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please check if you have entered" + "\n" + "a matching To for each From in the same line"));
    }
}