package org.admin.UI.window.scheduleWindow;

import org.Main;
import org.admin.connection.getRequests.GetSchedule;
import org.admin.controller.AdminController;
import org.admin.UI.components.sideMenu.SideMenu;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.collections.*;
import org.admin.model.ScheduleDay;
import org.admin.model.User;
import org.admin.utils.HelpFuncs;
import org.admin.utils.validation.Validation;
import org.admin.utils.validation.YearValidation;

import java.io.*;
import java.util.*;
import java.time.*;

public class ScheduleWindow extends Main{
    private static final int CALENDAR_ROW_CONSTRAINT = 85;
    private static final int CALENDAR_COLUMN_CONSTRAINT = 120;

    public static GridPane buildCalendarByYM(int year, int month){
        GridPane calendar           = new GridPane();

        int monthLen                = YearMonth.of(year, month).lengthOfMonth();
        int firstDayInWeek          = HelpFuncs.getDayOfWeekByStr(year+"-"+month+"-01");
        
        Label[] daysOfWeekArr = {new Label("ПН"), new Label("ВТ"), new Label("СР"), new Label("ЧТ"), 
                                new Label("ПТ"), new Label("СБ"), new Label("ВС")};
        
        calendar.setAlignment(Pos.CENTER);
        calendar.setGridLinesVisible(true);
        
        List<ScheduleDay> schedule = GetSchedule.getListByMonthAndYear(token, year, month);
//        if(schedule.isEmpty()){
//            for(int i = 0; i < 31; i++){
//                ScheduleDay scheduleDay = new ScheduleDay();
//                scheduleDay.setDate(LocalDate.of(year, month, i + 1));
//                schedule.add(scheduleDay);
//            }
//        }


        calendar.getRowConstraints().add(new RowConstraints(50));

        for (int i = 0; i < 7; i++){
            GridPane.setHalignment(daysOfWeekArr[i], HPos.CENTER);
            GridPane.setValignment(daysOfWeekArr[i], VPos.CENTER);
            calendar.add(daysOfWeekArr[i], i, 0);
            calendar.getColumnConstraints().add(new ColumnConstraints(CALENDAR_COLUMN_CONSTRAINT));
        }

        int row = 1;
        int column = firstDayInWeek-1;

        for(ScheduleDay scheduleDay : schedule){
            Rectangle rect = new Rectangle(CALENDAR_COLUMN_CONSTRAINT, CALENDAR_ROW_CONSTRAINT, Color.TRANSPARENT); // Прозрачный
            int day = scheduleDay.getDate().getDayOfMonth();
            Label dayLbl = new Label(String.valueOf(day));

            LocalDate date = scheduleDay.getDate();
            User admin = scheduleDay.getAdmin();
            List<User> masters = scheduleDay.getMasters();
            VBox cellInfo = new VBox();
            cellInfo.setAlignment(Pos.CENTER);

            Label adminLbl = new Label(admin.getCode()!=200?"Не назначен":admin.getSmallFio());
            adminLbl.setFont(Font.font("Open Sans Bold"));
            cellInfo.getChildren().add(adminLbl);
            for(User master: masters){
                Label masterLbl = new Label(master.getSurname() + " " + master.getName().charAt(0) + ".");
                cellInfo.getChildren().add(masterLbl);
            }

            rect.setOnMouseClicked(event -> AdminController.loadDayInfoWindow(rect, date));
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
                calendar.getRowConstraints().add(new RowConstraints(CALENDAR_ROW_CONSTRAINT));
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

        if(!new YearValidation(yearField.getText()).validate()) {error.setText("Некорректный год");}
        Main.calendar = buildCalendarByYM(Integer.parseInt(yearField.getText()), monthsList.indexOf(monthsComboBox.getValue())+1);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 10, 10));
        centerBox.setSpacing(20);

        confirmBtn.setOnAction(event ->  {
            error.setText("");

            if(!new YearValidation(yearField.getText()).validate()) {error.setText("Некорректный год"); return;}
            centerBox.getChildren().remove(calendarBox);
            int newYear = Integer.parseInt(yearField.getText());
            int newMonth = monthsList.indexOf(monthsComboBox.getValue());

            year = newYear;
            month = newMonth;

            Main.calendar = buildCalendarByYM(year, month+1);

            calendarBox.getChildren().clear();
            calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
            centerBox.getChildren().add(calendarBox);
        });

        nextBtn.setOnAction(event -> {
            if(month == 11){
                month = 0;
                year++;
            }
            else{
                month++;
            }
            if(!new YearValidation(yearField.getText()).validate()) {error.setText("Некорректный год"); return;}
            centerBox.getChildren().remove(calendarBox);
            error.setText("");
            monthsComboBox.setValue(monthsList.get(month));
            yearField.setText(String.valueOf(year));

            Main.calendar = buildCalendarByYM(year, month+1);
            calendarBox.getChildren().clear();
            calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
            centerBox.getChildren().add(calendarBox);
        });
        prevBtn.setOnAction(event ->  {
            if(month == 0){
                month = 11;
                year--;
            }
            else{
                month--;
            }

            if(!new YearValidation(yearField.getText()).validate()) {error.setText("Некорректный год"); return;}
            centerBox.getChildren().remove(calendarBox);
            error.setText("");
            monthsComboBox.setValue(monthsList.get(month));
            yearField.setText(String.valueOf(year));

            Main.calendar = buildCalendarByYM(year, month+1);
            calendarBox.getChildren().clear();
            calendarBox.getChildren().addAll(prevBtn, Main.calendar, nextBtn);
            centerBox.getChildren().add(calendarBox);
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