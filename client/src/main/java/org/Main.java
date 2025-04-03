package org;

import org.admin.UI.window.authorizationWindow.AuthorizationWindow;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.layout.*;

import java.io.*;
import java.util.*;
import java.time.*;

public class Main extends Application{
    public static Properties properties;
    public static String login;
    public static String token; 
    public static GridPane calendar;
    public static boolean isMenuVisible = true;
    
    public static int year;
    public static int month;

    public static final double MENU_WIDTH = 250; // Ширина меню
    public static final double SIDEMENU_BTN_HEIGHT = 75;

    public static void main(String[] args) throws FileNotFoundException{
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        year = LocalDate.now().getYear();
        month = LocalDate.now().getMonthValue()-1;
        
        
        Scene scene = new Scene(AuthorizationWindow.loadAuthorizationWindow());
        
        stage.setTitle("VerartiAPP");
        stage.setMaximized(true);
        stage.setFullScreen(false);

        getProperties();

        String style = (Main.class.getClassLoader().getResource("test.css")).toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);    
         
        stage.show();
    }

    public void getProperties() {
        this.properties = new Properties();
        try (FileInputStream input = new FileInputStream("client/src/main/resources/application.properties")) {
            this.properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
