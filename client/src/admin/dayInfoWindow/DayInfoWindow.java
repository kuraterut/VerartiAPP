package src.admin.dayInfoWindow;

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

public class DayInfoWindow extends Main{
	public static ScrollPane createDayInfoTable(LocalDate date){
		int day = date.getDayOfMonth();
		int year = date.getYear();
		int month = date.getMonthValue();

		ScrollPane scrollPane = new ScrollPane();
		GridPane table = new GridPane();

		int countTableCell = 1;
		table.getRowConstraints().add(new RowConstraints(50));

		for(int i = 8; i < 23; i++){
			Label first = new Label(i+":00-"+i+":30");
			Label second = new Label(i+":30-"+(i+1)+":00");
			table.add(first, 0, countTableCell);
			countTableCell++;
			table.add(second, 0, countTableCell);
			countTableCell++;
			table.getRowConstraints().add(new RowConstraints(40));
			table.getRowConstraints().add(new RowConstraints(40));
		}
		scrollPane.setContent(table);

		table.getColumnConstraints().add(new ColumnConstraints(80));
		table.getColumnConstraints().add(new ColumnConstraints(150));

		table.setGridLinesVisible(true);
		scrollPane.setPrefViewportHeight(800);
		scrollPane.setPrefViewportWidth(1150);

		return scrollPane;
	}

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

        Button totalSumBtn			= new Button();
        Button cashBtn 				= new Button();


        rightBox.setMinWidth(MENU_WIDTH);
        totalSumBtn.setMinHeight(25);
        totalSumBtn.setMinWidth(150);
        cashBtn.setMinHeight(25);
        cashBtn.setMinWidth(150);
        miniCalendar.setMinHeight(25);
        miniCalendar.setMinWidth(150);
        

        // int totalSum = Connection.getTotalSum(token, date);
        // int cash = Connection.getCash(token, date);
        int totalSum = date.getYear();
        int cash = 1000;


        totalSumBtn.setText(Integer.toString(totalSum));
        cashBtn.setText(Integer.toString(cash));

        VBox.setMargin(sheduleHeaders, new Insets(100, 0, 0, 0));

        sheduleHeaders.setAlignment(Pos.CENTER);
        sheduleHeaders.setSpacing(900);
        miniCalendar.setValue(date);

        ScrollPane scrollTable = createDayInfoTable(date);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setScrollLeft(Double.MAX_VALUE);

        miniCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
         AdminInterface.loadDayInfoWindow(miniCalendar, newValue);
	    });

        rightSheduleHeaders.getChildren().addAll(cashBtn, totalSumBtn);
        leftSheduleHeaders.getChildren().addAll(miniCalendar);
        sheduleHeaders.getChildren().addAll(leftSheduleHeaders, rightSheduleHeaders);
        centerInfo.getChildren().addAll(scrollTable, textArea);
        centerBox.getChildren().addAll(sheduleHeaders, centerInfo);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
	}
}