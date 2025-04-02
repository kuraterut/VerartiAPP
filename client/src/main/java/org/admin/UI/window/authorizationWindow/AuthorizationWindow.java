package org.admin.UI.window.authorizationWindow;

import org.admin.connection.Connection;
import org.Main;
import org.admin.model.AuthResponse;
import org.admin.utils.UserRole;
import org.admin.utils.validation.PhoneNumberValidation;
import org.admin.utils.validation.Validation;
import org.admin.controller.AdminController;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.time.*;


public class AuthorizationWindow extends Main {
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
        headLbl.setText("АВТОРИЗАЦИЯ АДМИНИСТРАТОРА");
        loginLbl.setText("Телефон");
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

        
        authorizationBtn.setOnAction(event ->  {
                String login    = loginField.getText();
                String password = passwordField.getText();

                Validation phoneNumberValidation = new PhoneNumberValidation(login);
                if(!phoneNumberValidation.validate()){
                    lblErr.setText("Некорректный номер телефона. Пожалуйста проверьте правильность.");
                    return;
                }

                AuthResponse authResponse = Connection.checkAuthAndGetToken(login, password, UserRole.ADMIN);

                if(authResponse.getCode() != 200){
                    lblErr.setText(authResponse.getMsg());
                    return;
                }

                Main.token = authResponse.getAuthToken();
                Main.login = login;
                System.out.println(token);

                AdminController.loadDayInfoWindow(authorizationBtn, LocalDate.now());
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