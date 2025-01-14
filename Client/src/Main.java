import javafx.application.Application;
import javafx.stage.*;


import java.io.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;


import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.control.Alert.AlertType;

import javafx.scene.Node;

import javafx.scene.input.MouseEvent;    

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.paint.Color;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;

import javafx.collections.*;

import javafx.util.Duration;

import java.sql.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.net.*;
import java.awt.*;



public class Main extends Application{
    String role;
    String token; 
    GridPane calendar;
    boolean isMenuVisible = true;

    static int year;
    static int month;

    private static final double MENU_WIDTH = 250; // Ширина меню
    private static final double SIDEMENU_BTN_HEIGHT = 75;

    public static void main(String[] args) throws FileNotFoundException{
        launch(args);
    }

    private GridPane buildCalendarByYM(int year, int month){
        GridPane calendar           = new GridPane();
        
        int lastDayOfMonth          = YearMonth.of(year, month).lengthOfMonth();
        String yearStr              = String.valueOf(year);
        String monthStr             = String.valueOf(month);
        String lastDayOfMonthStr    = String.valueOf(lastDayOfMonth);
        int firstDayInWeek          = HelpFuncs.getDayOfWeekByStr(yearStr+"-"+monthStr+"-01");
        int lastDayInWeek           = HelpFuncs.getDayOfWeekByStr(yearStr+"-"+monthStr+"-"+lastDayOfMonthStr); 
        
        Label[] daysOfWeekArr = {new Label("ПН"), new Label("ВТ"), new Label("СР"), new Label("ЧТ"), 
                                new Label("ПТ"), new Label("СБ"), new Label("ВС")};
        
        for (int i = 0; i < 7; i++){
            GridPane.setHalignment(daysOfWeekArr[i], HPos.CENTER);
            GridPane.setValignment(daysOfWeekArr[i], VPos.CENTER);
            calendar.add(daysOfWeekArr[i], i, 0);
        }
        
        calendar.setAlignment(Pos.CENTER);

        calendar.setGridLinesVisible(true);
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));
        calendar.getColumnConstraints().add(new ColumnConstraints(150));

        calendar.getRowConstraints().add(new RowConstraints(50));
        
        int numCell = 0;
        ArrayList<String[]> timetableInfo = Connection.getTimetableByYM(year, month, token);


        int index = 1;
        boolean flag = true;
        for (int i = 1; i < 7; i++){
            if(flag){
                for (int j = firstDayInWeek-1; j < 7; j++){
                    Rectangle rect = new Rectangle(150, 100, Color.TRANSPARENT); // Прозрачный
                    Label indexLbl = new Label(String.valueOf(index));
                    
                    Label cellMiniInfo = new Label();

                    String[] infoYM = null;
                    if(timetableInfo == null){
                        infoYM = new String[2];
                        infoYM[0] = "Ошибка";
                        infoYM[1] = "Ошибка";
                    }
                    else{
                        infoYM = timetableInfo.get(numCell);    
                    }
                    numCell++;
                    cellMiniInfo.setText("Кол-во: " + infoYM[0] + "\nВремя работы:\n" + infoYM[1]);

                    
                    final int roww = i;
                    final int coll = j;

                    int numCellHelp = numCell;
                    rect.setOnMouseClicked(event -> showCalendarCellInfoDialog(year, month, numCellHelp));
                    rect.setOnMouseEntered(event -> {
                        rect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                    });
                    rect.setOnMouseExited(event -> rect.setStyle(""));

                    calendar.add(indexLbl, j, i);
                    calendar.add(cellMiniInfo, j, i);
                    calendar.add(rect, j, i);

                    GridPane.setHalignment(indexLbl, HPos.LEFT);
                    GridPane.setValignment(indexLbl, VPos.TOP);
                    GridPane.setHalignment(cellMiniInfo, HPos.CENTER);
                    GridPane.setValignment(cellMiniInfo, VPos.CENTER);
                    
                    
                    index++;
                }
                calendar.getRowConstraints().add(new RowConstraints(100));
                flag = false;
            }
            else{
                for (int j = 0; j < 7 && index <= lastDayOfMonth; j++){
                    Rectangle rect = new Rectangle(150, 100, Color.TRANSPARENT);
                    Label indexLbl = new Label(String.valueOf(index));

                    Label cellMiniInfo = new Label();

                    String[] infoYM = null;
                    if(timetableInfo == null){
                        infoYM = new String[2];
                        infoYM[0] = "Ошибка";
                        infoYM[1] = "Ошибка";
                    }
                    else{
                        infoYM = timetableInfo.get(numCell);    
                    }
                    numCell++;
                    cellMiniInfo.setText("Кол-во: " + infoYM[0] + "\nВремя работы:\n" + infoYM[1]);

                    final int roww = i;
                    final int coll = j;

                    int numCellHelp = numCell;
                    rect.setOnMouseClicked(event -> showCalendarCellInfoDialog(year, month, numCellHelp));
                    rect.setOnMouseEntered(event -> {
                        rect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                    });
                    rect.setOnMouseExited(event -> rect.setStyle(""));

                    calendar.add(indexLbl, j, i);
                    calendar.add(cellMiniInfo, j, i);
                    calendar.add(rect, j, i);
                    
                    GridPane.setHalignment(indexLbl, HPos.LEFT);
                    GridPane.setValignment(indexLbl, VPos.TOP);
                    GridPane.setHalignment(cellMiniInfo, HPos.CENTER);
                    GridPane.setValignment(cellMiniInfo, VPos.CENTER);
                    
                    index++;
                }
                calendar.getRowConstraints().add(new RowConstraints(100));
                if(index > lastDayOfMonth){break;}
            }   
        }

        return calendar;
    }

    

    private void showCalendarCellInfoDialog(int year, int month, int day) {
        String monthStr = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")); // Вернёт Январь

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о дне");
        
        Label infoLabel = new Label((day) + " " + monthStr + " " + year + " года");

        ArrayList<String[]> arrGuestsInfo = Connection.getTimetableByDate(year, month, day, token);
        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableGuestsInfo = new GridPane();
        
        Label nameHead = new Label("Имя посетителя");
        Label timeHead = new Label("Время");
        Label serviceHead = new Label("Услуга");

        tableGuestsInfo.addRow(0, nameHead, timeHead, serviceHead);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(timeHead, HPos.CENTER);
        GridPane.setValignment(timeHead, VPos.CENTER);
        GridPane.setHalignment(serviceHead, HPos.CENTER);
        GridPane.setValignment(serviceHead, VPos.CENTER);

        if(arrGuestsInfo == null){
            String[] errorStrArr = new String[]{"Ошибка", "Ошибка", "Ошибка"};
            arrGuestsInfo = new ArrayList<>();
            arrGuestsInfo.add(errorStrArr);
        }

        for (int i = 0; i < arrGuestsInfo.size(); i++){
            Label name = new Label(arrGuestsInfo.get(i)[0]);
            Label time = new Label(arrGuestsInfo.get(i)[1]);
            Label service = new Label(arrGuestsInfo.get(i)[2]);
            tableGuestsInfo.addRow(i+1, name, time, service);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(time, HPos.CENTER);
            GridPane.setValignment(time, VPos.CENTER);
            GridPane.setHalignment(service, HPos.CENTER);
            GridPane.setValignment(service, VPos.CENTER);
        }

        tableGuestsInfo.getColumnConstraints().add(new ColumnConstraints(150));
        tableGuestsInfo.getColumnConstraints().add(new ColumnConstraints(150));

        tableGuestsInfo.setAlignment(Pos.CENTER);
        tableGuestsInfo.setGridLinesVisible(true);

        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableGuestsInfo);
        

        
        Button button1 = new Button("Назад");
        button1.setOnAction(e -> {
            dialog.close();
        });


        VBox dialogLayout = new VBox(100);
        dialogLayout.setAlignment(Pos.CENTER);
        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        // btns.setSpacing(50);
        btns.getChildren().addAll(button1);
        dialogLayout.getChildren().addAll(infoLabel, scrollTable, btns);
        Scene dialogScene = new Scene(dialogLayout, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void toggleMenu(VBox sideMenu) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), sideMenu);        
        if (isMenuVisible) {
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
        isMenuVisible = !isMenuVisible;
    }


    private StackPane buildMasterSideMenu(int state){
        isMenuVisible               = true;
        Image menuIconImg           = null;
        try{menuIconImg             = new Image(new FileInputStream("photos/Menu-ICON.png"));}
        catch(Exception ex)         {System.out.println(ex);}

        StackPane sideMenuStack     = new StackPane();
        VBox sideMenuBox            = new VBox();
        ImageView menuIcon          = new ImageView(menuIconImg);
        LinkedList<ButtonBase>  sideMenuBtnsList = new LinkedList<>();

        ButtonBase[] mainSideMenuBtn = new ButtonBase[5];
        mainSideMenuBtn[0] = new Button();
        mainSideMenuBtn[1] = new ToggleButton();
        mainSideMenuBtn[2] = new Button();
        mainSideMenuBtn[3] = new Button();
        mainSideMenuBtn[4] = new Button();

        Button toggleButton = new Button();
        
        for (int i = 0; i < mainSideMenuBtn.length; i++){
            sideMenuBtnsList.add(mainSideMenuBtn[i]);
            mainSideMenuBtn[i].setPrefWidth(Double.MAX_VALUE);
            mainSideMenuBtn[i].setPrefHeight(SIDEMENU_BTN_HEIGHT);
        }
        mainSideMenuBtn[0].setText("Календарь");
        mainSideMenuBtn[1].setText("Ресурсы");
        mainSideMenuBtn[2].setText("Профиль");
        mainSideMenuBtn[3].setText("Услуги");
        mainSideMenuBtn[4].setText("Помощь");
        VBox.setMargin(mainSideMenuBtn[0], new Insets(100, 0, 0, 0));

        mainSideMenuBtn[state].setStyle("-fx-background-color: grey");

        sideMenuBox.setVisible(false);
        sideMenuBox.setSpacing(5);
        sideMenuBox.setPrefWidth(MENU_WIDTH);
        sideMenuBox.setTranslateX(-(MENU_WIDTH));
        sideMenuBox.setAlignment(Pos.TOP_CENTER);
        sideMenuBox.getStyleClass().add("side-menu");
        StackPane.setAlignment(sideMenuBox, Pos.CENTER_LEFT);

        toggleButton.setGraphic(menuIcon);
        StackPane.setAlignment(toggleButton, Pos.TOP_LEFT);
        toggleButton.setOnAction(event -> toggleMenu(sideMenuBox));

        menuIcon.setFitWidth(60);
        menuIcon.setFitHeight(60);

        mainSideMenuBtn[0].setOnAction(event -> HelpFuncs.loadMasterCalendarWindowFunc(mainSideMenuBtn[0], this));
        mainSideMenuBtn[2].setOnAction(event -> HelpFuncs.loadMasterProfileWindowFunc(mainSideMenuBtn[2], this));
        mainSideMenuBtn[3].setOnAction(event -> HelpFuncs.loadMasterServicesWindowFunc(mainSideMenuBtn[3], this));
        mainSideMenuBtn[4].setOnAction(event -> HelpFuncs.loadMasterHelpWindowFunc(mainSideMenuBtn[4], this));

        Main main = this;
        ToggleButton resourcesBtn = (ToggleButton)mainSideMenuBtn[1];
        resourcesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (resourcesBtn.isSelected()){
                    Button listBtn = new Button("Список");
                    Button requestsBtn = new Button("Запросы");

                    listBtn.setOnAction(e -> HelpFuncs.loadMasterResourcesListWindowFunc(listBtn, main));
                    requestsBtn.setOnAction(e -> HelpFuncs.loadMasterResourcesRequestsWindowFunc(requestsBtn, main));

                    listBtn.setPrefWidth(MENU_WIDTH-30);
                    requestsBtn.setPrefWidth(MENU_WIDTH-30);
                    listBtn.setPrefHeight(SIDEMENU_BTN_HEIGHT-30);
                    requestsBtn.setPrefHeight(SIDEMENU_BTN_HEIGHT-30);

                    sideMenuBtnsList.add(sideMenuBtnsList.indexOf(resourcesBtn)+1, requestsBtn);
                    sideMenuBtnsList.add(sideMenuBtnsList.indexOf(resourcesBtn)+1, listBtn);
                    sideMenuBox.getChildren().clear();
                    for(int i = 0; i < sideMenuBtnsList.size(); i++){sideMenuBox.getChildren().add(sideMenuBtnsList.get(i));}
                }
                else{
                    sideMenuBtnsList.remove(sideMenuBtnsList.indexOf(resourcesBtn)+1);
                    sideMenuBtnsList.remove(sideMenuBtnsList.indexOf(resourcesBtn)+1);
                    sideMenuBox.getChildren().clear();
                    for(int i = 0; i < sideMenuBtnsList.size(); i++){sideMenuBox.getChildren().add(sideMenuBtnsList.get(i));}
                }
            }
        });


        for(int i = 0; i < sideMenuBtnsList.size(); i++){sideMenuBox.getChildren().add(sideMenuBtnsList.get(i));}
        sideMenuStack.getChildren().addAll(sideMenuBox, toggleButton);

        return sideMenuStack;
    }


    public BorderPane loadMasterResourcesListWindow(){
        ArrayList<String[]> resourceList = Connection.getMasterResourcesListJSON(token);
        
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableResourceList = new GridPane();
        
        Label nameHead = new Label("Название ресурса");
        Label descriptionHead = new Label("Описание ресурса");

        tableResourceList.addRow(0, nameHead, descriptionHead);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(descriptionHead, HPos.CENTER);
        GridPane.setValignment(descriptionHead, VPos.CENTER);

        for (int i = 0; i < resourceList.size(); i++){
            Label name = new Label(resourceList.get(i)[0]);
            Label description = new Label(resourceList.get(i)[1]);
            tableResourceList.addRow(i+1, name, description);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(description, HPos.CENTER);
            GridPane.setValignment(description, VPos.CENTER);
        }

        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));
        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));

        // tableResourceList.getRowConstraints().add(new RowConstraints(100));
        // tableResourceList.getRowConstraints().add(new RowConstraints(100));
        tableResourceList.setAlignment(Pos.CENTER);
        tableResourceList.setGridLinesVisible(true);

        scrollTable.setPrefViewportHeight(200);
        scrollTable.setPrefViewportWidth(100);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableResourceList);
        

        title.setText("Список Ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title, scrollTable);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public BorderPane loadMasterResourcesRequestsWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Запросы Ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public BorderPane loadChangeProfileWindow(){
        BorderPane root                 = new BorderPane();
        StackPane sideMenuStack         = buildMasterSideMenu(2);
        GridPane table                  = new GridPane();

        VBox rightBox                   = new VBox();
        VBox centerBox                  = new VBox();

        HBox btnsBox                    = new HBox();

        TextField nameField             = new TextField();
        TextField surnameField          = new TextField();
        TextField patronymicField       = new TextField();
        TextField emailField            = new TextField();
        TextField phoneField            = new TextField();
        TextField bioField              = new TextField();

        Label title                     = new Label(); 
        Label nameLbl                   = new Label();
        Label surnameLbl                = new Label();
        Label patronymicLbl             = new Label();
        Label emailLbl                  = new Label();
        Label phoneLbl                  = new Label();
        Label bioLbl                    = new Label();
        Label errorMsg                  = new Label();

        Button cancel                   = new Button();
        Button saveChanges              = new Button();

        
        title.setText("Изменение информации профиля");
        nameLbl.setText("Имя");
        surnameLbl.setText("Фамилия");
        patronymicLbl.setText("Отчество");
        emailLbl.setText("Email");
        phoneLbl.setText("Телефон");
        bioLbl.setText("Биография");
        errorMsg.setText("");

        cancel.setText("Отмена");
        saveChanges.setText("Сохранить изменения");

        rightBox.setPrefWidth(MENU_WIDTH);

        Map<String, String> masterMap   = Connection.getMasterProfileInfo(token);
        
        nameField.setText("Имя");
        surnameField.setText("Фамилия");
        patronymicField.setText("Отчество");
        emailField.setText("Email");
        phoneField.setText("Телефон");
        bioField.setText("Биография");

        table.add(nameLbl, 0, 0);
        table.add(nameField, 1, 0);
        GridPane.setHalignment(nameLbl, HPos.CENTER);
        GridPane.setValignment(nameLbl, VPos.CENTER);
        GridPane.setHalignment(nameField, HPos.CENTER);
        GridPane.setValignment(nameField, VPos.CENTER);

        table.add(surnameLbl, 0, 1);
        table.add(surnameField, 1, 1);
        GridPane.setHalignment(surnameLbl, HPos.CENTER);
        GridPane.setValignment(surnameLbl, VPos.CENTER);
        GridPane.setHalignment(surnameField, HPos.CENTER);
        GridPane.setValignment(surnameField, VPos.CENTER);

        table.add(patronymicLbl, 0, 2);
        table.add(patronymicField, 1, 2);
        GridPane.setHalignment(patronymicLbl, HPos.CENTER);
        GridPane.setValignment(patronymicLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicField, HPos.CENTER);
        GridPane.setValignment(patronymicField, VPos.CENTER);

        table.add(emailLbl, 0, 3);
        table.add(emailField, 1, 3);
        GridPane.setHalignment(emailLbl, HPos.CENTER);
        GridPane.setValignment(emailLbl, VPos.CENTER);
        GridPane.setHalignment(emailField, HPos.CENTER);
        GridPane.setValignment(emailField, VPos.CENTER);

        table.add(phoneLbl, 0, 4);
        table.add(phoneField, 1, 4);
        GridPane.setHalignment(phoneLbl, HPos.CENTER);
        GridPane.setValignment(phoneLbl, VPos.CENTER);
        GridPane.setHalignment(phoneField, HPos.CENTER);
        GridPane.setValignment(phoneField, VPos.CENTER);
        
        table.add(bioLbl, 0, 5);
        table.add(bioField, 1, 5);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);
        GridPane.setHalignment(bioField, HPos.CENTER);
        GridPane.setValignment(bioField, VPos.CENTER);

        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setAlignment(Pos.TOP_CENTER);
        btnsBox.setAlignment(Pos.CENTER);

        btnsBox.setSpacing(100);
        centerBox.setSpacing(50);

        cancel.setOnAction(event -> HelpFuncs.loadMasterProfileWindowFunc(cancel, this));
        
        Main main = this;
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newName          = nameField.getText();
                String newSurname       = surnameField.getText();
                String newPatronymic    = patronymicField.getText();
                String newEmail         = emailField.getText();
                String newPhone         = phoneField.getText();
                String newBio           = bioField.getText();

                int status = Connection.changeMasterInfo(token, newName, newSurname, newPatronymic, newEmail, newPhone, newBio);
                if(status == 200){HelpFuncs.loadMasterProfileWindowFunc(cancel, main);}
                else {errorMsg.setText("Ошибка отправки данных на сервер");}
            }
        });


        btnsBox.getChildren().addAll(cancel, saveChanges);
        centerBox.getChildren().addAll(title, table, errorMsg, btnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public BorderPane loadChangeProfilePasswordWindow(){
        BorderPane root                 = new BorderPane();
        StackPane sideMenuStack         = buildMasterSideMenu(2);
        GridPane table                  = new GridPane();

        VBox rightBox                   = new VBox();
        VBox centerBox                  = new VBox();

        HBox btnsBox                    = new HBox();

        TextField oldPasswordField      = new TextField();
        TextField newPasswordField      = new TextField();
        
        Label title                     = new Label(); 
        Label oldPasswordLbl            = new Label();
        Label newPasswordLbl            = new Label();
        Label errorMsg                  = new Label();
        
        Button cancel                   = new Button();
        Button saveChanges              = new Button();

        
        title.setText("Изменение пароля профиля");
        errorMsg.setText("");
        oldPasswordLbl.setText("Старый пароль");
        newPasswordLbl.setText("Новый пароль");
        
        cancel.setText("Отмена");
        saveChanges.setText("Сохранить изменения");

        rightBox.setPrefWidth(MENU_WIDTH);

        table.add(oldPasswordLbl, 0, 0);
        table.add(oldPasswordField, 1, 0);
        GridPane.setHalignment(oldPasswordLbl, HPos.CENTER);
        GridPane.setValignment(oldPasswordLbl, VPos.CENTER);
        GridPane.setHalignment(oldPasswordField, HPos.CENTER);
        GridPane.setValignment(oldPasswordField, VPos.CENTER);

        table.add(newPasswordLbl, 0, 1);
        table.add(newPasswordField, 1, 1);
        GridPane.setHalignment(newPasswordLbl, HPos.CENTER);
        GridPane.setValignment(newPasswordLbl, VPos.CENTER);
        GridPane.setHalignment(newPasswordField, HPos.CENTER);
        GridPane.setValignment(newPasswordField, VPos.CENTER);

        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setAlignment(Pos.TOP_CENTER);
        btnsBox.setAlignment(Pos.CENTER);

        btnsBox.setSpacing(100);
        centerBox.setSpacing(50);

        cancel.setOnAction(event -> HelpFuncs.loadMasterProfileWindowFunc(cancel, this));
        
        Main main = this;
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String oldPassword      = oldPasswordField.getText();
                String newPassword       = newPasswordField.getText();
                
                int status = Connection.changeMasterPassword(token, oldPassword, newPassword);
                if(status == 200){HelpFuncs.loadMasterProfileWindowFunc(cancel, main);}
                else {errorMsg.setText("Ошибка отправки данных на сервер");}
            }
        });

        btnsBox.getChildren().addAll(cancel, saveChanges);
        centerBox.getChildren().addAll(title, table, errorMsg, btnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public BorderPane loadMasterProfileWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(2);
        GridPane table              = new GridPane();
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();
        VBox avatarBox              = new VBox();

        HBox profileInfoBox         = new HBox();
        HBox changeInfoBtnsBox      = new HBox();

        Button changeAvatarBtn      = new Button();
        Button changePasswordBtn    = new Button();
        Button changeInfoBtn        = new Button();

        Label nameLbl               = new Label();
        Label surnameLbl            = new Label();
        Label patronymicLbl         = new Label();
        Label birthdayLbl           = new Label();
        Label phoneLbl              = new Label();
        Label emailLbl              = new Label();
        Label bioLbl                = new Label();
        Label roleLbl               = new Label();

        Label name                  = new Label();
        Label surname               = new Label();
        Label patronymic            = new Label();
        Label birthday              = new Label();
        Label phone                 = new Label();
        Label email                 = new Label();
        Label bio                   = new Label();
        Label role                  = new Label();        
        
        Label title                 = new Label();

        
        title.setText("Профиль");
        nameLbl.setText("Имя");
        surnameLbl.setText("Фамилия");
        patronymicLbl.setText("Отчество");
        birthdayLbl.setText("Дата рождения");
        phoneLbl.setText("Телефон");
        emailLbl.setText("Email");
        bioLbl.setText("Биография");
        roleLbl.setText("Роль");

        Map<String, String> masterInfo = Connection.getMasterProfileInfo(token);
        
        Image avatarImage = null;
        
        if(masterInfo == null) {
            avatarImage = Connection.getMasterPhoto(null);
            name.setText("Ошибка получения данных");
            surname.setText("Ошибка получения данных");
            patronymic.setText("Ошибка получения данных");
            birthday.setText("Ошибка получения данных");
            phone.setText("Ошибка получения данных");
            email.setText("Ошибка получения данных");
            bio.setText("Ошибка получения данных");
            role.setText("Ошибка получения данных");
        }
        else{
            avatarImage = Connection.getMasterPhoto(masterInfo.get("photo"));
            name.setText(masterInfo.get("name"));
            surname.setText(masterInfo.get("surname"));
            patronymic.setText(masterInfo.get("patronymic"));
            birthday.setText(masterInfo.get("birthday"));
            phone.setText(masterInfo.get("phone"));
            email.setText(masterInfo.get("email"));
            bio.setText(masterInfo.get("bio"));
            role.setText(masterInfo.get("role"));
        }
        
        ImageView avatarImageView = new ImageView(avatarImage);
        Circle circle = new Circle(avatarImageView.getImage().getHeight()/20);
        avatarImageView.setFitHeight(avatarImageView.getImage().getHeight()/10);
        avatarImageView.setFitWidth(avatarImageView.getImage().getWidth()/10);
        avatarImageView.setPreserveRatio(true);
        avatarImageView.setClip(circle);
        circle.setCenterX(avatarImageView.getImage().getWidth()/20);
        circle.setCenterY(avatarImageView.getImage().getHeight()/20);

        table.add(nameLbl, 0, 0);
        table.add(name, 1, 0);
        GridPane.setHalignment(nameLbl, HPos.CENTER);
        GridPane.setValignment(nameLbl, VPos.CENTER);
        GridPane.setHalignment(name, HPos.CENTER);
        GridPane.setValignment(name, VPos.CENTER);

        table.add(surnameLbl, 0, 1);
        table.add(surname, 1, 1);
        GridPane.setHalignment(surnameLbl, HPos.CENTER);
        GridPane.setValignment(surnameLbl, VPos.CENTER);
        GridPane.setHalignment(surname, HPos.CENTER);
        GridPane.setValignment(surname, VPos.CENTER);

        table.add(patronymicLbl, 0, 2);
        table.add(patronymic, 1, 2);
        GridPane.setHalignment(patronymicLbl, HPos.CENTER);
        GridPane.setValignment(patronymicLbl, VPos.CENTER);
        GridPane.setHalignment(patronymic, HPos.CENTER);
        GridPane.setValignment(patronymic, VPos.CENTER);

        table.add(birthdayLbl, 0, 3);
        table.add(birthday, 1, 3);
        GridPane.setHalignment(birthdayLbl, HPos.CENTER);
        GridPane.setValignment(birthdayLbl, VPos.CENTER);
        GridPane.setHalignment(birthday, HPos.CENTER);
        GridPane.setValignment(birthday, VPos.CENTER);

        table.add(phoneLbl, 0, 4);
        table.add(phone, 1, 4);
        GridPane.setHalignment(phoneLbl, HPos.CENTER);
        GridPane.setValignment(phoneLbl, VPos.CENTER);
        GridPane.setHalignment(phone, HPos.CENTER);
        GridPane.setValignment(phone, VPos.CENTER);

        table.add(emailLbl, 0, 5);
        table.add(email, 1, 5);
        GridPane.setHalignment(emailLbl, HPos.CENTER);
        GridPane.setValignment(emailLbl, VPos.CENTER);
        GridPane.setHalignment(email, HPos.CENTER);
        GridPane.setValignment(email, VPos.CENTER);
        
        table.add(bioLbl, 0, 6);
        table.add(bio, 1, 6);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);
        GridPane.setHalignment(bio, HPos.CENTER);
        GridPane.setValignment(bio, VPos.CENTER);

        table.add(roleLbl, 0, 7);
        table.add(role, 1, 7);
        GridPane.setHalignment(roleLbl, HPos.CENTER);
        GridPane.setValignment(roleLbl, VPos.CENTER);
        GridPane.setHalignment(role, HPos.CENTER);
        GridPane.setValignment(role, VPos.CENTER);

        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        changeAvatarBtn.setText("Сменить фото");
        changePasswordBtn.setText("Сменить пароль");
        changeInfoBtn.setText("Изменить данные");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);
        avatarBox.setSpacing(30);
        profileInfoBox.setSpacing(100);
        changeInfoBtnsBox.setSpacing(50);

        avatarBox.setAlignment(Pos.CENTER);
        centerBox.setAlignment(Pos.TOP_CENTER);
        table.setAlignment(Pos.CENTER);
        profileInfoBox.setAlignment(Pos.CENTER);
        changeInfoBtnsBox.setAlignment(Pos.CENTER);


        changeInfoBtn.setOnAction(event -> HelpFuncs.loadMasterChangeProfileInfoWindowFunc(changeInfoBtn, this));
        changePasswordBtn.setOnAction(event -> HelpFuncs.loadMasterChangeProfilePasswordWindowFunc(changePasswordBtn, this));


        Main main = this;
        changeAvatarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                
                fileChooser.setTitle("Выберите файл");
                
                FileChooser.ExtensionFilter extFilter1 = 
                    new FileChooser.ExtensionFilter("Картинки (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter extFilter2 = 
                    new FileChooser.ExtensionFilter("Картинки (*.png)", "*.png");
                
                fileChooser.getExtensionFilters().add(extFilter1);
                fileChooser.getExtensionFilters().add(extFilter2);
                
                File file = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());
                if (file != null) {
                    System.out.println("Выбранный файл: " + file.getAbsolutePath());
                    int status = Connection.changeMasterPhoto(token, file);
                }
                HelpFuncs.loadMasterProfileWindowFunc(changeAvatarBtn, main);
            }
        });

        avatarBox.getChildren().addAll(avatarImageView, changeAvatarBtn);
        changeInfoBtnsBox.getChildren().addAll(changePasswordBtn, changeInfoBtn);
        profileInfoBox.getChildren().addAll(avatarBox, table);
        centerBox.getChildren().addAll(title, profileInfoBox, changeInfoBtnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }



    public BorderPane loadMasterServicesWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(3);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Услуги");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public BorderPane loadMasterHelpWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(4);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Помощь");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public BorderPane loadMasterCalendarWindow(){
        BorderPane root             = new BorderPane();
        
        StackPane sideMenuStack     = buildMasterSideMenu(0);
        VBox rightBox               = new VBox();
        
        VBox centerBox              = new VBox();
        HBox calendarBox            = new HBox();
        
        HBox chooseMY               = new HBox();
        
        Label title                 = new Label();
        Label error                 = new Label();
        
        TextField yearField         = new TextField();
        
        Button confirmBtn           = new Button();
        Button nextBtn              = new Button();
        Button prevBtn              = new Button();


        Image arrowRightIconImg   = null;
        try{arrowRightIconImg     = new Image(new FileInputStream("photos/Arrow_right_ICON.png"));}
        catch(Exception ex)         {System.out.println(ex);}
        ImageView arrowRightIcon   = new ImageView(arrowRightIconImg);

        Image arrowLeftIconImg    = null;
        try{arrowLeftIconImg      = new Image(new FileInputStream("photos/Arrow_left_ICON.png"));}
        catch(Exception ex)         {System.out.println(ex);}
        ImageView arrowLeftIcon    = new ImageView(arrowLeftIconImg);

        arrowLeftIcon.setFitWidth(50);
        arrowLeftIcon.setFitHeight(100);
        arrowRightIcon.setFitWidth(50);
        arrowRightIcon.setFitHeight(100);

        nextBtn.setGraphic(arrowRightIcon);
        prevBtn.setGraphic(arrowLeftIcon);
        

        ObservableList<String> monthsList = FXCollections.observableArrayList("Январь", "Февраль", 
            "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        
        ComboBox<String> monthsComboBox = new ComboBox<String>(monthsList);
        

        rightBox.setPrefWidth(MENU_WIDTH);
        
        title.setText("Календарь");
        confirmBtn.setText("Показать");

        chooseMY.setAlignment(Pos.CENTER);
        chooseMY.setSpacing(5);

        calendarBox.setAlignment(Pos.CENTER);
        calendarBox.setSpacing(5);
        
        
        monthsComboBox.setValue(monthsList.get(month));
        yearField.setText(String.valueOf(year));
        
        calendar = buildCalendarByYM(Integer.parseInt(yearField.getText()), monthsList.indexOf(monthsComboBox.getValue())+1);
        


        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(50);

        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                centerBox.getChildren().remove(calendarBox);
                error.setText("");
                try{
                    int newYear = Integer.parseInt(yearField.getText());
                    int newMonth = monthsList.indexOf(monthsComboBox.getValue());
                    if(newYear <= 3000 && newYear >= 2000){
                        year = newYear;
                        month = newMonth;
                        
                        monthsComboBox.setValue(monthsList.get(month));
                        yearField.setText(String.valueOf(year));
                        calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, calendar, nextBtn);
                        centerBox.getChildren().add(calendarBox);        
                    }
                    else{
                        error.setText("Введите корректные месяц и год");
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                    error.setText("Введите корректные месяц и год");
                }
            }
        });

        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(month == 11){
                    month = 0;
                    year++;
                }
                else{
                    month++;
                }

                centerBox.getChildren().remove(calendarBox);
                error.setText("");
                try{
                    if(year <= 3000 && year >= 2000){
                        monthsComboBox.setValue(monthsList.get(month));
                        yearField.setText(String.valueOf(year));

                        calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, calendar, nextBtn);
                        centerBox.getChildren().add(calendarBox);  
                        monthsComboBox.setValue(monthsList.get(month));
                        yearField.setText(String.valueOf(year));      
                    }
                    else{
                        error.setText("Введите корректные месяц и год");
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                    error.setText("Введите корректные месяц и год");
                }

            }
        });


        prevBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(month == 0){
                    month = 11;
                    year--;
                }
                else{
                    month--;
                }

                centerBox.getChildren().remove(calendarBox);
                error.setText("");
                try{
                    if(year <= 3000 && year >= 2000){
                        monthsComboBox.setValue(monthsList.get(month));
                        yearField.setText(String.valueOf(year));

                        calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, calendar, nextBtn);
                        centerBox.getChildren().add(calendarBox);  
                        monthsComboBox.setValue(monthsList.get(month));
                        yearField.setText(String.valueOf(year));      
                    }
                    else{
                        error.setText("Введите корректные месяц и год");
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                    error.setText("Введите корректные месяц и год");
                }

            }
        });

        chooseMY.getChildren().addAll(monthsComboBox, yearField, confirmBtn);
        calendarBox.getChildren().addAll(prevBtn, calendar, nextBtn);
        centerBox.getChildren().addAll(title, chooseMY, error, calendarBox);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
    }

    



    public VBox loadAuthorizationWindow(){
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

        Main main = this;
        authorizationBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                // HelpFuncs.loadMasterCalendarWindowFunc(authorizationBtn, main);
                
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

                token = checkResponse[0];
                role = checkResponse[1];
                // Properties props = new Properties();
                // props.setProperty("token", token);
                // props.setProperty("role", role);   
                // try{
                //     OutputStream out = Files.newOutputStream(Paths.get("sources/client_props.properties"));
                //     props.store(out, "add info");
                // }
                // catch(Exception ex){System.out.println(ex);}
                HelpFuncs.loadMasterCalendarWindowFunc(authorizationBtn, main);
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

































    @Override
    public void start(Stage stage) {
        year = LocalDate.now().getYear();
        month = LocalDate.now().getMonthValue()-1;
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("sources/client_props.properties"))){
                props.load(in);
            }catch(Exception ex){System.out.println(ex);}
            
        String login = props.getProperty("token", "No");
        // if(login.equals("No") )


        Scene scene = new Scene(loadAuthorizationWindow());
        stage.setTitle("VerartiAPP");
        stage.setWidth(1920);
        stage.setHeight(1080);
        String style = (getClass().getResource("/test.css")).toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);    
         
        stage.show();
    }
}
