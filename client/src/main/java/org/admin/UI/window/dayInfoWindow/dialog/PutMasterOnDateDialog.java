package org.admin.UI.window.dayInfoWindow.dialog;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.controller.AdminController;
import org.admin.connection.getRequests.GetMaster;
import org.admin.connection.postRequests.PutMasterOnDate;
import org.admin.UI.components.searchingStrings.SearchingStringMasters;
import org.admin.UI.window.enterpriseWindow.dialog.creation.CreateMasterDialog;
import org.admin.model.Master;
import org.admin.model.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PutMasterOnDateDialog extends Main {
    public static void show(LocalDate date, Node node) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        String dateStr = dtf.format(date);

        Stage dialog            = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Назначить мастера");

        Label errorMsg          = new Label("");
        Label dateLbl           = new Label(dateStr);


        Button cancelBtn = new Button("Отмена");
        Button createMasterBtn = new Button("Создать нового мастера");
        HBox btnsBox = new HBox();

        btnsBox.setSpacing(50);
        btnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        List<Master> mastersNotOnDate = GetMaster.getListByDate(token, date, false);

        VBox choosingMaster = SearchingStringMasters.build(mastersNotOnDate, masterInfo -> {
            if(masterInfo != null) {
                Long masterId = masterInfo.getId();
                Response response = PutMasterOnDate.post(token, masterId, date);
                if(response.getCode() == 200) {
                    dialog.close();
                    AdminController.loadDayInfoWindow(node, date);
                } else {
                    errorMsg.setText(response.getCode() + " " + response.getMsg());
                }
            }
        });



        btnsBox.getChildren().addAll(cancelBtn, createMasterBtn);
        root.getChildren().addAll(dateLbl, choosingMaster, errorMsg, btnsBox);

        cancelBtn.setOnAction(event -> dialog.close());
        createMasterBtn.setOnAction(event -> {
            dialog.close();
            CreateMasterDialog.show(node);
        });




        Scene dialogScene = new Scene(root, 1200, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
