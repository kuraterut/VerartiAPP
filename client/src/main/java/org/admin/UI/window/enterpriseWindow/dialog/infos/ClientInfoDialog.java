package org.admin.UI.window.enterpriseWindow.dialog.infos;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.controller.AdminController;
import org.admin.connection.deleteRequests.DeleteClient;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.connection.getRequests.GetClient;
import org.admin.connection.putRequests.UpdateClient;
import org.admin.model.Response;
import org.admin.model.Appointment;
import org.admin.model.Client;
import org.admin.model.Option;

import java.util.List;
import java.util.Optional;

public class ClientInfoDialog extends Main {
    public static void show(Long clientId, Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о Клиенте");

        Client client = GetClient.getById(token, clientId);

        List<Appointment> clientAppointments = GetAppointment.getListByClientId(token, clientId);

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
            for(Option option : appointment.getOptions()){
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

        Button cancelBtn = new Button("Отмена");
        Button deleteButton = new Button("Удалить");
        Button saveBtn = new Button("Сохранить");

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


        saveBtn.setWrapText(true);
        saveBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        deleteButton.setWrapText(true);
        deleteButton.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(20);
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
        saveBtn.setOnAction(event -> {
            Client newClient = new Client();
            newClient.setName(nameTextField.getText());
            newClient.setSurname(surnameTextField.getText());
            newClient.setPatronymic(patronymicTextField.getText());
            newClient.setPhone(phoneTextField.getText());
            newClient.setBirthday(birthdayDatePicker.getValue());
            newClient.setComment(commentsArea.getText());
            Response response = UpdateClient.updateInfo(token, newClient);
            if(response.getCode() == 200){errorMsg.setText("Сохранено");}
            else{errorMsg.setText(response.getMsg());}
        });
        deleteButton.setOnAction(event -> {
            if(!showDeleteClientConfirmation()) return;
            Response response = DeleteClient.deleteById(token, client.getId());
            if(response.getCode() == 200){
                dialog.close();
                AdminController.loadEnterpriseWindow(node);
            }
            else{errorMsg.setText(response.getMsg());}
        });

        appointmentsTable.setAlignment(Pos.CENTER);
        appointmentsScrollPane.setFitToWidth(true);
        appointmentsScrollPane.setFitToHeight(true);


        btnsBox.getChildren().addAll(cancelBtn, deleteButton, saveBtn);
        root.getChildren().addAll(headLbl, table, appointmentsScrollPane, commentsArea, errorMsg, btnsBox);
        Scene dialogScene = new Scene(root, 1200, 700);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static boolean showDeleteClientConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Удаление клиента");
        alert.setContentText("Вы уверены, что хотите безвозвратно удалить клиента?");

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
