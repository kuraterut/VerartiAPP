package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import org.admin.connection.Connection;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.utils.Appointment;
import org.admin.utils.ClientInfo;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentInfoDialog extends Main {
    public static void show(Long appointmentId) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о записи");

        Appointment appointment = GetAppointment.getById(token, appointmentId);
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
            Label durationLbl = new Label(service.getDurationString());
            Label priceLbl = new Label(Double.toString(service.getPrice()));
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
}
