package at.tugraz.sw20asd.lang.ui.Controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class LangUITest extends ApplicationTest {

//    FXMLLoader loader = new FXMLLoader();

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainRoot = FXMLLoader.load(getClass().getResource("/main.fxml"));
        stage.setScene(new Scene(mainRoot));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws Exception{

    }

    @After
    public void tearDown() throws Exception{
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testButton(){
        clickOn("#overview_btn");
//        assertThat()
    }
}