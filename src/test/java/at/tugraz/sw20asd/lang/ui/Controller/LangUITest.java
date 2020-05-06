package at.tugraz.sw20asd.lang.ui.Controller;
import javafx.stage.Stage;

import javafx.stage.Window;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
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
        FxToolkit.hideStage();
    }

    @Test
    public void testButton(){
        FxAssert.verifyThat("#overview_btn", LabeledMatchers.hasText("Overview"));
        FxAssert.verifyThat("#add_btn", LabeledMatchers.hasText("Add Vocabulary"));
        FxAssert.verifyThat("#train_btn", LabeledMatchers.hasText("Vocabulary Trainer"));
        FxAssert.verifyThat("#exit_btn", LabeledMatchers.hasText("Exit"));
    }

    @Test
    public void testScene(FxRobot robot){
        robot.clickOn("#add_btn" );
        FxAssert.verifyThat("#title", LabeledMatchers.hasText("Add new vocabulary"));
    }

}