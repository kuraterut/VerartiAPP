package org.admin.enterpriseWindow.dialog.infos;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.deleteRequests.DeleteOption;
import org.admin.connection.getRequests.GetOption;
import org.admin.connection.putRequests.UpdateOption;
import org.admin.utils.Response;
import org.admin.utils.entities.Option;

public class OptionInfoDialog extends Main {
    public static void show(Long id, Node node){
        Option option = GetOption.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация об Услуге");

        VBox root = new VBox();
        Label messageLabel = new Label("");

        Button cancelButton = new Button("Отмена");
        Button deleteButton = new Button("Удалить услугу");
        Button saveButton = new Button("Сохранить");

        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(50);

        GridPane infoTable = new GridPane();
        Label idLabel           = new Label("ID");
        Label nameLabel         = new Label("Имя: ");
        Label priceLabel        = new Label("Прайс: ");
        Label durationLabel     = new Label("Время: ");
        Label descriptionLabel        = new Label("Описание: ");


        Label serviceIdLabel            = new Label(option.getId().toString());
        TextField nameTextField         = new TextField(option.getName());
        TextField priceTextField        = new TextField(option.getPrice().toString());
        TextField durationTextField     = new TextField(option.getDurationString());
        TextArea descriptionTextArea    = new TextArea(option.getDescription());

        infoTable.getColumnConstraints().add(new ColumnConstraints(150));
        infoTable.getColumnConstraints().add(new ColumnConstraints(200));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(150));

        infoTable.addColumn(0,
                idLabel, nameLabel, priceLabel, durationLabel, descriptionLabel);

        infoTable.addColumn(1,
                serviceIdLabel, nameTextField, priceTextField, durationTextField, descriptionTextArea);

        GridPane.setHalignment(idLabel, HPos.CENTER);
        GridPane.setValignment(idLabel, VPos.CENTER);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setValignment(nameLabel, VPos.CENTER);
        GridPane.setHalignment(priceLabel, HPos.CENTER);
        GridPane.setValignment(priceLabel, VPos.CENTER);
        GridPane.setHalignment(durationLabel, HPos.CENTER);
        GridPane.setValignment(durationLabel, VPos.CENTER);
        GridPane.setHalignment(descriptionLabel, HPos.CENTER);
        GridPane.setValignment(descriptionLabel, VPos.CENTER);


        GridPane.setHalignment(serviceIdLabel, HPos.CENTER);
        GridPane.setValignment(serviceIdLabel, VPos.CENTER);
        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(priceTextField, HPos.CENTER);
        GridPane.setValignment(priceTextField, VPos.CENTER);
        GridPane.setHalignment(durationTextField, HPos.CENTER);
        GridPane.setValignment(durationTextField, VPos.CENTER);
        GridPane.setHalignment(descriptionTextArea, HPos.CENTER);
        GridPane.setValignment(descriptionTextArea, VPos.CENTER);

        infoTable.setAlignment(Pos.CENTER);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        btns.getChildren().addAll(cancelButton, deleteButton, saveButton);
        root.getChildren().addAll(infoTable, messageLabel, btns);

        cancelButton.setOnAction(event -> dialog.close());

        deleteButton.setOnAction(event -> {
            //TODO ALERT DELETE
            Response response = DeleteOption.deleteById(token, option.getId());
            if(response.getCode() == 200){
                dialog.close();
                AdminInterface.loadEnterpriseWindow(node);
            }
            else{messageLabel.setText(response.getMsg());}
        });

        saveButton.setOnAction(event -> {
            Response response = UpdateOption.updateInfo(token, option);
            if(response.getCode() == 200){messageLabel.setText("Сохранено");}
            else{messageLabel.setText(response.getMsg());}
        });

        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
