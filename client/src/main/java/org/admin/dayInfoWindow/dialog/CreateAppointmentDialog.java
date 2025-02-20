package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetOption;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.utils.*;
import org.admin.utils.entities.Appointment;
import org.admin.utils.entities.Master;
import org.admin.utils.entities.Option;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateAppointmentDialog extends Main {
    public static void show(Master master, LocalDate date, Integer startCell, List<Appointment> appointments){
        //TODO При создании записи запрашивать номер телефона, если есть,
        // то создавать готовые Label с инфой о клиенте и подтверждать, если нет
        // создавать TextField для создания нового клиента
        Appointment appointment = new Appointment();
        appointment.setServices(new ArrayList<>());


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



        ScrollPane table = buildTableServices(appointment.getServices());

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

        buttonsBox.getChildren().addAll(cancelButton, addServiceButton, createAppointmentButton);
        root.getChildren().addAll(masterFioLabel, dateLabel, startTimeLabel, tableBox, buttonsBox);

        Scene dialogScene = new Scene(root, 1200, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

    }

    public static ScrollPane buildTableServices(List<Option> options){
        //TODO Кнопка удаления услуги из таблицы
        ScrollPane scrollPane = new ScrollPane();
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
        Label serviceCountTableHeadLabel = new Label("Количество");
        table.addRow(0,
                serviceIdTableHeadLabel,
                serviceNameTableHeadLabel,
                servicePriceTableHeadLabel,
                serviceCountTableHeadLabel);
        GridPane.setHalignment(serviceIdTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(serviceIdTableHeadLabel, VPos.CENTER);
        GridPane.setHalignment(serviceNameTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(serviceNameTableHeadLabel, VPos.CENTER);
        GridPane.setHalignment(servicePriceTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(servicePriceTableHeadLabel, VPos.CENTER);
        GridPane.setHalignment(serviceCountTableHeadLabel, HPos.CENTER);
        GridPane.setValignment(serviceCountTableHeadLabel, VPos.CENTER);

        int rowNum = 1;
        for (Option option : options) {
            Long seriveId = option.getId();
            String seriveName = option.getName();
            Long serivePrice = option.getPrice();
            Integer serviceCount = 1;

            Label serviceIdLabel = new Label(seriveId.toString());
            Label serviceNameLabel = new Label(seriveName);
            Label servicePriceLabel = new Label(serivePrice.toString());
            Label serviceCountLabel = new Label(serviceCount.toString());

            table.addRow(rowNum,
                    serviceIdLabel,
                    serviceNameLabel,
                    servicePriceLabel,
                    serviceCountLabel);

            GridPane.setHalignment(serviceIdLabel, HPos.CENTER);
            GridPane.setValignment(serviceIdLabel, VPos.CENTER);
            GridPane.setHalignment(serviceNameLabel, HPos.CENTER);
            GridPane.setValignment(serviceNameLabel, VPos.CENTER);
            GridPane.setHalignment(servicePriceLabel, HPos.CENTER);
            GridPane.setValignment(servicePriceLabel, VPos.CENTER);
            GridPane.setHalignment(serviceCountLabel, HPos.CENTER);
            GridPane.setValignment(serviceCountLabel, VPos.CENTER);
            rowNum++;
        }
        return scrollPane;
    }
}
