package src.admin.helpWindow;

import src.Main;
import src.admin.AdminInterface;
import src.admin.connection.Connection;
import src.admin.sideMenu.SideMenu;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.control.Alert.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.animation.*;
import javafx.collections.*;
import javafx.util.*;

import java.sql.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class HelpWindow extends Main{
	public static BorderPane loadHelpWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(3);
        
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