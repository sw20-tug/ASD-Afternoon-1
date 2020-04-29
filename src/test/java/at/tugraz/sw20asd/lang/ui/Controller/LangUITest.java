package at.tugraz.sw20asd.lang.ui.Controller;
import javafx.stage.Stage;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
class LangUITest {
    private Stage prim;
    private LangUI main;

    @BeforeAll
    public static void setup() throws Exception {

        //only for git action to work
        //use "if-statement" if you want to see Application tests not headless
//        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
//        }
    }
    @Start
    public void start(Stage stage) throws Exception {
        prim = stage;
        main = new LangUI();
        main.start(stage);
    }

    @AfterEach
    public void stop() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @Test
    public void testButton(FxRobot robot){
        FxAssert.verifyThat("#overview_btn", LabeledMatchers.hasText("Overview"));
        FxAssert.verifyThat("#add_btn", LabeledMatchers.hasText("Add Vocabulary"));
        FxAssert.verifyThat("#train_btn", LabeledMatchers.hasText("Vocabulary Trainer"));
        FxAssert.verifyThat("#exit_btn", LabeledMatchers.hasText("Exit"));
    }
    //TODO: have to inform how to get FXML Loader File to do the test in higher level
//    @Test
//    public void testSceneSwitch(FxRobot robot){
//
//
//        robot.clickOn("#add_btn");
//        WaitForAsyncUtils.waitForFxEvents(100);
//        FxAssert.verifyThat("#title", LabeledMatchers.hasText("Add new vocabulary"));
////        try {
////            FxAssert.verifyThat(main.getClass().getMethod("getScene()"),    );
////        } catch (NoSuchMethodException e) {
////            e.printStackTrace();
////        }
//    }

    @Test
    public void testAddInput(FxRobot robot) {
        robot.clickOn("#add_btn");
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
        robot.clickOn("#add_btn");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please select from and to language"));
    }

    @Test
    public void testAddInput2(FxRobot robot) {
        robot.clickOn("#add_btn");
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
        robot.clickOn("#add_btn");
        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
        robot.clickOn("#to_choice").dropTo("English").clickOn("English");
        robot.clickOn("#from_field").write("Apfel");
        robot.clickOn("#to_field").write("apple");
        robot.clickOn("#submit_btn");
        WaitForAsyncUtils.waitForFxEvents(100);
        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please fill out the Name of your Vocabulary group"));
    }
    //TODO: have to select right one
//    @Test
//    public void testAddInput4(FxRobot robot) {
//        robot.clickOn("#add_btn");
//        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
//        robot.clickOn("#to_choice").dropTo("German").clickOn("German");
//
//        WaitForAsyncUtils.waitForFxEvents(100);
//        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Please select another language"));
//    }
    //TODO: have to look how we could start the server
//    @Test
//    public void testAddInput5(FxRobot robot) {
//        robot.clickOn("#add_btn");
//        WaitForAsyncUtils.waitForFxEvents(100);
//        robot.clickOn("#from_choice").dropTo("German").clickOn("German");
//        robot.clickOn("#to_choice").dropTo("English").clickOn("English");
//        robot.clickOn("#category").write("Fruits");
//        WaitForAsyncUtils.waitForFxEvents(100);
//        robot.clickOn("#from_field").write("Apfel");
//        robot.clickOn("#to_field").write("apple");
//        robot.clickOn("#submit_btn");
//        WaitForAsyncUtils.waitForFxEvents(100);
//        FxAssert.verifyThat("#user_info", LabeledMatchers.hasText("Vocabulary added!"));
//    }


}