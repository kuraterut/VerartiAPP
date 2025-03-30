package org.admin.UI.window.dayInfoWindow.dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.UI.components.searchingStrings.SearchingStringOptions;
import org.admin.model.Appointment;
import org.admin.model.Option;

import java.util.List;

public class AddOptionToAppoinmentDialog extends Main {
    public static void show(VBox tableBox, Appointment appointment, List<Option> options){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить услугу");

        VBox root = new VBox();

        Label headerLabel = new Label("Добавьте услугу");
        VBox searchingStringBox = SearchingStringOptions.build(options, service->{
            if(service != null){
                appointment.addOption(service);
                tableBox.getChildren().clear();
                ScrollPane scrollPane = new ScrollPane();
                CreateAppointmentDialog.buildTableServices(scrollPane, appointment);
                tableBox.getChildren().add(scrollPane);
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

    public static void show(Appointment appointment, List<Option> options){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить услугу");

        VBox root = new VBox();

        Label headerLabel = new Label("Добавьте услугу");
        VBox searchingStringBox = SearchingStringOptions.build(options, service->{
            if(service != null){
                appointment.addOption(service);
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
