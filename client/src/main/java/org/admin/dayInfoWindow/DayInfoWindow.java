package org.admin.dayInfoWindow;

import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.getRequests.GetClient;
import org.admin.dayInfoWindow.dialog.AddNewClientDialog;
import org.admin.dayInfoWindow.dialog.PutAdminOnDateDialog;
import org.admin.dayInfoWindow.dialog.PutMasterOnDateDialog;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.enterpriseWindow.dialog.infos.ClientInfoDialog;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringClients;
import org.admin.sideMenu.SideMenu;
import org.admin.utils.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.collections.*;
import org.admin.utils.entities.Admin;
import org.admin.utils.entities.Client;

import java.util.*;
import java.time.*;

public class DayInfoWindow extends Main{
	public static StackPane loadWindow(LocalDate date){
            //TODO Добавить транзакции
        StackPane stackPane         = new StackPane();
        BorderPane root             = new BorderPane();

        StackPane sideMenuStack     = SideMenu.buildSideMenu(0);

        DatePicker miniCalendar		= new DatePicker();

        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        HBox sheduleHeaders			= new HBox();
        HBox rightSheduleHeaders	= new HBox();
        HBox leftSheduleHeaders		= new HBox();
        HBox centerInfo				= new HBox();
        HBox searchStringBox        = new HBox();

        Label adminLabel            = new Label("");

        Button totalSumBtn			= new Button();
        Button putMasterOnDayBtn    = new Button();
        Button putAdminOnDayBtn     = new Button();
        Button addNewClientBtn      = new Button();

        Region spacer               = new Region();

        Admin todayAdmin = GetAdmin.getByDate(token, date);
        if(todayAdmin == null){adminLabel.setText("Админ не назначен");}
        else {
                adminLabel.setText("Админ: " + todayAdmin);
        }
        addNewClientBtn.setMinHeight(30);
        addNewClientBtn.setMinWidth(120);
        addNewClientBtn.setPrefHeight(30);
        addNewClientBtn.setPrefWidth(120);

        centerBox.setAlignment(Pos.CENTER);
        searchStringBox.setAlignment(Pos.CENTER);
        rightBox.setMinWidth(MENU_WIDTH);
        totalSumBtn.setMinHeight(40);
        totalSumBtn.setMinWidth(150);
        putAdminOnDayBtn.setMinHeight(40);
        putAdminOnDayBtn.setMinWidth(150);
        putMasterOnDayBtn.setMinHeight(40);
        putMasterOnDayBtn.setMinWidth(150);
        miniCalendar.setMinHeight(40);
        miniCalendar.setMinWidth(200);

        // int totalSum = Connection.getTotalSum(token, date);
        // int cash = Connection.getCash(token, date);
        int totalSum = date.getYear();
        int cash = 1000;


        totalSumBtn.setText("Общее: " + totalSum);
        putMasterOnDayBtn.setText("Назначить мастера");
        putAdminOnDayBtn.setText("Назначить администратора");
        addNewClientBtn.setText("Добавить клиента");

        totalSumBtn.setWrapText(true);
        putAdminOnDayBtn.setWrapText(true);
        putMasterOnDayBtn.setWrapText(true);
        addNewClientBtn.setWrapText(true);

        totalSumBtn.setTextAlignment(TextAlignment.CENTER);
        putAdminOnDayBtn.setTextAlignment(TextAlignment.CENTER);
        putMasterOnDayBtn.setTextAlignment(TextAlignment.CENTER);
        addNewClientBtn.setTextAlignment(TextAlignment.CENTER);

        VBox.setMargin(sheduleHeaders, new Insets(100, 0, 0, 0));

        sheduleHeaders.setAlignment(Pos.CENTER);
        miniCalendar.setValue(date);

        HBox.setHgrow(spacer, Priority.ALWAYS);

        List<Client> clients = GetClient.getAll(token);
        VBox searchStringClients = SearchingStringClients.build(clients, client-> ClientInfoDialog.show(client.getId(), root));
        ScrollPane scrollTable = DayInfoTable.create(date);

//        TextArea textArea = new TextArea();
//        textArea.setWrapText(true);
//        textArea.setScrollLeft(Double.MAX_VALUE);

        searchStringBox.setSpacing(25);
        searchStringBox.setAlignment(Pos.TOP_CENTER);
        searchStringBox.setMaxSize(500, 100);
        searchStringBox.setPadding(new Insets(10, 0, 0, 0));

        miniCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            AdminInterface.loadDayInfoWindow(miniCalendar, newValue);
        });


        putMasterOnDayBtn.setOnAction(event -> PutMasterOnDateDialog.show(date, putMasterOnDayBtn));
        putAdminOnDayBtn.setOnAction(event -> PutAdminOnDateDialog.show(date, putAdminOnDayBtn));
        // сash.setOnAction(event -> showCashDialog());
        // totalSumBtn.setOnAction(event -> showDayTransactionsDialog());
        addNewClientBtn.setOnAction(event -> AddNewClientDialog.show(addNewClientBtn, date));

        stackPane.setAlignment(Pos.CENTER);
        StackPane.setAlignment(searchStringBox, Pos.TOP_CENTER);
        StackPane.setAlignment(root, Pos.CENTER);
        adminLabel.setWrapText(true);

        searchStringBox.getChildren().addAll(searchStringClients, addNewClientBtn, adminLabel);
        rightSheduleHeaders.getChildren().addAll(putAdminOnDayBtn, putMasterOnDayBtn, totalSumBtn);
        leftSheduleHeaders.getChildren().addAll(miniCalendar);
        sheduleHeaders.getChildren().addAll(leftSheduleHeaders, spacer, rightSheduleHeaders);
        centerInfo.getChildren().addAll(scrollTable);
        centerBox.getChildren().addAll(sheduleHeaders, centerInfo);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);

        stackPane.getChildren().addAll(root, searchStringBox);

        return stackPane;
    }
}






