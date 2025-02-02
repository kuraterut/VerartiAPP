package org.admin.dayInfoWindow;

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

        Map<Long, List<Appointment>> dayInfo = Connection.getMastersAppointmentsByDate(token, date);
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

    public static void showClientInfoDialog(Long clientId){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о клиенте");

        ClientInfo client = Connection.getClientById(token, clientId);

        List<Appointment> clientAppointments = Connection.getClientAppointmentsById(token, clientId);

        if(clientAppointments == null) clientAppointments = new ArrayList<>();

        Label headLbl = new Label(client.getFio());

        GridPane table = new GridPane();

        ScrollPane appointmentsScrollPane = new ScrollPane();
        GridPane appointmentsTable = new GridPane();
        TextArea commentsArea = new TextArea();

        appointmentsScrollPane.setContent(appointmentsTable);

        Label appointmentIdTableHeadLbl = new Label("Запись №");
        Label appointmentDateTimeTableHeadLbl = new Label("Дата и время");
        Label appointmentServicesTableHeadLbl = new Label("Услуги");
        Label appointmentMasterTableHeadLbl = new Label("Мастер");

        appointmentsTable.addRow(0, appointmentIdTableHeadLbl,
                                    appointmentDateTimeTableHeadLbl,
                                    appointmentServicesTableHeadLbl,
                                    appointmentMasterTableHeadLbl);

        int appointmentsTableRowNum = 1;
        for(Appointment appointment: clientAppointments){
            Label appointmentIdLbl = new Label(appointment.getId()+"");
            Label appointmentDateTimeLbl = new Label(appointment.getDateTimeStr());
            
            VBox appointmentServicesVBox = new VBox();
            for(ServiceInfo service: appointment.getServices()){
                Label serviceNameLbl = new Label(service.getName());
                appointmentServicesVBox.getChildren().add(serviceNameLbl);
            }
            appointmentServicesVBox.setAlignment(Pos.CENTER);

            Label appointmentMasterLbl = new Label(appointment.getMaster().getFio());

            appointmentsTable.addRow(appointmentsTableRowNum,   appointmentIdLbl,
                                                                appointmentDateTimeLbl,
                                                                appointmentServicesVBox,
                                                                appointmentMasterLbl);

            GridPane.setHalignment(appointmentIdLbl, HPos.CENTER);
            GridPane.setValignment(appointmentIdLbl, VPos.CENTER);
            GridPane.setHalignment(appointmentDateTimeLbl, HPos.CENTER);
            GridPane.setValignment(appointmentDateTimeLbl, VPos.CENTER);
            GridPane.setHalignment(appointmentServicesVBox, HPos.CENTER);
            GridPane.setValignment(appointmentServicesVBox, VPos.CENTER);
            GridPane.setHalignment(appointmentMasterLbl, HPos.CENTER);
            GridPane.setValignment(appointmentMasterLbl, VPos.CENTER);

            appointmentsTableRowNum++;
        }

        appointmentsTable.setGridLinesVisible(true);
        appointmentsScrollPane.setPrefViewportHeight(200);
        appointmentsScrollPane.setPrefViewportWidth(400);

        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setText(client.getComment());
        commentsArea.setMinWidth(400);
        commentsArea.setMinHeight(200);
        commentsArea.setMaxWidth(400);
        commentsArea.setMaxHeight(200);
        commentsArea.setPrefWidth(400);
        commentsArea.setPrefHeight(200);

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button saveBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Имя: ");
        Label surnameHeadLbl = new Label("Фамилия: ");
        Label patronymicHeadLbl = new Label("Отчество: ");
        Label phoneHeadLbl = new Label("Номер телефона: ");
        Label birthdayHeadLbl = new Label("Дата рождения: ");
        
        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField phoneTextField = new TextField();
        DatePicker birthdayDatePicker = new DatePicker();


        nameTextField.setText(client.getName());
        surnameTextField.setText(client.getSurname());
        patronymicTextField.setText(client.getPatronymic());
        phoneTextField.setText(client.getPhone());
        birthdayDatePicker.setValue(client.getBirthday());



        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        saveBtn.setText("Сохранить");
        saveBtn.setWrapText(true);
        saveBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.add(nameHeadLbl, 0, 0);
        table.add(surnameHeadLbl, 0, 1);
        table.add(patronymicHeadLbl, 0, 2);
        table.add(phoneHeadLbl, 0, 3);
        table.add(birthdayHeadLbl, 0, 4);
        
        table.add(nameTextField, 1, 0);
        table.add(surnameTextField, 1, 1);
        table.add(patronymicTextField, 1, 2);
        table.add(phoneTextField, 1, 3);
        table.add(birthdayDatePicker, 1, 4);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(birthdayHeadLbl, HPos.CENTER);
        GridPane.setValignment(birthdayHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(birthdayDatePicker, HPos.CENTER);
        GridPane.setValignment(birthdayDatePicker, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        
        cancelBtn.setOnAction(event -> dialog.close());
        
        appointmentsTable.setAlignment(Pos.CENTER);
        appointmentsScrollPane.setFitToWidth(true);
        appointmentsScrollPane.setFitToHeight(true);
        

        btnsBox.getChildren().addAll(cancelBtn, saveBtn);
        root.getChildren().addAll(headLbl, table, appointmentsScrollPane, commentsArea, errorMsg, btnsBox);
        Scene dialogScene = new Scene(root, 1500, 800);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();   
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

        Region spacer               = new Region();

        ComboBox<String> comboBox   = new ComboBox<>();

        List<ClientInfo> clientsInfo = Connection.getAllClients(token);
        if(clientsInfo == null){clientsInfo = new ArrayList<>();}

        comboBox.getEditor().setOnKeyReleased(new SearchingStringListenerClients(comboBox, clientsInfo));
        comboBox.setOnAction(event -> {
            String[] val = comboBox.getValue().split(" ");
            try{
                Long clientId = Long.parseLong(val[0]);
                showClientInfoDialog(clientId);
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

        ScrollPane scrollTable = createDayInfoTable(date);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setScrollLeft(Double.MAX_VALUE);

        searchStringBox.setSpacing(25);

        miniCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            AdminInterface.loadDayInfoWindow(miniCalendar, newValue);
	    });


        putMasterOnDayBtn.setOnAction(event -> showPutMasterOnDayDialog(date));
        // putAdminOnDayBtn.setOnAction(event -> showPutAdminOnDayDialog());
        // сash.setOnAction(event -> showCashDialog());
        // totalSumBtn.setOnAction(event -> showDayTransactionsDialog());
        addNewClientBtn.setOnAction(event -> showAddNewClientDialog());

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


    public static void showPutMasterOnDayDialog(LocalDate date) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM yyyy", Locale.ENGLISH);
        String dateStr = dtf.format(date);
        
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Назначить мастера");

        Label errorMsg = new Label("");
        Label dateLbl = new Label(dateStr);

        // Set<Long> mastersOnDate = Connection.getMastersIdsSetByDate(token, date);
        // List<MasterInfo> mastersNotOnDate = new ArrayList<>();
        // List<MasterInfo> allMasters = Connection.getAllMasters(token);

        // for(MasterInfo master : allMasters){
        //     if(!mastersOnDate.contains(master.getId())){
        //         mastersNotOnDate.add(master);
        //     }
        // }
        List<MasterInfo> mastersNotOnDate = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            MasterInfo master = new MasterInfo();
            master.setId(Long.valueOf(i));
            master.setName("Имя" + i);
            master.setSurname("Фамилия" + i);
            master.setPatronymic("Отчество" + i);
            master.setPhone("+7909276246" + i);

            mastersNotOnDate.add(master);
        }


        ComboBox<String> choosingMaster = new ComboBox();

        choosingMaster.getEditor().setOnKeyReleased(new SearchingStringListenerMasters(choosingMaster, mastersNotOnDate));

        choosingMaster.setEditable(true);
        ObservableList comboBoxList = FXCollections.observableArrayList(mastersNotOnDate); 
        choosingMaster.setItems(comboBoxList);
        
        Button cancelBtn = new Button("Отмена");
        Button putMasterBtn = new Button("Назначить мастера");
        HBox btnsBox = new HBox();

        btnsBox.setSpacing(50);
        btnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);
        
        btnsBox.getChildren().addAll(cancelBtn, putMasterBtn);
        root.getChildren().addAll(dateLbl, choosingMaster, btnsBox);

        cancelBtn.setOnAction(event -> dialog.close());
        // putMasterBtn.setOnAction(event -> {

        // });



        Scene dialogScene = new Scene(root, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }





    public static void showAddNewClientDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить клиента");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addClientBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Имя: ");
        Label surnameHeadLbl = new Label("Фамилия: ");
        Label patronymicHeadLbl = new Label("Отчество: ");
        Label phoneHeadLbl = new Label("Номер телефона: ");
        Label birthdayHeadLbl = new Label("Дата рождения: ");
        
        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField phoneTextField = new TextField();
        DatePicker birthdayDatePicker = new DatePicker();


        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        addClientBtn.setText("Добавить клиента");
        addClientBtn.setWrapText(true);
        addClientBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.add(nameHeadLbl, 0, 0);
        table.add(surnameHeadLbl, 0, 1);
        table.add(patronymicHeadLbl, 0, 2);
        table.add(phoneHeadLbl, 0, 3);
        table.add(birthdayHeadLbl, 0, 4);
        
        table.add(nameTextField, 1, 0);
        table.add(surnameTextField, 1, 1);
        table.add(patronymicTextField, 1, 2);
        table.add(phoneTextField, 1, 3);
        table.add(birthdayDatePicker, 1, 4);



        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(birthdayHeadLbl, HPos.CENTER);
        GridPane.setValignment(birthdayHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(birthdayDatePicker, HPos.CENTER);
        GridPane.setValignment(birthdayDatePicker, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        
        cancelBtn.setOnAction(event -> dialog.close());
        addClientBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String phone = phoneTextField.getText();
            LocalDate birthday = birthdayDatePicker.getValue();
            
            ClientInfo client = new ClientInfo();
            
            client.setName(name);
            client.setSurname(surname);
            client.setPatronymic(patronymic);
            client.setPhone(phone);
            client.setBirthday(birthday);
            
            Response checkInfo = client.checkInfo();
            
            if(checkInfo.getCode() == -1){
                errorMsg.setText(checkInfo.getMsg());
                return;
            }

            Response addingClientResponse = Connection.addNewClient(token, client);
            if(addingClientResponse.getCode() == 200) dialog.close();
            else{errorMsg.setText(addingClientResponse.getMsg());}
        });


        btnsBox.getChildren().addAll(cancelBtn, addClientBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);



        Scene dialogScene = new Scene(root, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }


}






