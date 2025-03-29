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
import org.admin.connection.deleteRequests.DeleteAdmin;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.putRequests.UpdateAdmin;
import org.admin.model.Admin;
import org.admin.model.Response;

public class AdminInfoDialog extends Main {
    public static void show(Long id, Node node){
        Admin admin = GetAdmin.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация об Админе");

        VBox root = new VBox();
        Label messageLabel = new Label("");

        Button cancelButton = new Button("Отмена");
        Button deleteButton = new Button("Удалить админа");
        Button saveButton = new Button("Сохранить");

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
        TextField nameTextField         = new TextField(admin.getName());
        TextField surnameTextField      = new TextField(admin.getSurname());
        TextField patronymicTextField   = new TextField(admin.getPatronymic());
        TextField phoneTextField        = new TextField(admin.getPhone());
        TextArea bioTextArea            = new TextArea(admin.getBio());

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

        btns.getChildren().addAll(cancelButton, deleteButton, saveButton);
        root.getChildren().addAll(infoTable, messageLabel, btns);


        cancelButton.setOnAction(event -> dialog.close());

        deleteButton.setOnAction(event -> {
            //TODO ALERT DELETE
            Response response = DeleteAdmin.deleteById(token, admin.getId());
            if(response.getCode() == 200){
                dialog.close();
                AdminInterface.loadEnterpriseWindow(node);
            }
            else{messageLabel.setText(response.getMsg());}
        });

        saveButton.setOnAction(event -> {
            Admin newAdmin = new Admin();
            newAdmin.setName(nameTextField.getText());
            newAdmin.setSurname(surnameTextField.getText());
            newAdmin.setPatronymic(patronymicTextField.getText());
            newAdmin.setPhone(phoneTextField.getText());
            newAdmin.setBio(bioTextArea.getText());

            Response response = UpdateAdmin.updateInfo(token, newAdmin);
            if(response.getCode() == 200){messageLabel.setText("Сохранено");}
            else{messageLabel.setText(response.getMsg());}
        });

        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
