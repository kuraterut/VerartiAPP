package src.admin.calendarWindow;

import src.Main;
import src.admin.AdminInterface;
import src.admin.connection.Connection;
import src.admin.sideMenu.SideMenu;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.control.Alert.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
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

public class CalendarWindow extends Main{
	public static GridPane buildCalendarByYM(int year, int month){
        GridPane calendar           = new GridPane();
        
        int lastDayOfMonth          = YearMonth.of(year, month).lengthOfMonth();
        String yearStr              = String.valueOf(year);
        String monthStr             = String.valueOf(month);
        String lastDayOfMonthStr    = String.valueOf(lastDayOfMonth);
        int firstDayInWeek          = AdminInterface.getDayOfWeekByStr(yearStr+"-"+monthStr+"-01");
        int lastDayInWeek           = AdminInterface.getDayOfWeekByStr(yearStr+"-"+monthStr+"-"+lastDayOfMonthStr); 
        
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
        String[][] timetableInfo = Connection.getTimetableByYM(year, month, token);


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
                        infoYM = timetableInfo[numCell];    
                    }
                    numCell++;
                    if(infoYM != null){
                       cellMiniInfo.setText("Кол-во: " + infoYM[0] + "\nВремя работы:\n" + infoYM[1]);
                    }
                    else{
                        cellMiniInfo.setText("Выходной");
                    }
                    
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
                        infoYM = timetableInfo[numCell];    
                    }
                    numCell++;
                    if(infoYM != null){
                       cellMiniInfo.setText("Кол-во: " + infoYM[0] + "\nВремя работы:\n" + infoYM[1]);
                    }
                    else{
                        cellMiniInfo.setText("Выходной");
                    }
                    
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



    public static void showCalendarCellInfoDialog(int year, int month, int day) {
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
        btns.getChildren().addAll(button1);
        dialogLayout.getChildren().addAll(infoLabel, scrollTable, btns);
        Scene dialogScene = new Scene(dialogLayout, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }


    public static BorderPane loadCalendarWindow(){
        BorderPane root             = new BorderPane();
        
        StackPane sideMenuStack     = SideMenu.buildSideMenu(0);
        
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
        try{arrowRightIconImg     = new Image(new FileInputStream("client/photos/Arrow_right_ICON.png"));}
        catch(Exception ex)         {System.out.println(ex);}
        ImageView arrowRightIcon   = new ImageView(arrowRightIconImg);

        Image arrowLeftIconImg    = null;
        try{arrowLeftIconImg      = new Image(new FileInputStream("client/photos/Arrow_left_ICON.png"));}
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