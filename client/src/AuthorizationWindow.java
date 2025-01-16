package src;

import src.master.MasterInterface;
import src.Main;
import src.Connection;
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


public class AuthorizationWindow extends Main{
	public static VBox loadAuthorizationWindow(){
        VBox root                       = new VBox();
        HBox buttons                    = new HBox();
        GridPane table                  = new GridPane();
        
        Label headLbl                  = new Label();
        Label lblErr                   = new Label();
        Label loginLbl                 = new Label();
        Label passwordLbl              = new Label();
        
        TextField loginField           = new TextField();
        PasswordField passwordField    = new PasswordField();
        
        Button authorizationBtn        = new Button();


        root.setSpacing(20);
        buttons.setSpacing(30);

        lblErr.setText("");
        headLbl.setText("АВТОРИЗАЦИЯ");
        loginLbl.setText("Логин");
        passwordLbl.setText("Пароль");
        authorizationBtn.setText("Авторизация");

        root.setAlignment(Pos.CENTER);
        table.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);
        
        table.setVgap(15);
        table.setHgap(30);
    
        loginLbl.setTooltip(new Tooltip("Номер телефона"));
        
        loginField.setPrefColumnCount(20);
        passwordField.setPrefColumnCount(20);

        loginField.setPrefHeight(30);
        passwordField.setPrefHeight(30);

        authorizationBtn.setPrefWidth(170);
        authorizationBtn.setPrefHeight(40);

        
        authorizationBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login    = loginField.getText();
                String password = passwordField.getText();
                try{long k = Long.parseLong(login.substring(1));}
                catch(Exception ex){
                    lblErr.setText("Некорректный номер телефона. Пожалуйста проверьте правильность, он должен начинаться с +7");
                    return;
                }

                if(!login.startsWith("+7") || login.length() != 12){
                    lblErr.setText("Некорректный номер телефона. Пожалуйста проверьте правильность, он должен начинаться с +7");
                    return;
                }

                String[] checkResponse = Connection.checkAuthAndGetToken(login, password);
                if(checkResponse == null){
                    lblErr.setText("Ошибка подключения к серверу");
                    return;
                }
                if(checkResponse[0].equals("-1")){
                    lblErr.setText(checkResponse[1]);
                    return;
                }

                Main.token = checkResponse[0];
                Main.role = checkResponse[1];
                System.out.println(Main.role);
                if(Main.role.equals("master")){
                    MasterInterface.loadCalendarWindow(authorizationBtn);
                }
            }
        });


        table.add(loginLbl, 0, 0);
        table.add(passwordLbl, 0, 1);
        table.add(loginField, 1, 0);
        table.add(passwordField, 1, 1);
        
        root.getChildren().addAll(headLbl, table, lblErr, buttons);
        buttons.getChildren().addAll(authorizationBtn);
        return root;

    }
}