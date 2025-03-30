package org.admin.UI.window.profileWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.putRequests.UpdateProfile;
import org.admin.controller.AdminController;
import org.admin.model.Admin;
import org.admin.model.Response;

public class ChangeProfileInfoDialog extends Main {
    public static void show(Admin admin, Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Изменение информации");

        Label title = new Label("Профиль");
        Label messageLabel = new Label("");

        GridPane table = new GridPane();
        table.setAlignment(Pos.CENTER);

        Label nameHeadLabel = new Label("Имя: ");
        Label surnameHeadLabel = new Label("Фамилия: ");
        Label patronymicHeadLabel = new Label("Отчество: ");
        Label bioHeadLabel = new Label("Биография: ");

        TextField nameTextField = new TextField(admin.getName());
        TextField surnameTextField = new TextField(admin.getSurname());
        TextField patronymicTextField = new TextField(admin.getPatronymic());
        TextField bioTextField = new TextField(admin.getBio());

        table.addColumn(0, nameHeadLabel, surnameHeadLabel, patronymicHeadLabel, bioHeadLabel);
        table.addColumn(1, nameTextField, surnameTextField, patronymicTextField, bioTextField);

        table.getColumnConstraints().add(new ColumnConstraints(100));
        table.getColumnConstraints().add(new ColumnConstraints(200));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));

        GridPane.setValignment(nameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(nameHeadLabel, HPos.CENTER);
        GridPane.setValignment(surnameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLabel, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLabel, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLabel, HPos.CENTER);
        GridPane.setValignment(bioHeadLabel, VPos.CENTER);
        GridPane.setHalignment(bioHeadLabel, HPos.CENTER);

        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(bioTextField, VPos.CENTER);
        GridPane.setHalignment(bioTextField, HPos.CENTER);

        Button cancelBtn = new Button("Отмена");
        Button confirmedBtn = new Button("Подтвердить");

        cancelBtn.setOnAction(e -> dialog.close());

        confirmedBtn.setOnAction(e -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String bio = bioTextField.getText();

            admin.setName(name);
            admin.setSurname(surname);
            admin.setPatronymic(patronymic);
            admin.setBio(bio);

            Response response = UpdateProfile.updateInfo(token, admin);
            if(response.getCode() == 200){
                dialog.close();
                AdminController.loadProfileWindow(node);
                return;
            }
            messageLabel.setText(response.getMsg());
        });

        HBox buttonsBox = new HBox(50, cancelBtn, confirmedBtn);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, table, messageLabel, buttonsBox);

        Scene dialogScene = new Scene(root, 800, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
