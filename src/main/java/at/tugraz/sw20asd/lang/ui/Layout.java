package at.tugraz.sw20asd.lang.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Layout extends TabPane {

    public Layout(){
        this.setDisable(false);

        Tab addTab = new Tab("Add vocabulary");
        Tab OverviewTab = new Tab("Overview");


        getTabs().add(addTab);
        getTabs().add(OverviewTab);

        addTab.setContent(new AddVocab());
        OverviewTab.setContent(new Overview());

        this.getSelectionModel().select(addTab);
    }
}
