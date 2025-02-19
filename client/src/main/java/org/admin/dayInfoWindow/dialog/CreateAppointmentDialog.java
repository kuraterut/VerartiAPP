package org.admin.dayInfoWindow.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.Main;
import org.admin.dayInfoWindow.searchingStrings.SearchingStringServices;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.utils.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateAppointmentDialog extends Main {
    private static Integer rowNum = 1;

    public static void show(MasterInfo master, LocalDate date, Integer startCell, List<Appointment> appointments){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать запись");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        LocalTime startTime = DayInfoTable.startCellToStartTime(startCell);

        List<ServiceInfo> services = master.getServices();
        if(services == null){services = new ArrayList<>();}

        Label masterFioLabel = new Label(master.getFio());
        Label dateLabel = new Label(HelpFuncs.localDateToString(date, "dd.MM.yyyy"));
        Label startTimeLabel = new Label(startTime.toString());


        GridPane table = new GridPane();
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

        VBox searchBox = SearchingStringServices.build(services, serviceInfo -> {
            if(serviceInfo != null){
                Long seriveId = serviceInfo.getId();
                String seriveName = serviceInfo.getName();
                Long serivePrice = serviceInfo.getPrice();
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
        });
        table.setAlignment(Pos.CENTER);
        table.setGridLinesVisible(true);


        root.getChildren().addAll(masterFioLabel, dateLabel, startTimeLabel, searchBox, table);

        Scene dialogScene = new Scene(root, 700, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
