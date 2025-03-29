package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.model.Appointment;
import org.admin.model.Client;
import org.admin.model.Master;
import org.admin.model.Option;
import org.admin.utils.HelpFuncs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentInfoDialog extends Main {
    public static void show(Long appointmentId, Node node) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о записи");

        Appointment appointment = GetAppointment.getById(token, appointmentId);
        Master master = appointment.getMaster();
        Client client = appointment.getClient();
        List<Option> options = appointment.getOptions();
        LocalDateTime dateTime = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());

        String dateTimeStr = HelpFuncs.localDateTimeToString(dateTime, "yyyy-MM-dd HH:mm");

        Label dateTimeLbl = new Label(dateTimeStr);
        Label appointmentHeadLbl = new Label("Запись №"+appointmentId);
        Label masterLbl = new Label("Мастер: " + master.getFio());
        Label clientLbl = new Label("Клиент: " + client.toString());
        Label servicesLbl = new Label("Услуги:");

        ScrollPane servicesScrollPane = new ScrollPane();
        servicesScrollPane.setPrefViewportHeight(300);
        servicesScrollPane.setPrefViewportWidth(400);
        servicesScrollPane.setFitToHeight(true);
        servicesScrollPane.setFitToWidth(true);

        GridPane servicesTable = new GridPane();
        servicesTable.setGridLinesVisible(true);
        servicesTable.setAlignment(Pos.CENTER);

        servicesScrollPane.setContent(servicesTable);

        Label serviceHeadTable = new Label("Услуга");
        Label timeHeadTable = new Label("Рассчетное время");
        Label priceHeadTable = new Label("Прайс");

        servicesTable.add(serviceHeadTable, 0, 0);
        servicesTable.add(timeHeadTable, 1, 0);
        servicesTable.add(priceHeadTable, 2, 0);

        int numServiceRow = 0;
        for(Option option : options){
            numServiceRow++;
            Label serviceLbl = new Label(option.getName());
            Label durationLbl = new Label(HelpFuncs.localTimeToString(option.getDuration(), "HH:mm"));
            Label priceLbl = new Label(Double.toString(option.getPrice()));
            servicesTable.add(serviceLbl, 0, numServiceRow);
            servicesTable.add(durationLbl, 1, numServiceRow);
            servicesTable.add(priceLbl, 2, numServiceRow);
            GridPane.setHalignment(serviceLbl, HPos.CENTER);
            GridPane.setValignment(serviceLbl, VPos.CENTER);
            GridPane.setHalignment(durationLbl, HPos.CENTER);
            GridPane.setValignment(durationLbl, VPos.CENTER);
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
        commentsArea.setMaxWidth(400);
        commentsArea.setMaxHeight(200);

        //TODO Отмена услуги
        //TODO Оплата услуги
        //TODO Обновить инфу о записи
        HBox bottomBtnsBox = new HBox();
        Button closeBtn = new Button("Закрыть");
        Button cancelOptionBtn = new Button("Отмена Записи");
        Button saveAppointmentBtn = new Button("Сохранить");
        Button paymentBtn = new Button("Оплата");
        closeBtn.setOnAction(event -> dialog.close());

        bottomBtnsBox.getChildren().addAll(closeBtn, cancelOptionBtn, addServiceBtn, saveAppointmentBtn, paymentBtn);
        bottomBtnsBox.setSpacing(100);
        bottomBtnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15);
        root.setMaxWidth(800);

        dateTimeLbl.setAlignment(Pos.TOP_RIGHT);
        appointmentHeadLbl.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(dateTimeLbl, appointmentHeadLbl);
        root.getChildren().addAll(masterLbl, clientLbl);
        root.getChildren().addAll(servicesLbl, servicesScrollPane, commentsArea, bottomBtnsBox);

        Scene dialogScene = new Scene(root, 1000, 800);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
