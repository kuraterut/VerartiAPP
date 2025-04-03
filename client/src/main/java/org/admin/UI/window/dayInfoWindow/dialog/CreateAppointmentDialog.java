package org.admin.UI.window.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.UI.components.searchingStrings.SearchingStringClients;
import org.admin.controller.AdminController;
import org.admin.connection.getRequests.GetClient;
import org.admin.connection.getRequests.GetOption;
import org.admin.connection.postRequests.CreateAppointment;
import org.admin.UI.window.dayInfoWindow.tables.DayInfoTable;
import org.admin.model.*;
import org.admin.utils.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CreateAppointmentDialog extends Main {
    public static void show(User master, LocalDate date, Integer startCell, List<Appointment> appointments, Node node){
        Appointment appointment = new Appointment();
        appointment.setOptions(new ArrayList<>());

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать запись");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        LocalTime startTime = DayInfoTable.startCellToStartTime(startCell);

        List<Option> options = GetOption.getListByMasterId(token, master.getId());

        Label masterFioLabel = new Label(master.getFio());
        Label dateLabel = new Label(HelpFuncs.localDateToString(date, "dd.MM.yyyy"));
        Label startTimeLabel = new Label(startTime.toString());
        Label clientPhoneLabel = new Label("Телефон клиента: ");
        Label messageLabel = new Label("");

        TextArea commentsArea = new TextArea();
        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setMinWidth(400);
        commentsArea.setMinHeight(200);
        commentsArea.setMaxWidth(400);
        commentsArea.setMaxHeight(200);
        commentsArea.setPrefWidth(400);
        commentsArea.setPrefHeight(200);

//        TextField clientPhoneTextField = new TextField();

        HBox clientPhoneBox = new HBox(20);
        Client client = new Client();
        client.setCode(404);
        client.setMsg("Клиент не выбран");
        VBox searchingStringClient = SearchingStringClients.build(GetClient.getAll(token), chosenClient -> {
            client.setId(chosenClient.getId());
            client.setName(chosenClient.getName());
            client.setSurname(chosenClient.getSurname());
            client.setPatronymic(chosenClient.getPatronymic());
            client.setPhone(chosenClient.getPhone());
            client.setBirthday(chosenClient.getBirthday());
            client.setComment(chosenClient.getComment());
            client.setCode(200);
        });
        searchingStringClient.setMaxHeight(100);

        clientPhoneBox.getChildren().addAll(clientPhoneLabel, searchingStringClient);
        clientPhoneBox.setAlignment(Pos.CENTER);

        ScrollPane table = new ScrollPane();

        buildTableServices(table, appointment);

        VBox tableBox = new VBox();
        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().add(table);

        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(50);
        Button cancelButton = new Button("Отмена");
        Button addServiceButton = new Button("Добавить услугу");
        Button createAppointmentButton = new Button("Создать");

        cancelButton.setOnAction(event -> dialog.close());
        addServiceButton.setOnAction(event -> AddOptionToAppoinmentDialog.show(tableBox, appointment, options));
        createAppointmentButton.setOnAction(event -> {
            if(appointment.getOptions().isEmpty()){
                messageLabel.setText("Список услуг не может быть пустым");
                return;
            }
            if(DayInfoTable.calculateCellNumber(appointment.getOptions()) > calculatePossibleCellsCount(appointments, startCell)){
                boolean userConfirmed = showTooCloseConfirmation();
                if(!userConfirmed) return;
            }

            if(client.getCode() != 200){messageLabel.setText(client.getMsg()); return;}
            appointment.setClient(client);
            appointment.setMaster(master);
            appointment.setStartTime(startTime);
            appointment.setDate(date);
            appointment.setComment(commentsArea.getText());
            Response response = CreateAppointment.post(token, appointment);
            if(response.getCode() == 200) {
                dialog.close();
                AdminController.loadDayInfoWindow(node, date);
            }
            if(response.getCode() == 401){
                dialog.close();
                AdminController.loadAuthorizationWindow(node);
            }
            messageLabel.setText(response.getMsg());
        });

        buttonsBox.getChildren().addAll(cancelButton, addServiceButton, createAppointmentButton);
        root.getChildren().addAll(masterFioLabel, dateLabel, startTimeLabel, clientPhoneBox, tableBox, commentsArea, messageLabel, buttonsBox);

        Scene dialogScene = new Scene(root, 1600, 800);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static void buildTableServices(ScrollPane scrollPane, Appointment appointment){
        GridPane table = new GridPane();
        scrollPane.setContent(table);
        table.setGridLinesVisible(true);
        scrollPane.setPrefViewportHeight(300);
        scrollPane.setPrefViewportWidth(600);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        table.setAlignment(Pos.CENTER);

        Label serviceIdTableHeadLabel = new Label("ID Услуги");
        Label serviceNameTableHeadLabel = new Label("Наименование услуги");
        Label servicePriceTableHeadLabel = new Label("Стоимость");
        table.addRow(0,
                serviceIdTableHeadLabel,
                serviceNameTableHeadLabel,
                servicePriceTableHeadLabel);
        GridPane.setHalignment(serviceIdTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(serviceIdTableHeadLabel, VPos.CENTER);
        GridPane.setHalignment(serviceNameTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(serviceNameTableHeadLabel, VPos.CENTER);
        GridPane.setHalignment(servicePriceTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(servicePriceTableHeadLabel, VPos.CENTER);

        int rowNum = 1;
        for (Option option : appointment.getOptions()) {
            Long optionId = option.getId();
            String optionName = option.getName();
            Long optionPrice = option.getPrice();

            Label optionIdLabel = new Label(optionId.toString());
            Label optionNameLabel = new Label(optionName);
            Label optionPriceLabel = new Label(optionPrice.toString());
            Button deleteOptionBtn = new Button("Удалить");

            table.addRow(rowNum,
                    optionIdLabel,
                    optionNameLabel,
                    optionPriceLabel,
                    deleteOptionBtn);

            GridPane.setHalignment(optionIdLabel, HPos.CENTER);
            GridPane.setValignment(optionIdLabel, VPos.CENTER);
            GridPane.setHalignment(optionNameLabel, HPos.CENTER);
            GridPane.setValignment(optionNameLabel, VPos.CENTER);
            GridPane.setHalignment(optionPriceLabel, HPos.CENTER);
            GridPane.setValignment(optionPriceLabel, VPos.CENTER);
            GridPane.setHalignment(deleteOptionBtn, HPos.CENTER);
            GridPane.setValignment(deleteOptionBtn, VPos.CENTER);

            final int rowNumForDeleteBtn = rowNum;
            deleteOptionBtn.setOnAction(event -> {
                appointment.getOptions().remove(rowNumForDeleteBtn-1);
                buildTableServices(scrollPane, appointment);
            });

            rowNum++;
        }
    }

    public static int calculatePossibleCellsCount(List<Appointment> appointments, int startCell){
        List<Integer> startCells = new ArrayList<>();
        for (Appointment appointment : appointments) {
            Integer startCellHelp = DayInfoTable.calculateCellStart(appointment.getStartTime());
            if(startCellHelp > startCell){startCells.add(startCellHelp);}
        }
        startCells.add(29);
        int nearestCell = Collections.min(startCells);
        return nearestCell-startCell;
    }

    public static boolean showTooCloseConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Запись слишком близко");
        alert.setContentText("Следующая запись расположена очень близко. Всё равно создать?");

        // Настраиваем кнопки
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        // Ждём выбора пользователя
        Optional<ButtonType> result = alert.showAndWait();

        // Возвращаем true, если нажата OK
        return result.isPresent() && result.get() == buttonTypeOk;
    }


}
