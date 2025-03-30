package org.admin.UI.window.enterpriseWindow.dialog.creation;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.controller.AdminController;
import org.admin.connection.postRequests.CreateMaster;
import org.admin.model.Response;
import org.admin.model.Master;

public class CreateMasterDialog extends Main {
    public static void show(Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать Мастера");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Имя: ");
        Label surnameHeadLbl = new Label("Фамилия: ");
        Label patronymicHeadLbl = new Label("Отчество: ");
        Label emailHeadLbl = new Label("Email: ");
        Label phoneHeadLbl = new Label("Телефон: ");
        Label passwordHeadLbl = new Label("Пароль: ");

        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField passwordTextField = new TextField();

        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        addBtn.setText("Создать мастера");
        addBtn.setWrapText(true);
        addBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.addColumn(0, nameHeadLbl, surnameHeadLbl, patronymicHeadLbl, emailHeadLbl, phoneHeadLbl, passwordHeadLbl);
        table.addColumn(1, nameTextField, surnameTextField, patronymicTextField, emailTextField, phoneTextField, passwordTextField);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(emailHeadLbl, HPos.CENTER);
        GridPane.setValignment(emailHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(passwordHeadLbl, HPos.CENTER);
        GridPane.setValignment(passwordHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(emailTextField, HPos.CENTER);
        GridPane.setValignment(emailTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(passwordTextField, HPos.CENTER);
        GridPane.setValignment(passwordTextField, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));

        cancelBtn.setOnAction(event -> dialog.close());
        addBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String email = emailTextField.getText();
            String phone = phoneTextField.getText();
            String password = passwordTextField.getText();

            Master master = new Master();

            master.setName(name);
            master.setSurname(surname);
            master.setPatronymic(patronymic);
            master.setPhone(phone);
            master.setPassword(password);


            Response checkInfo = master.checkInfo();

            if(checkInfo.getCode() == -1){
                errorMsg.setText(checkInfo.getMsg());
                return;
            }

            Response response = CreateMaster.post(token, master);
            if(response.getCode() == 200) {
                AdminController.loadEnterpriseWindow(node);
                dialog.close();
            }
            else{errorMsg.setText(response.getMsg());}
        });


        btnsBox.getChildren().addAll(cancelBtn, addBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);



        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
