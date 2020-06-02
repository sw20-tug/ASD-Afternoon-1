package at.tugraz20.sw20asd.lang.ui.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

//please extend from this class for gui tests
public class LangUiTestBase {

    @BeforeAll
    public static void init(){
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            System.loadLibrary("WindowsCodecs");
        }
    }

    @AfterEach
    public void stop() throws TimeoutException {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
    }
}
