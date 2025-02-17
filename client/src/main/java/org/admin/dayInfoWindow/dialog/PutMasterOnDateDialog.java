package org.admin.dayInfoWindow.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.Main;
import org.admin.connection.Connection;
import org.admin.connection.getRequests.GetMaster;
import org.admin.connection.postRequests.PutMasterOnDate;
import org.admin.dayInfoWindow.searchingStrings.SearchingStringMasters;
import org.admin.utils.MasterInfo;
import org.admin.utils.Response;
import org.admin.utils.SearchingStringListenerMasters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PutMasterOnDateDialog extends Main {
    public static void show(LocalDate date) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM yyyy", Locale.ENGLISH);
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

        List<MasterInfo> mastersNotOnDate = GetMaster.getListByDate(token, date, false);

        VBox choosingMaster = SearchingStringMasters.build(mastersNotOnDate, masterInfo -> {
            if(masterInfo != null) {
                Long masterId = masterInfo.getId();
                Response response = PutMasterOnDate.post(token, masterId, date);
                if(response.getCode() == 200) {
                    dialog.close();
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
            CreateMasterDialog.show();
        });




        Scene dialogScene = new Scene(root, 500, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
