package org.admin.dayInfoWindow.dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.enterpriseWindow.searchingStrings.SearchingStringServices;
import org.admin.utils.entities.Appointment;
import org.admin.utils.entities.Option;

import java.util.List;

public class AddServiceToAppoinmentDialog extends Main {
    public static void show(VBox tableBox, Appointment appointment, List<Option> options){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить услугу");

        VBox root = new VBox();

        Label headerLabel = new Label("Добавьте услугу");
        VBox searchingStringBox = SearchingStringServices.build(options, service->{
            if(service != null){
                appointment.addService(service);
                tableBox.getChildren().removeAll();
                tableBox.getChildren().add(CreateAppointmentDialog.buildTableServices(appointment.getServices()));
                dialog.close();
            }
        });
        Button cancelButton = new Button("Отмена");

        cancelButton.setOnAction(event -> dialog.close());
        root.getChildren().addAll(headerLabel, searchingStringBox, cancelButton);
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);


        Scene dialogScene = new Scene(root, 1200, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
