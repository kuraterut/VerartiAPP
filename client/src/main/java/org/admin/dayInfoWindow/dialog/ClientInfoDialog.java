package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.connection.getRequests.GetClient;
import org.admin.utils.entities.Appointment;
import org.admin.utils.entities.Client;
import org.admin.utils.entities.Option;

import java.util.ArrayList;
import java.util.List;

public class ClientInfoDialog extends Main {
    public static void show(Long clientId){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о клиенте");

        Client client = GetClient.getById(token, clientId);

        List<Appointment> clientAppointments = GetAppointment.getListByClientId(token, clientId);

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
            for(Option option : appointment.getServices()){
                Label serviceNameLbl = new Label(option.getName());
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
}
