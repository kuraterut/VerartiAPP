package org.admin.calendarOfEmployeesWindow;

import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.Connection;
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

public class CalendarOfEmployeesWindow extends Main{
    public static GridPane buildCalendarByYM(int year, int month){
        GridPane calendar           = new GridPane();
        
        // for(String fontName : Font.getFontNames()){
        //     System.out.println(fontName);
        // }
        
        int monthLen                = YearMonth.of(year, month).lengthOfMonth();
        int firstDayInWeek          = AdminInterface.getDayOfWeekByStr(year+"-"+month+"-01");
        
        Label[] daysOfWeekArr = {new Label("ПН"), new Label("ВТ"), new Label("СР"), new Label("ЧТ"), 
                                new Label("ПТ"), new Label("СБ"), new Label("ВС")};
        
        calendar.setAlignment(Pos.CENTER);
        calendar.setGridLinesVisible(true);
        

        calendar.getRowConstraints().add(new RowConstraints(50));

        for (int i = 0; i < 7; i++){
            GridPane.setHalignment(daysOfWeekArr[i], HPos.CENTER);
            GridPane.setValignment(daysOfWeekArr[i], VPos.CENTER);
            calendar.add(daysOfWeekArr[i], i, 0);
            calendar.getColumnConstraints().add(new ColumnConstraints(150));
        }

        int row = 1;
        int column = firstDayInWeek-1;
        // int day = 1;

        for(int day = 1; day <= monthLen; day++){
            Rectangle rect = new Rectangle(150, 100, Color.TRANSPARENT); // Прозрачный
            Label dayLbl = new Label(String.valueOf(day));
            
            LocalDate date = LocalDate.of(year, month, day);
            AdminInfo admin = Connection.getAdminByDate(token, date);
            List<MasterInfo> masters = Connection.getMastersListByDate(token, date);
            VBox cellInfo = new VBox();
            cellInfo.setAlignment(Pos.CENTER);

            if(admin == null){
                admin = new AdminInfo();
                admin.setSurname("Ошибка подключения");
                admin.setName(" ");
            }
            if(masters == null) masters = new ArrayList<>();

            Label adminLbl = new Label(admin.getSurname() + " " + admin.getName().charAt(0) + ".");
            adminLbl.setFont(Font.font("Open Sans Bold"));
            cellInfo.getChildren().add(adminLbl);
            for(MasterInfo master: masters){
                Label masterLbl = new Label(master.getSurname() + " " + master.getName().charAt(0) + ".");
                cellInfo.getChildren().add(masterLbl);
            }
            
            
            rect.setOnMouseClicked(event -> AdminInterface.loadDayInfoWindow(rect, date));
            rect.setOnMouseEntered(event -> {
                rect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
            });
            rect.setOnMouseExited(event -> rect.setStyle(""));

            calendar.add(dayLbl, column, row);
            calendar.add(cellInfo, column, row);
            calendar.add(rect, column, row);

            GridPane.setHalignment(dayLbl, HPos.LEFT);
            GridPane.setValignment(dayLbl, VPos.TOP);
            GridPane.setHalignment(cellInfo, HPos.CENTER);
            GridPane.setValignment(cellInfo, VPos.CENTER);


            column++;
            if(column == 7) {
                row++;
                column = 0;
                calendar.getRowConstraints().add(new RowConstraints(100));
            }
        }
        return calendar;

    }


    public static BorderPane loadCalendarWindow(){
        BorderPane root             = new BorderPane();
        
        StackPane sideMenuStack     = SideMenu.buildSideMenu(2);
        
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
        try{arrowRightIconImg     = new Image(new FileInputStream(properties.getProperty("path.arrow-right-icon")));}
        catch(Exception ex)         {System.out.println(ex);}
        ImageView arrowRightIcon   = new ImageView(arrowRightIconImg);

        Image arrowLeftIconImg    = null;
        try{arrowLeftIconImg      = new Image(new FileInputStream(properties.getProperty("path.arrow-left-icon")));}
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
        
        Main.calendar = buildCalendarByYM(Integer.parseInt(yearField.getText()), monthsList.indexOf(monthsComboBox.getValue())+1);

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
                        Main.calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
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

                        Main.calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
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

                        Main.calendar = buildCalendarByYM(year, month+1);
                        calendarBox.getChildren().clear();
                        calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
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
        calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
        centerBox.getChildren().addAll(title, chooseMY, error, calendarBox);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
    }
}