package org.admin.UI.window.dayInfoWindow.dialog;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetUser;
import org.admin.controller.AdminController;
import org.admin.connection.postRequests.PutAdminOnDate;
import org.admin.UI.components.searchingStrings.SearchingStringAdmins;
import org.admin.UI.window.enterpriseWindow.dialog.creation.CreateAdminDialog;
import org.admin.model.Response;
import org.admin.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PutAdminOnDateDialog extends Main {
    public static void show(LocalDate date, Node node) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        String dateStr = dtf.format(date);

        Stage dialog            = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Назначить Админа");

        Label errorMsg          = new Label("");
        Label dateLbl           = new Label(dateStr);


        Button cancelBtn = new Button("Отмена");
        Button createAdminBtn = new Button("Создать Админа");
        HBox btnsBox = new HBox();

        btnsBox.setSpacing(50);
        btnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        List<User> adminsNotOnDate = GetUser.getAllAdmins(token);
        VBox choosingAdmin = SearchingStringAdmins.build(adminsNotOnDate, admin -> {
            if(admin != null) {
                Long adminId = admin.getId();
                Response response = PutAdminOnDate.post(token, adminId, date);
                if(response.getCode() == 200) {
                    dialog.close();
                    AdminController.loadDayInfoWindow(node, date);
                }
                if(response.getCode() == 401){
                    dialog.close();
                    AdminController.loadAuthorizationWindow(node);
                }
                errorMsg.setText(response.getMsg());
            }
        });



        btnsBox.getChildren().addAll(cancelBtn, createAdminBtn);
        root.getChildren().addAll(dateLbl, choosingAdmin, errorMsg, btnsBox);

        cancelBtn.setOnAction(event -> dialog.close());
        createAdminBtn.setOnAction(event -> {
            dialog.close();
            CreateAdminDialog.show(node);
        });

        Scene dialogScene = new Scene(root, 1200, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
