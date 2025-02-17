package org.admin.dayInfoWindow;

import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.getRequests.GetClient;
import org.admin.dayInfoWindow.dialog.AddNewClientDialog;
import org.admin.dayInfoWindow.dialog.ClientInfoDialog;
import org.admin.dayInfoWindow.dialog.PutMasterOnDateDialog;
import org.admin.dayInfoWindow.searchingStrings.SearchingStringClient;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.sideMenu.SideMenu;
import org.admin.utils.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.collections.*;

import java.util.*;
import java.time.*;

public class DayInfoWindow extends Main{
	public static StackPane loadWindow(LocalDate date){
        StackPane stackPane = new StackPane();
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
        addNewClientBtn.setMinWidth(120);
        addNewClientBtn.setPrefHeight(30);
        addNewClientBtn.setPrefWidth(120);
        

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

            VBox searchStringClients = SearchingStringClient.build();
            searchStringBox.getChildren().addAll(searchStringClients, addNewClientBtn);
            rightSheduleHeaders.getChildren().addAll(putAdminOnDayBtn, putMasterOnDayBtn, cashBtn, totalSumBtn);
            leftSheduleHeaders.getChildren().addAll(miniCalendar);
            sheduleHeaders.getChildren().addAll(leftSheduleHeaders, spacer, rightSheduleHeaders);
            centerInfo.getChildren().addAll(scrollTable, textArea);
            centerBox.getChildren().addAll(sheduleHeaders, centerInfo);
            root.setLeft(sideMenuStack);
            root.setCenter(centerBox);
            root.setRight(rightBox);
            stackPane.setAlignment(Pos.CENTER);
            StackPane.setAlignment(searchStringBox, Pos.TOP_CENTER);
            StackPane.setAlignment(searchStringClients, Pos.TOP_CENTER);
            StackPane.setAlignment(addNewClientBtn, Pos.TOP_CENTER);
            StackPane.setAlignment(root, Pos.TOP_CENTER);
            stackPane.getChildren().addAll(root, searchStringBox);
            searchStringBox.setPadding(new Insets(10, 0, 0, 0));
            searchStringBox.setAlignment(Pos.TOP_CENTER);

//            root.setPadding(new Insets(0, 0, 0, 0));
            searchStringBox.setMaxSize(500, 100);
            return stackPane;
    }
}






