package at.tugraz.sw20asd.lang.ui;

import at.tugraz.sw20asd.lang.ui.Controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import java.io.IOException;

import static org.loadui.testfx.Assertions.assertNodeExists;
@Category(TestFX.class)
public class SimpleGuiTest extends GuiTest {

    Parent root;
    @Override
    protected Parent getRootNode() {
        stage.setTitle("Vocabulary Trainer");

        root = new Controller();
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        return root;
    }

    @Test
    public void ClickButton(){
        Button exit = find("#exit_btn");
//            System.out.println("not null ----------------------\n");
////        click(exit);
////        assertNodeExists("#exit_btn");
    }
}
