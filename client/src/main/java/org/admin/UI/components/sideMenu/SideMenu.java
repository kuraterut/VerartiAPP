package org.admin.UI.components.sideMenu;

import org.Main;
import org.admin.controller.AdminController;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.*;

import java.io.*;
import java.util.*;
import java.time.LocalDate;


public class SideMenu extends Main{
	public static void toggleMenu(VBox sideMenu) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), sideMenu);        
        if (Main.isMenuVisible) {
            transition.setToX(0);
            sideMenu.setVisible(true);
            sideMenu.setOpacity(0.0);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.1), sideMenu);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        } 

        else {
            transition.setToX(-(MENU_WIDTH));
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.1), sideMenu);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event->sideMenu.setVisible(false));
            fadeOut.play();
        }
        transition.play();
        Main.isMenuVisible = !Main.isMenuVisible;
    }


    public static StackPane buildSideMenu(int state) {
        Main.isMenuVisible          = true;
        Image menuIconImg           = null;

        try{
            menuIconImg             = new Image(new FileInputStream(properties.getProperty("path.sidemenu-icon")));
        }
        catch(Exception ex)         {System.out.println(ex);}

        StackPane sideMenuStack     = new StackPane();
        VBox sideMenuBox            = new VBox();
        ImageView menuIcon          = new ImageView(menuIconImg);
        LinkedList<ButtonBase>  sideMenuBtnsList = new LinkedList<>();

        ButtonBase[] mainSideMenuBtn = new ButtonBase[5];
        mainSideMenuBtn[0] = new Button();
        mainSideMenuBtn[1] = new Button();
        mainSideMenuBtn[2] = new Button();
        mainSideMenuBtn[3] = new Button();
        mainSideMenuBtn[4] = new Button();

        Button toggleButton = new Button();
        
        for (int i = 0; i < mainSideMenuBtn.length; i++){
            sideMenuBtnsList.add(mainSideMenuBtn[i]);
            mainSideMenuBtn[i].setPrefWidth(Double.MAX_VALUE);
            mainSideMenuBtn[i].setPrefHeight(SIDEMENU_BTN_HEIGHT);
        }
        mainSideMenuBtn[0].setText("Главная");
        mainSideMenuBtn[1].setText("Товары");
        mainSideMenuBtn[2].setText("Календарь сотрудников");
        mainSideMenuBtn[3].setText("Профиль");
        mainSideMenuBtn[4].setText("Предприятие");
        VBox.setMargin(mainSideMenuBtn[0], new Insets(100, 0, 0, 0));

        mainSideMenuBtn[state].setStyle("-fx-background-color: grey");

        sideMenuBox.setVisible(false);
        sideMenuBox.setSpacing(5);
        sideMenuBox.setPrefWidth(Main.MENU_WIDTH);
        sideMenuBox.setTranslateX(-(MENU_WIDTH));
        sideMenuBox.setAlignment(Pos.TOP_CENTER);
        sideMenuBox.getStyleClass().add("side-menu");
        StackPane.setAlignment(sideMenuBox, Pos.CENTER_LEFT);

        toggleButton.setGraphic(menuIcon);
        StackPane.setAlignment(toggleButton, Pos.TOP_LEFT);
        toggleButton.setOnAction(event -> toggleMenu(sideMenuBox));

        menuIcon.setFitWidth(60);
        menuIcon.setFitHeight(60);

        mainSideMenuBtn[0].setOnAction(event -> AdminController.loadDayInfoWindow(mainSideMenuBtn[0], LocalDate.now()));
        mainSideMenuBtn[1].setOnAction(event -> AdminController.loadProductsWindow(mainSideMenuBtn[1]));
        mainSideMenuBtn[2].setOnAction(event -> AdminController.loadCalendarOfEmployeesWindow(mainSideMenuBtn[2]));
        mainSideMenuBtn[3].setOnAction(event -> AdminController.loadProfileWindow(mainSideMenuBtn[3]));
        mainSideMenuBtn[4].setOnAction(event -> AdminController.loadEnterpriseWindow(mainSideMenuBtn[4]));




        for(int i = 0; i < sideMenuBtnsList.size(); i++){sideMenuBox.getChildren().add(sideMenuBtnsList.get(i));}
        sideMenuStack.getChildren().addAll(sideMenuBox, toggleButton);

        return sideMenuStack;
    }
}