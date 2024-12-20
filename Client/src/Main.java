import javafx.application.Application;
import javafx.stage.Stage;

import javax.mail.*;
// import java.text.*;

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
        
        
        int index = 1;
        boolean flag = true;
        for (int i = 1; i < 7; i++){
            if(flag){
                for (int j = firstDayInWeek-1; j < 7; j++){
                    Rectangle rect = new Rectangle(150, 100, Color.TRANSPARENT); // Прозрачный
                    Label indexLbl = new Label(String.valueOf(index));
                    

                    Label cellMiniInfo = new Label();
                    int guestCount = 1;
                    String workTime = "9:00-21:00";
                    cellMiniInfo.setText("Кол-во: "+ guestCount+"\nВремя работы:\n"+workTime);

                    
                    final int roww = i;
                    final int coll = j;

                    rect.setOnMouseClicked(event -> showCellInfo(roww, coll));
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
                    int guestCount = 1;
                    String workTime = "9:00-21:00";
                    cellMiniInfo.setText("Кол-во: "+ guestCount+"\nВремя работы:\n"+workTime);
                    
                    final int roww = i;
                    final int coll = j;

                    rect.setOnMouseClicked(event -> showCellInfo(roww, coll));
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

    private void showCellInfo(int row, int col) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Cell Information");
        alert.setHeaderText(null);
        alert.setContentText("You clicked on cell at Row: " + row + ", Column: " + col);
        alert.showAndWait();
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
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Список Ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
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
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public BorderPane loadMasterProfileWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = buildMasterSideMenu(2);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title = new Label();
        
        title.setText("Профиль");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title);
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
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
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
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
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
        
        HBox chooseMY               = new HBox();
        
        Label title                 = new Label();
        Label error                 = new Label();
        
        TextField yearField         = new TextField();
        
        Button confirmBtn           = new Button();
        

        ObservableList<String> monthsList = FXCollections.observableArrayList("Январь", "Февраль", 
            "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        
        ComboBox<String> monthsComboBox = new ComboBox<String>(monthsList);
        

        rightBox.setPrefWidth(MENU_WIDTH);
        
        title.setText("Календарь");
        confirmBtn.setText("Показать");

        chooseMY.setAlignment(Pos.CENTER);
        chooseMY.setSpacing(5);
        
        
        monthsComboBox.setValue(monthsList.get(LocalDate.now().getMonthValue()-1));
        
        yearField.setText(String.valueOf(LocalDate.now().getYear()));
        
        calendar = buildCalendarByYM(Integer.parseInt(yearField.getText()), monthsList.indexOf(monthsComboBox.getValue())+1);
        
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(50);

        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                centerBox.getChildren().remove(calendar);
                error.setText("");
                try{
                    int year = Integer.parseInt(yearField.getText());
                    if(year <= 3000 && year >= 2000){
                        calendar = buildCalendarByYM(Integer.parseInt(yearField.getText()), monthsList.indexOf(monthsComboBox.getValue())+1);
                        centerBox.getChildren().add(calendar);        
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
        centerBox.getChildren().addAll(title, chooseMY, error, calendar);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
    }

    private void toggleMenu(VBox sideMenu) {
        
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), sideMenu);        
        if (isMenuVisible) {
            transition.setToX(0);
            sideMenu.setVisible(true); // Сначала делаем меню видимым
            sideMenu.setOpacity(0.0); // Устанавливаем начальную непрозрачность в 0
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.1), sideMenu);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            fadeIn.play();
             // Скрыть меню
        } 

        else {
            transition.setToX(-(MENU_WIDTH));
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.1), sideMenu);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event ->sideMenu.setVisible(false)); // Скрыть меню после анимации
            fadeOut.play();
             // Показать меню
        }
        
        transition.play();
        isMenuVisible = !isMenuVisible; // Переключаем состояние видимости
    }


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(loadMasterCalendarWindow());
        stage.setTitle("VerartiAPP");
        stage.setWidth(1920);
        stage.setHeight(1080);
        String style = (getClass().getResource("/test.css")).toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);    
         
        stage.show();
    }
}
