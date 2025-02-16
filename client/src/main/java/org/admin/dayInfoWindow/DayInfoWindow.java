package org.admin.dayInfoWindow;

import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.Connection;
import org.admin.connection.getRequests.GetClient;
import org.admin.dayInfoWindow.dialog.AddNewClientDialog;
import org.admin.dayInfoWindow.dialog.AppointmentInfoDialog;
import org.admin.dayInfoWindow.dialog.ClientInfoDialog;
import org.admin.dayInfoWindow.dialog.PutMasterOnDateDialog;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.sideMenu.SideMenu;
import org.admin.utils.*;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.control.Alert.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.animation.*;
import javafx.collections.*;
import javafx.util.*;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

public class DayInfoWindow extends Main{
	public static BorderPane loadWindow(LocalDate date){
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

        Button totalSumBtn			= new Button();
        Button cashBtn 				= new Button();
        Button putMasterOnDayBtn    = new Button();
        Button putAdminOnDayBtn     = new Button();
        Button addNewClientBtn      = new Button();

        Region spacer               = new Region();

        ComboBox<String> comboBox   = new ComboBox<>();

        List<ClientInfo> clientsInfo = GetClient.getAll(token);
        if(clientsInfo == null){clientsInfo = new ArrayList<>();}

        comboBox.getEditor().setOnKeyReleased(new SearchingStringListenerClients(comboBox, clientsInfo));
        comboBox.setOnAction(event -> {
            String[] val = comboBox.getValue().split(" ");
            try{
                Long clientId = Long.parseLong(val[0]);
                ClientInfoDialog.show(clientId);
            }
            catch (Exception e){
                return;
            }
        });

        comboBox.setEditable(true);

        ObservableList fiosList = FXCollections.observableArrayList(); 
        for(ClientInfo client: clientsInfo){
            fiosList.add(client.toString());
        }
        comboBox.setItems(fiosList);

        comboBox.setPrefWidth(500);
        comboBox.setPrefHeight(30);
        addNewClientBtn.setMinHeight(30);
        addNewClientBtn.setMinWidth(100);        
        

        centerBox.setAlignment(Pos.CENTER);
        searchStringBox.setAlignment(Pos.CENTER);
        rightBox.setMinWidth(MENU_WIDTH);
        totalSumBtn.setMinHeight(40);
        totalSumBtn.setMinWidth(150);
        cashBtn.setMinHeight(40);
        cashBtn.setMinWidth(150);
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
        cashBtn.setText("Касса: " + cash);
        putMasterOnDayBtn.setText("Назначить мастера");
        putAdminOnDayBtn.setText("Назначить администратора");
        addNewClientBtn.setText("Добавить клиента");

        cashBtn.setWrapText(true);
        totalSumBtn.setWrapText(true);
        putAdminOnDayBtn.setWrapText(true);
        putMasterOnDayBtn.setWrapText(true);
        addNewClientBtn.setWrapText(true);

        cashBtn.setTextAlignment(TextAlignment.CENTER);
        totalSumBtn.setTextAlignment(TextAlignment.CENTER);
        putAdminOnDayBtn.setTextAlignment(TextAlignment.CENTER);
        putMasterOnDayBtn.setTextAlignment(TextAlignment.CENTER);
        addNewClientBtn.setTextAlignment(TextAlignment.CENTER);

        VBox.setMargin(sheduleHeaders, new Insets(100, 0, 0, 0));

        sheduleHeaders.setAlignment(Pos.CENTER);
        // sheduleHeaders.setSpacing(300);
        miniCalendar.setValue(date);

        HBox.setHgrow(spacer, Priority.ALWAYS);

        ScrollPane scrollTable = DayInfoTable.create(date);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setScrollLeft(Double.MAX_VALUE);

        searchStringBox.setSpacing(25);

        miniCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            AdminInterface.loadDayInfoWindow(miniCalendar, newValue);
	    });


        putMasterOnDayBtn.setOnAction(event -> PutMasterOnDateDialog.show(date));
        // putAdminOnDayBtn.setOnAction(event -> showPutAdminOnDayDialog());
        // сash.setOnAction(event -> showCashDialog());
        // totalSumBtn.setOnAction(event -> showDayTransactionsDialog());
        addNewClientBtn.setOnAction(event -> AddNewClientDialog.show(addNewClientBtn, date));

        searchStringBox.getChildren().addAll(comboBox, addNewClientBtn);
        rightSheduleHeaders.getChildren().addAll(putAdminOnDayBtn, putMasterOnDayBtn, cashBtn, totalSumBtn);
        leftSheduleHeaders.getChildren().addAll(miniCalendar);
        sheduleHeaders.getChildren().addAll(leftSheduleHeaders, spacer, rightSheduleHeaders);
        centerInfo.getChildren().addAll(scrollTable, textArea);
        centerBox.getChildren().addAll(searchStringBox, sheduleHeaders, centerInfo);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
	}
}






