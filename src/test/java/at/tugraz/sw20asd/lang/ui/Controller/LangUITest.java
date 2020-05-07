package at.tugraz.sw20asd.lang.ui.Controller;
import javafx.stage.Stage;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;

import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;


@ExtendWith(ApplicationExtension.class)
class LangUITest extends LangUiTestBase {
    private Stage prim;
    private LangUI main;

    @Start
    public void start(Stage stage) throws Exception {
        prim = stage;
        main = new LangUI();
        main.start(stage);
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