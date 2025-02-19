package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.postRequests.CreateUser;
import org.admin.utils.Response;
import org.admin.utils.User;

public class CreateMasterDialog extends Main {
    public static void show(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать Мастера");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addMasterBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Имя: ");
        Label surnameHeadLbl = new Label("Фамилия: ");
        Label patronymicHeadLbl = new Label("Отчество: ");
        Label emailHeadLbl = new Label("Email: ");
        Label phoneHeadLbl = new Label("Номер телефона: ");
        Label passwordHeadLbl = new Label("Пароль: ");

        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField passwordTextField = new TextField();

        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        addMasterBtn.setText("Создать мастера");
        addMasterBtn.setWrapText(true);
        addMasterBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.add(nameHeadLbl, 0, 0);
        table.add(surnameHeadLbl, 0, 1);
        table.add(patronymicHeadLbl, 0, 2);
        table.add(phoneHeadLbl, 0, 3);
        table.add(emailHeadLbl, 0, 4);
        table.add(passwordHeadLbl, 0, 5);

        table.add(nameTextField, 1, 0);
        table.add(surnameTextField, 1, 1);
        table.add(patronymicTextField, 1, 2);
        table.add(phoneTextField, 1, 3);
        table.add(emailTextField, 1, 4);
        table.add(passwordTextField, 1, 5);




        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(emailHeadLbl, HPos.CENTER);
        GridPane.setValignment(emailHeadLbl, VPos.CENTER);
        GridPane.setHalignment(passwordHeadLbl, HPos.CENTER);
        GridPane.setValignment(passwordHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(emailTextField, HPos.CENTER);
        GridPane.setValignment(emailTextField, VPos.CENTER);
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
        addMasterBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String phone = phoneTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();

            User user = new User();

            user.setName(name);
            user.setSurname(surname);
            user.setPatronymic(patronymic);
            user.setPhone(phone);
            user.setEmail(email);
            user.setPassword(password);
            user.addRole("master");

            Response checkInfo = user.checkInfo();

            if(checkInfo.getCode() == -1){
                errorMsg.setText(checkInfo.getMsg());
                return;
            }


            Response addingClientResponse = CreateUser.post(token, user);
            if(addingClientResponse.getCode() == 200) dialog.close();
            else{errorMsg.setText(addingClientResponse.getMsg());}
        });


        btnsBox.getChildren().addAll(cancelBtn, addMasterBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);



        Scene dialogScene = new Scene(root, 500, 500);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
