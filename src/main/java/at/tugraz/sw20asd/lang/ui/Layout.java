package at.tugraz.sw20asd.lang.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Layout extends TabPane {

    public Layout(){
        this.setDisable(false);

        Tab addTab = new Tab("Add vocabulary");


        getTabs().add(addTab);

        addTab.setContent(new AddVocab());

        this.getSelectionModel().select(addTab);
    }
}
