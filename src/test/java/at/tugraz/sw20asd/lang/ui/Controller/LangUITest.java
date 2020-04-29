package at.tugraz.sw20asd.lang.ui.Controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(ApplicationExtension.class)
class LangUITest {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }
    @Start
    public void start(Stage stage) throws Exception {
        new LangUI().start(stage);
    }



    @Test
    public void testButton(FxRobot robot){
//        verifyThat("#overview_btn", hasText("Overview"));
        robot.clickOn("#overview_btn");
        FxAssert.verifyThat("#overview_btn", LabeledMatchers.hasText("Overview"));
        FxAssert.verifyThat("#add_btn", LabeledMatchers.hasText("Add Vocabulary"));
        FxAssert.verifyThat("#train_btn", LabeledMatchers.hasText("Vocabulary Trainer"));
        FxAssert.verifyThat("#exit_btn", LabeledMatchers.hasText("Exit"));
//        assertThat()
    }

    //    @BeforeAll
//    public void setUp() throws Exception{
//        FxToolkit.registerPrimaryStage();
//    }
//
//    @AfterAll
//    public void tearDown() throws Exception{
//        FxToolkit.hideStage();
//
////        release(new KeyCode[]{});
////        release(new MouseButton[]{});
//    }
}