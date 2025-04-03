package org.admin.UI.window.enterpriseWindow.dialog.creation;

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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.controller.AdminController;
import org.admin.connection.postRequests.CreateOption;
import org.admin.model.Response;
import org.admin.model.Option;
import org.admin.utils.validation.DurationValidation;
import org.admin.utils.validation.PriceValidation;
import org.admin.utils.validation.Validation;

import java.time.LocalTime;


public class CreateOptionDialog extends Main {
    public static void show(Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать Услугу");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addBtn = new Button();

        Label errorMsg = new Label("");
        Label nameHeadLbl = new Label("Название: ");
        Label priceHeadLbl = new Label("Прайс: ");
        Label durationHeadLbl = new Label("Время (hh:mm): ");
        Label descriptionHeadLbl = new Label("Описание: ");

        TextField nameTextField = new TextField();
        TextField priceTextField = new TextField();
        TextField durationTextField = new TextField();
        TextArea descriptionTextArea = new TextArea();

        table.setAlignment(Pos.CENTER);

        addBtn.setText("Создать Услугу");
        addBtn.setWrapText(true);
        addBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.addColumn(0, nameHeadLbl, priceHeadLbl, durationHeadLbl, descriptionHeadLbl);
        table.addColumn(1, nameTextField, priceTextField, durationTextField, descriptionTextArea);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(priceHeadLbl, HPos.CENTER);
        GridPane.setValignment(priceHeadLbl, VPos.CENTER);
        GridPane.setHalignment(durationHeadLbl, HPos.CENTER);
        GridPane.setValignment(durationHeadLbl, VPos.CENTER);
        GridPane.setHalignment(descriptionHeadLbl, HPos.CENTER);
        GridPane.setValignment(descriptionHeadLbl, VPos.CENTER);


        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(priceTextField, HPos.CENTER);
        GridPane.setValignment(priceTextField, VPos.CENTER);
        GridPane.setHalignment(durationTextField, HPos.CENTER);
        GridPane.setValignment(durationTextField, VPos.CENTER);
        GridPane.setHalignment(descriptionTextArea, HPos.CENTER);
        GridPane.setValignment(descriptionTextArea, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(200));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(100));

        cancelBtn.setOnAction(event -> dialog.close());
        addBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String priceStr = priceTextField.getText();
            String durationStr = durationTextField.getText();

            Validation priceValidation = new PriceValidation(priceStr);
            Validation durationValidation = new DurationValidation(durationStr);
            if(!priceValidation.validate()) {errorMsg.setText("Неправильный формат прайса, должно быть целое число"); return;}
            if(!durationValidation.validate()) {errorMsg.setText("Неверный формат времени(ячейки по 30 минут)"); return;}

            Long price = Long.parseLong(priceStr);;
            LocalTime duration = LocalTime.parse(durationStr);

            String description = descriptionTextArea.getText();

            Option option = new Option();

            option.setName(name);
            option.setPrice(price);
            option.setDuration(duration);
            option.setDescription(description);

            Response response = CreateOption.post(token, option);
            if(response.getCode() == 200) {
                dialog.close();
                AdminController.loadEnterpriseWindow(node);
            }
            if(response.getCode() == 401){
                dialog.close();
                AdminController.loadAuthorizationWindow(node);
            }
            errorMsg.setText(response.getMsg());
        });


        btnsBox.getChildren().addAll(cancelBtn, addBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);

        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}

