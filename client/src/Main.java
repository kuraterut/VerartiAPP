package src;

import src.AuthorizationWindow;

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

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.time.*;



public class Main extends Application{
    public static String role;
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
        stage.setWidth(1920);
        stage.setHeight(1080);
        String style = (getClass().getResource("/test.css")).toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);    
         
        stage.show();
    }
}
