package org.admin.UI.window.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.deleteRequests.DeleteAppointment;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.connection.getRequests.GetOption;
import org.admin.connection.putRequests.UpdateAppointment;
import org.admin.controller.AdminController;
import org.admin.model.*;
import org.admin.utils.HelpFuncs;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentInfoDialog extends Main {
    public static void show(Long appointmentId, Node node) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о записи");

        Appointment appointment = GetAppointment.getById(token, appointmentId);
        User master = appointment.getMaster();
        Client client = appointment.getClient();
        LocalDateTime dateTime = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());

        String dateTimeStr = HelpFuncs.localDateTimeToString(dateTime, "yyyy-MM-dd HH:mm");

        Label dateTimeLbl = new Label(dateTimeStr);
        Label appointmentHeadLbl = new Label("Запись №"+appointmentId);
        Label masterLbl = new Label("Мастер: " + master.getFio());
        Label clientLbl = new Label("Клиент: " + client.toString());
        Label servicesLbl = new Label("Услуги:");
        Label messageLabel = new Label("");

        if(appointment.getCode() != 200) messageLabel.setText(appointment.getCode()+" "+appointment.getMsg());

        ScrollPane optionsInfoScrollPane = new ScrollPane();
        optionsInfoScrollPane.setPrefViewportHeight(300);
        optionsInfoScrollPane.setPrefViewportWidth(400);
        optionsInfoScrollPane.setFitToHeight(true);
        optionsInfoScrollPane.setFitToWidth(true);
        buildOptionsTable(optionsInfoScrollPane, appointment);

        TextArea commentsArea = new TextArea();
        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setText(appointment.getComment());
        commentsArea.setMinWidth(400);
        commentsArea.setMinHeight(200);
        commentsArea.setMaxWidth(400);
        commentsArea.setMaxHeight(200);

        //TODO Оплата услуги
        HBox bottomBtnsBox = new HBox();
        Button closeBtn = new Button("Закрыть");
        Button cancelAppointmentBtn = new Button("Отмена Записи");
        Button addOptionBtn = new Button("Добавить услугу");
        Button saveAppointmentBtn = new Button("Сохранить");
        Button paymentBtn = new Button("Оплата");

        closeBtn.setOnAction(event -> {
            dialog.close();
            AdminController.loadDayInfoWindow(node, appointment.getDate());
        });

        cancelAppointmentBtn.setOnAction(event -> {
            Response response = DeleteAppointment.deleteById(token, appointment.getId());
            if(response.getCode() == 200) {
                dialog.close();
                AdminController.loadDayInfoWindow(node, appointment.getDate());
            }
            else messageLabel.setText(response.getMsg());
        });

        addOptionBtn.setOnAction(event -> {
            AddOptionToAppoinmentDialog.show(appointment, GetOption.getListByMasterId(token, master.getId()));
            buildOptionsTable(optionsInfoScrollPane, appointment);
        });

        paymentBtn.setOnAction(event -> AppointmentPaymentDialog.show(appointment, node, dialog));

        saveAppointmentBtn.setOnAction(event -> {
            if(appointment.getOptions().isEmpty()){
                messageLabel.setText("Список услуг не может быть пустым");
                return;
            }
            appointment.setComment(commentsArea.getText());

            Response response = UpdateAppointment.updateInfo(token, appointment);
            if (response.getCode() == 200) messageLabel.setText("Сохранено");
            else messageLabel.setText(response.getMsg());
        });

        bottomBtnsBox.getChildren().addAll(closeBtn, cancelAppointmentBtn, addOptionBtn, saveAppointmentBtn, paymentBtn);
        bottomBtnsBox.setSpacing(50);
        bottomBtnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15);
        root.setMaxWidth(800);

        dateTimeLbl.setAlignment(Pos.TOP_RIGHT);
        appointmentHeadLbl.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(dateTimeLbl, appointmentHeadLbl);
        root.getChildren().addAll(masterLbl, clientLbl);
        root.getChildren().addAll(servicesLbl, optionsInfoScrollPane, commentsArea, messageLabel, bottomBtnsBox);

        Scene dialogScene = new Scene(root, 1200, 800);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }


    public static void buildOptionsTable(ScrollPane scrollPane, Appointment appointment) {
        //Options table
        GridPane optionsTable = new GridPane();
        Label optionNameHeadLabel = new Label("Услуга");
        Label optionTimeHeadLabel = new Label("Длительность");
        Label optionPriceHeadLabel = new Label("Прайс");

        optionsTable.addRow(0, optionNameHeadLabel, optionTimeHeadLabel, optionPriceHeadLabel);
        optionsTable.getColumnConstraints().add(new ColumnConstraints(200));
        optionsTable.getColumnConstraints().add(new ColumnConstraints(100));
        optionsTable.getColumnConstraints().add(new ColumnConstraints(100));
        optionsTable.getColumnConstraints().add(new ColumnConstraints(60));

        GridPane.setValignment(optionNameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionNameHeadLabel, HPos.CENTER);
        GridPane.setValignment(optionTimeHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionTimeHeadLabel, HPos.CENTER);
        GridPane.setValignment(optionPriceHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionPriceHeadLabel, HPos.CENTER);

        List<Option> options = appointment.getOptions();

        int optionsRowNum = 1;
        for(Option option : options) {
            Label optionNameLabel = new Label(option.getName());
            Label optionDurationLabel = new Label(HelpFuncs.localTimeToString(option.getDuration(), "HH:mm"));
            Label optionPriceLabel = new Label(Long.toString(option.getPrice()));
            Button deleteOptionBtn = new Button("Удалить");

            optionsTable.addRow(optionsRowNum, optionNameLabel, optionDurationLabel, optionPriceLabel, deleteOptionBtn);

            GridPane.setValignment(optionNameLabel, VPos.CENTER);
            GridPane.setHalignment(optionNameLabel, HPos.CENTER);
            GridPane.setValignment(optionDurationLabel, VPos.CENTER);
            GridPane.setHalignment(optionDurationLabel, HPos.CENTER);
            GridPane.setValignment(optionPriceLabel, VPos.CENTER);
            GridPane.setHalignment(optionPriceLabel, HPos.CENTER);
            GridPane.setValignment(deleteOptionBtn, VPos.CENTER);
            GridPane.setHalignment(deleteOptionBtn, HPos.CENTER);

            final int rowNumForDeleteBtn = optionsRowNum;
            deleteOptionBtn.setOnAction(event -> {
                appointment.getOptions().remove(rowNumForDeleteBtn-1);
                buildOptionsTable(scrollPane, appointment);
            });
            optionsRowNum++;
        }


        optionsTable.setAlignment(Pos.CENTER);

        optionsTable.setGridLinesVisible(true);

        scrollPane.setContent(optionsTable);
    }
}
