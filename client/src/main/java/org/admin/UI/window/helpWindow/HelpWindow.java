package org.admin.UI.window.helpWindow;

import org.Main;
import org.admin.UI.components.sideMenu.SideMenu;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;


public class HelpWindow extends Main{
	public static BorderPane loadHelpWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(4);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Помощь");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }
}