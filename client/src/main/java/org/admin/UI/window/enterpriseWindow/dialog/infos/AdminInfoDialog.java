package org.admin.UI.window.enterpriseWindow.dialog.infos;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.deleteRequests.DeleteUser;
import org.admin.connection.getRequests.GetUser;
import org.admin.controller.AdminController;
import org.admin.model.Response;
import org.admin.model.User;

import java.util.Optional;

public class AdminInfoDialog extends Main {
    public static void show(Long id, Node node){
        User admin = GetUser.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация об Админе");

        VBox root = new VBox();
        Label messageLabel = new Label("");
        if(admin.getCode() != 200) messageLabel.setText(admin.getCode() + " " + admin.getMsg());

        Button cancelButton = new Button("Отмена");
        Button deleteButton = new Button("Удалить админа");

        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(50);

        GridPane infoTable = new GridPane();
        Label idLabel           = new Label("ID");
        Label nameLabel         = new Label("Имя: ");
        Label surnameLabel      = new Label("Фамилия: ");
        Label patronymicLabel   = new Label("Отчество: ");
        Label phoneLabel        = new Label("Телефон: ");
        Label bioLabel          = new Label("Биография: ");

        Label adminIdLabel             = new Label(admin.getId().toString());
        Label nameTextField         = new Label(admin.getName());
        Label surnameTextField      = new Label(admin.getSurname());
        Label patronymicTextField   = new Label(admin.getPatronymic());
        Label phoneTextField        = new Label(admin.getPhone());
        Label bioTextArea            = new Label(admin.getBio());

        infoTable.getColumnConstraints().add(new ColumnConstraints(150));
        infoTable.getColumnConstraints().add(new ColumnConstraints(200));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(150));

        infoTable.addColumn(0,
                idLabel, nameLabel, surnameLabel, patronymicLabel, phoneLabel, bioLabel);

        infoTable.addColumn(1,
                adminIdLabel, nameTextField, surnameTextField,
                patronymicTextField, phoneTextField, bioTextArea);

        GridPane.setHalignment(idLabel, HPos.CENTER);
        GridPane.setValignment(idLabel, VPos.CENTER);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setValignment(nameLabel, VPos.CENTER);
        GridPane.setHalignment(surnameLabel, HPos.CENTER);
        GridPane.setValignment(surnameLabel, VPos.CENTER);
        GridPane.setHalignment(patronymicLabel, HPos.CENTER);
        GridPane.setValignment(patronymicLabel, VPos.CENTER);
        GridPane.setHalignment(phoneLabel, HPos.CENTER);
        GridPane.setValignment(phoneLabel, VPos.CENTER);
        GridPane.setHalignment(bioLabel, HPos.CENTER);
        GridPane.setValignment(bioLabel, VPos.CENTER);

        GridPane.setHalignment(adminIdLabel, HPos.CENTER);
        GridPane.setValignment(adminIdLabel, VPos.CENTER);
        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(bioTextArea, HPos.CENTER);
        GridPane.setValignment(bioTextArea, VPos.CENTER);
        infoTable.setAlignment(Pos.CENTER);



        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        btns.getChildren().addAll(cancelButton, deleteButton);
        root.getChildren().addAll(infoTable, messageLabel, btns);


        cancelButton.setOnAction(event -> dialog.close());

        deleteButton.setOnAction(event -> {
            if(!showDeleteAdminConfirmation()) return;
            Response response = DeleteUser.deleteById(token, admin.getId());
            if(response.getCode() == 200){
                dialog.close();
                AdminController.loadEnterpriseWindow(node);
            }
            if(response.getCode() == 401){
                dialog.close();
                AdminController.loadAuthorizationWindow(node);
            }
            messageLabel.setText(response.getMsg());
        });


        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static boolean showDeleteAdminConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Удаление администратора");
        alert.setContentText("Вы уверены, что хотите безвозвратно удалить администратора?");

        // Настраиваем кнопки
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        // Ждём выбора пользователя
        Optional<ButtonType> result = alert.showAndWait();

        // Возвращаем true, если нажата OK
        return result.isPresent() && result.get() == buttonTypeOk;
    }
}
