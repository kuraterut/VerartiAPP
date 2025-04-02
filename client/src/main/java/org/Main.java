package org;

import org.admin.UI.window.authorizationWindow.AuthorizationWindow;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.layout.*;

import java.io.*;
import java.util.*;
import java.time.*;

//INSERT INTO users (name, surname, patronymic, password_hash, email, phone) VALUES ('Ilia', 'Kurylin', 'Artemovich', '6e73766e6a6e75347538393438767568323968726866656276383339346876756233758758c5deb39e2e1c2077a3999e1cc77b2ed109ea', 'kuraterut@yandex.ru', '+79092762462');


public class Main extends Application{
    public static Properties properties;
    //TODO Сделать проверку токена везде.
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
