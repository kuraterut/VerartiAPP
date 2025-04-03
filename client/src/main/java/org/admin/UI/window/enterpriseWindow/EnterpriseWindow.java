package org.admin.UI.window.enterpriseWindow;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.Main;
import org.admin.connection.getRequests.*;
import org.admin.UI.window.enterpriseWindow.dialog.creation.*;

import org.admin.UI.window.enterpriseWindow.dialog.infos.*;

import org.admin.UI.components.searchingStrings.*;
import org.admin.UI.components.sideMenu.SideMenu;
import org.admin.model.*;

import java.util.List;

public class EnterpriseWindow extends Main {
    public static BorderPane loadEnterpriseWindow() {
        BorderPane root = new BorderPane();

        StackPane sideMenuStack     = SideMenu.buildSideMenu(4);
        VBox sideMenuBox            = new VBox(sideMenuStack);

        Label headLabel = new Label("Управление предприятием");
//        sideMenuBox.setAlignment(Pos.CENTER);
        sideMenuBox.setMaxWidth(MENU_WIDTH);
        sideMenuBox.setPrefWidth(MENU_WIDTH);
        StackPane.setAlignment(sideMenuBox, Pos.CENTER_LEFT);
//        sideMenuBox.setMaxHeight(Double.MAX_VALUE);
//        sideMenuBox.setPrefHeight(Double.MAX_VALUE);

        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        rightBox.setMinWidth(MENU_WIDTH);



        List<User> admins = GetUser.getAllAdmins(token);
        List<User> masters = GetUser.getAllMasters(token);
        List<Option> options = GetOption.getAll(token);
        List<Client> clients = GetClient.getAll(token);


        HBox enterpriseInfoBox = new HBox();
        enterpriseInfoBox.setAlignment(Pos.CENTER);
        enterpriseInfoBox.setSpacing(10);

        VBox adminsBox = new VBox();
        Label adminsLabel = new Label("Админы");
        Button addNewAdminButton = new Button("Добавить Админа");
        VBox searchingStringsAdmins = SearchingStringAdmins.build(admins, admin->AdminInfoDialog.show(admin.getId(), root));
        adminsBox.setAlignment(Pos.CENTER);
        adminsBox.setSpacing(20);
        adminsBox.getChildren().addAll(adminsLabel, addNewAdminButton, searchingStringsAdmins);

        VBox mastersBox = new VBox();
        Label mastersLabel = new Label("Мастера");
        Button addNewMasterButton = new Button("Добавить Мастера");
        VBox searchingStringsMasters = SearchingStringMasters.build(masters, master-> MasterInfoDialog.show(master.getId(), root));
        mastersBox.setAlignment(Pos.CENTER);
        mastersBox.setSpacing(20);
        mastersBox.getChildren().addAll(mastersLabel, addNewMasterButton, searchingStringsMasters);

        VBox servicesBox = new VBox();
        Label servicesLabel = new Label("Услуги");
        Button addNewServiceButton = new Button("Добавить Услугу");
        VBox searchingStringsServices = SearchingStringOptions.build(options, service-> OptionInfoDialog.show(service.getId(), root));
        servicesBox.setAlignment(Pos.CENTER);
        servicesBox.setSpacing(20);
        servicesBox.getChildren().addAll(servicesLabel, addNewServiceButton, searchingStringsServices);

        VBox clientsBox = new VBox();
        Label clientsLabel = new Label("Клиенты");
        Button addNewClientButton = new Button("Добавить Клиента");
        VBox searchingStringsClients = SearchingStringClients.build(clients, client-> ClientInfoDialog.show(client.getId(), root));
        clientsBox.setAlignment(Pos.CENTER);
        clientsBox.setSpacing(20);
        clientsBox.getChildren().addAll(clientsLabel, addNewClientButton, searchingStringsClients);


        addNewAdminButton.setOnAction(event -> CreateAdminDialog.show(addNewAdminButton));
        addNewMasterButton.setOnAction(event -> CreateMasterDialog.show(addNewMasterButton));
        addNewServiceButton.setOnAction(event -> CreateOptionDialog.show(addNewServiceButton));
        addNewClientButton.setOnAction(event -> CreateClientDialog.show(addNewClientButton));

        enterpriseInfoBox.getChildren().addAll(adminsBox, mastersBox, servicesBox, clientsBox);
        centerBox.getChildren().addAll(headLabel, enterpriseInfoBox);

        root.setCenter(centerBox);
        root.setRight(rightBox);
        root.setLeft(sideMenuStack);

        return root;
    }
}
