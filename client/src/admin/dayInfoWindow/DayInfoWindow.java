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
    private static Integer CELLS_IN_COLUMN_COUNT = 28;

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

        Map<Long, List<Appointment>> dayInfo = Connection.getDayInfoMapMaster(token, date);
        if(dayInfo == null){dayInfo = new HashMap<>();}

        int countColumn = 0;
        for(Long masterId : dayInfo.keySet()){
            Set<Integer> usedCells = new HashSet<>();
            countColumn++;
            table.getColumnConstraints().add(new ColumnConstraints(200));
            MasterInfo master = Connection.getMasterById(token, masterId);

            Label masterIdLbl = new Label(Long.toString(masterId));
            Label masterFioLbl = new Label(master.getFio());

            table.add(masterIdLbl, countColumn, 0);
            table.add(masterFioLbl, countColumn, 0);
            GridPane.setHalignment(masterIdLbl, HPos.CENTER);
            GridPane.setValignment(masterIdLbl, VPos.CENTER);
            GridPane.setHalignment(masterFioLbl, HPos.CENTER);
            GridPane.setValignment(masterFioLbl, VPos.CENTER);

            List<Appointment> appointments = dayInfo.get(masterId);
            Set<Integer> startCellsSet = new HashSet<>();
            for(Appointment appointment: appointments){
                Integer startCellForSet = calculateCellStart(appointment.getStartTime());
                startCellsSet.add(startCellForSet);
            }

            for(Appointment appointment: appointments){
                Long id = appointment.getId();
                ClientInfo client = appointment.getClient();
                List<ServiceInfo> services = appointment.getServices();
                ServiceInfo firstService = services.get(0);
                Integer cellStart = calculateCellStart(appointment.getStartTime());
                Integer cellNumber = calculateCellNumber(services);

                Rectangle rectStart = new Rectangle(150, 100, Color.AQUAMARINE);
                rectStart.setOnMouseClicked(event -> showAppointmentInfoDialog(id));
                rectStart.setOnMouseEntered(event -> {
                    rectStart.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                });
                rectStart.setOnMouseExited(event -> rectStart.setStyle("-fx-fill: aquamarine"));

                Label appointmentHeadLbl = new Label("Запись №"+id);
                Label clientLbl = new Label(client.getFio());
                Label serviceLbl = new Label();
                
                if(services.size() == 1){
                    serviceLbl.setText(firstService.getName());
                }
                else{
                    serviceLbl.setText(firstService.getName()+"...");
                }

                table.add(appointmentHeadLbl, countColumn, cellStart);
                table.add(clientLbl, countColumn, cellStart);
                table.add(serviceLbl, countColumn, cellStart);
                table.add(rectStart, countColumn, cellStart);
                
                usedCells.add(cellStart);

                for(int i = 1; i < cellNumber; i++){
                    if(startCellsSet.contains(cellStart+i)) break;
                    Rectangle rectFill = new Rectangle(150, 100, Color.AQUA);
                    table.add(rectFill, countColumn, cellStart+i);
                    usedCells.add(cellStart+i);
                }

                for(int i = 1; i <= CELLS_IN_COLUMN_COUNT; i++){
                    if(!usedCells.contains(i)){
                        Rectangle unusedRect = new Rectangle(150, 100, Color.TRANSPARENT);
                        Integer startCellToCreate = i;
                        unusedRect.setOnMouseClicked(event -> showCreateAppointmentDialog(master, date, startCellToCreate, appointments));
                        table.add(unusedRect, countColumn, i);
                        unusedRect.setOnMouseEntered(event -> {
                            unusedRect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                        });
                        unusedRect.setOnMouseExited(event -> unusedRect.setStyle("")); 
                    }   
                }
            }
        }

		table.setGridLinesVisible(true);
		scrollPane.setPrefViewportHeight(800);
		scrollPane.setPrefViewportWidth(1150);

		return scrollPane;
	}

    private static LocalTime startCellToStartTime(Integer startCell){
        Integer hour = (startCell-1)/2 + 8;
        Integer minute = 30*((startCell+1)%2);
        return LocalTime.of(hour, minute);
    }

    public static void showCreateAppointmentDialog(MasterInfo master, LocalDate date, Integer startCell, List<Appointment> appointments){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать запись");

        LocalTime startTime = startCellToStartTime(startCell);
        List<ServiceInfo> services = master.getServices();
    
        
    }


    public static void showAppointmentInfoDialog(Long appointmentId) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о записи");

        Appointment appointment = Connection.getAppointmentById(token, appointmentId);
        MasterInfo master = appointment.getMaster();
        ClientInfo client = appointment.getClient();
        List<ServiceInfo> services = appointment.getServices();
        LocalDateTime dateTime = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTimeStr = dateTime.format(formatter); // "1986-04-08 12:30"

        Label dateTimeLbl = new Label(dateTimeStr);
        Label appointmentHeadLbl = new Label("Запись №"+appointmentId);
        Label masterLbl = new Label("Мастер: " + master.getFio());
        Label clientLbl = new Label("Клиент: " + client.toString());
        Label servicesLbl = new Label("Услуги:");

        ScrollPane servicesScrollPane = new ScrollPane();
        servicesScrollPane.setPrefViewportHeight(300);
        servicesScrollPane.setPrefViewportWidth(400);
        
        GridPane servicesTable = new GridPane();
        servicesTable.setGridLinesVisible(true);

        servicesScrollPane.setContent(servicesTable);

        Label serviceHeadTable = new Label("Услуга");
        Label timeHeadTable = new Label("Рассчетное время");
        Label priceHeadTable = new Label("Прайс");
        
        servicesTable.add(serviceHeadTable, 0, 0);
        servicesTable.add(timeHeadTable, 1, 0);
        servicesTable.add(priceHeadTable, 2, 0);

        int numServiceRow = 0;
        for(ServiceInfo service: services){
            numServiceRow++;
            Label serviceLbl = new Label(service.getName());
            Label timeLbl = new Label(service.getTimeString());
            Label priceLbl = new Label(Double.toString(service.getPrice()));
            servicesTable.add(serviceLbl, 0, numServiceRow);
            servicesTable.add(timeLbl, 1, numServiceRow);
            servicesTable.add(priceLbl, 2, numServiceRow);
            GridPane.setHalignment(serviceLbl, HPos.CENTER);
            GridPane.setValignment(serviceLbl, VPos.CENTER);
            GridPane.setHalignment(timeLbl, HPos.CENTER);
            GridPane.setValignment(timeLbl, VPos.CENTER);
            GridPane.setHalignment(priceLbl, HPos.CENTER);
            GridPane.setValignment(priceLbl, VPos.CENTER);
        }
        Button addServiceBtn = new Button("Добавить услугу");

        TextArea commentsArea = new TextArea();
        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setText(appointment.getComment());
        commentsArea.setMinWidth(400);
        commentsArea.setMinHeight(200);


        HBox bottomBtnsBox = new HBox();
        Button closeBtn = new Button("Закрыть");
        Button cancelServiceBtn = new Button("Отмена услуги");
        Button paymentBtn = new Button("Оплата");

        bottomBtnsBox.getChildren().addAll(closeBtn, cancelServiceBtn, paymentBtn);
        bottomBtnsBox.setSpacing(100);
        bottomBtnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();

        dateTimeLbl.setAlignment(Pos.TOP_RIGHT);
        appointmentHeadLbl.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(dateTimeLbl, appointmentHeadLbl);
        root.getChildren().addAll(masterLbl, clientLbl);
        root.getChildren().addAll(servicesLbl, servicesScrollPane);
        root.getChildren().addAll(addServiceBtn, commentsArea);
        root.getChildren().addAll(bottomBtnsBox);
        
        Scene dialogScene = new Scene(root, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }


    private static Integer calculateCellStart(LocalTime startTime){
        int hour = startTime.getHour();
        int minute = startTime.getMinute();

        int ans = (hour-8)*2 + 1 + ((minute == 30)?1:0);
        return ans;
    }
    private static Integer calculateCellNumber(List<ServiceInfo> services){
        int totalCount = 0;
        for(ServiceInfo service : services){
            totalCount += service.getTime().getHour()*2;
            totalCount += service.getTime().getMinute()==30?1:0;
        }
        
        return totalCount;
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
        HBox searchStringBox        = new HBox();

        Button totalSumBtn			= new Button();
        Button cashBtn 				= new Button();
        Button putMasterOnDayBtn    = new Button();
        Button putAdminOnDayBtn     = new Button();
        Button addNewClientBtn      = new Button();

        ComboBox<String> comboBox   = new ComboBox<>();

        ArrayList<ClientInfo> clientsInfo = Connection.getAllClientsInfo(token);
        if(clientsInfo == null){clientsInfo = new ArrayList<>();}

        comboBox.getEditor().setOnKeyReleased(new AutoCompleteComboBoxListener(comboBox, clientsInfo));

        comboBox.setEditable(true);
        // comboBox.getItems().addAll(numbers);
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
        sheduleHeaders.setSpacing(550);
        miniCalendar.setValue(date);

        ScrollPane scrollTable = createDayInfoTable(date);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setScrollLeft(Double.MAX_VALUE);

        searchStringBox.setSpacing(25);

        miniCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            AdminInterface.loadDayInfoWindow(miniCalendar, newValue);
	    });


        // putMasterOnDayBtn.setOnAction(event -> showPutMasterOnDayDialog());
        // putAdminOnDayBtn.setOnAction(event -> showPutAdminOnDayDialog());
        // сash.setOnAction(event -> showCashDialog());
        // totalSumBtn.setOnAction(event -> showDayTransactionsDialog());


        searchStringBox.getChildren().addAll(comboBox, addNewClientBtn);
        rightSheduleHeaders.getChildren().addAll(putAdminOnDayBtn, putMasterOnDayBtn, cashBtn, totalSumBtn);
        leftSheduleHeaders.getChildren().addAll(miniCalendar);
        sheduleHeaders.getChildren().addAll(leftSheduleHeaders, rightSheduleHeaders);
        centerInfo.getChildren().addAll(scrollTable, textArea);
        centerBox.getChildren().addAll(searchStringBox, sheduleHeaders, centerInfo);
        root.setLeft(sideMenuStack);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        return root;
	}
}



class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {
    private ComboBox comboBox;
    private ObservableList fios;
    private ObservableList numbers;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteComboBoxListener(final ComboBox comboBox, List<ClientInfo> clientsInfo) {
        this.comboBox = comboBox;
        List<String> fiosList = new ArrayList<>();
        List<String> numbersList = new ArrayList<>();
        


        for(int i = 0; i < clientsInfo.size(); i++){
            fiosList.add(clientsInfo.get(i).getFio());
            numbersList.add(clientsInfo.get(i).getPhone());
        }

        this.fios = FXCollections.observableArrayList(fiosList);
        this.numbers = FXCollections.observableArrayList(numbersList);

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
        for (int i = 0; i < fios.size(); i++) {
            String input = comboBox.getEditor().getText().toLowerCase().trim();
            
            if(input.length() > 0 && Character.isDigit(input.charAt(0))){
                if(numbers.get(i).toString().toLowerCase().endsWith(input)){
                    list.add(fios.get(i) + " (" + numbers.get(i) + ")"); 
                } 
            }
            else{
                if(fios.get(i).toString().toLowerCase().startsWith(input)) {
                    list.add(fios.get(i) + " (" + numbers.get(i) + ")");
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