package org.admin.enterpriseWindow;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.Main;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.getRequests.GetClient;
import org.admin.connection.getRequests.GetMaster;
import org.admin.connection.getRequests.GetService;
import org.admin.dayInfoWindow.dialog.ClientInfoDialog;
import org.admin.enterpriseWindow.infos.AdminInfoDialog;
import org.admin.enterpriseWindow.infos.MasterInfoDialog;
import org.admin.enterpriseWindow.infos.ServiceInfoDialog;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringAdmins;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringClients;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringMasters;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringServices;
import org.admin.sideMenu.SideMenu;
import org.admin.utils.AdminInfo;
import org.admin.utils.ClientInfo;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;

import java.util.List;

public class EnterpriseWindow extends Main {
    public static BorderPane loadEnterpriseWindow() {
        BorderPane root = new BorderPane();

        StackPane sideMenuStack     = SideMenu.buildSideMenu(5);
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

        HBox searchingStringsBox = new HBox();
        searchingStringsBox.setAlignment(Pos.CENTER);
        searchingStringsBox.setSpacing(10);

        List<AdminInfo> admins = GetAdmin.getAll(token);
        List<MasterInfo> masters = GetMaster.getAll(token);
        List<ServiceInfo> services = GetService.getAll(token);
        List<ClientInfo> clients = GetClient.getAll(token);

        VBox searchingStringsAdmins = SearchingStringAdmins.build(admins, admin->AdminInfoDialog.show(admin.getId()));
        VBox searchingStringsMasters = SearchingStringMasters.build(masters, master-> MasterInfoDialog.show(master.getId()));
        VBox searchingStringsServices = SearchingStringServices.build(services, service-> ServiceInfoDialog.show(service.getId()));
        VBox searchingStringsClients = SearchingStringClients.build(clients, client-> ClientInfoDialog.show(client.getId()));

        searchingStringsBox.getChildren().addAll(   searchingStringsAdmins, searchingStringsMasters,
                                                    searchingStringsServices, searchingStringsClients);
        centerBox.getChildren().addAll(headLabel, searchingStringsBox);

        root.setCenter(centerBox);
        root.setRight(rightBox);
        root.setLeft(sideMenuStack);

        return root;
    }
}
