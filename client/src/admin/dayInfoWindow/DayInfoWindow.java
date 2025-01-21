package src.admin.dayInfoWindow;

import src.Main;
import src.admin.AdminInterface;
import src.admin.connection.Connection;
import src.admin.sideMenu.SideMenu;
import src.admin.utils.*;

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
            
            GridPane.setHalignment(first, HPos.CENTER);
            GridPane.setValignment(first, VPos.CENTER);
            GridPane.setHalignment(second, HPos.CENTER);
            GridPane.setValignment(second, VPos.CENTER);
		}
		scrollPane.setContent(table);

		table.getColumnConstraints().add(new ColumnConstraints(100));
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

        ComboBox<String> comboBox = new ComboBox<>();

        ArrayList<String>[] clientsInfo = Connection.getAllClientsInfo(token);
        if(clientsInfo == null){
            clientsInfo = new ArrayList[2];
            clientsInfo[0] = new ArrayList<String>();
            clientsInfo[1] = new ArrayList<String>();
        }

        comboBox.getEditor().setOnKeyReleased(new AutoCompleteComboBoxListener(comboBox, clientsInfo[0], clientsInfo[1]));

        comboBox.setEditable(true);
        comboBox.getItems().addAll(numbers);
        comboBox.setPrefWidth(500);
        

        centerBox.setAlignment(Pos.CENTER);
        rightBox.setMinWidth(MENU_WIDTH);
        totalSumBtn.setMinHeight(25);
        totalSumBtn.setMinWidth(150);
        cashBtn.setMinHeight(25);
        cashBtn.setMinWidth(150);
        miniCalendar.setMinHeight(25);
        miniCalendar.setMinWidth(200);
        

        // int totalSum = Connection.getTotalSum(token, date);
        // int cash = Connection.getCash(token, date);
        int totalSum = date.getYear();
        int cash = 1000;


        totalSumBtn.setText(Integer.toString(totalSum));
        cashBtn.setText(Integer.toString(cash));

        VBox.setMargin(sheduleHeaders, new Insets(100, 0, 0, 0));

        sheduleHeaders.setAlignment(Pos.CENTER);
        sheduleHeaders.setSpacing(850);
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
        centerBox.getChildren().addAll(comboBox, sheduleHeaders, centerInfo);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
	}
}



class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {
    private ComboBox comboBox;
    private ObservableList names;
    private ObservableList numbers;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteComboBoxListener(final ComboBox comboBox, List<String> listNames, List<String> listNumbers) {
        this.comboBox = comboBox;
        this.names = FXCollections.observableList(listNames);
        this.numbers = FXCollections.observableList(listNumbers);

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
    }

    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }



        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }


        ObservableList list = FXCollections.observableArrayList();
        for (int i = 0; i < names.size(); i++) {
            String input = comboBox.getEditor().getText().toLowerCase().trim();
            
            if(input.length() > 0 && Character.isDigit(input.charAt(0))){
                if(numbers.get(i).toString().toLowerCase().endsWith(input)){
                    list.add(names.get(i) + " (" + numbers.get(i) + ")"); 
                } 
            }
            else{
                if(names.get(i).toString().toLowerCase().startsWith(input)) {
                    list.add(names.get(i) + " (" + numbers.get(i) + ")");
                }    
            }
            
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }

        if(event.getCode() == KeyCode.ENTER){
            caretPos = -1;
            moveCaret(t.length());
            comboBox.hide();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
}