package at.tugraz.sw20asd.lang.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Layout extends TabPane {

    public Layout(){
        this.setDisable(false);

        Tab addTab = new Tab("Add vocabulary");
        Tab addTab2 = new Tab("Overview");


        getTabs().add(addTab);
        getTabs().add(addTab2);

        addTab.setContent(new AddVocab());
        addTab2.setContent(new Overview());

        this.getSelectionModel().select(addTab);
    }
}
