package org.admin.dayInfoWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.AdminInterface;
import org.admin.connection.Connection;
import org.admin.connection.postRequests.AddNewClient;
import org.admin.utils.ClientInfo;
import org.admin.utils.Response;

import java.time.LocalDate;

public class AddNewClientDialog extends Main {
    public static void show(Node node, LocalDate date) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить клиента");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addClientBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Имя: ");
        Label surnameHeadLbl = new Label("Фамилия: ");
        Label patronymicHeadLbl = new Label("Отчество: ");
        Label phoneHeadLbl = new Label("Номер телефона: ");
        Label birthdayHeadLbl = new Label("Дата рождения: ");

        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField phoneTextField = new TextField();
        DatePicker birthdayDatePicker = new DatePicker();


        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        addClientBtn.setText("Добавить клиента");
        addClientBtn.setWrapText(true);
        addClientBtn.setTextAlignment(TextAlignment.CENTER);

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
        table.add(birthdayHeadLbl, 0, 4);

        table.add(nameTextField, 1, 0);
        table.add(surnameTextField, 1, 1);
        table.add(patronymicTextField, 1, 2);
        table.add(phoneTextField, 1, 3);
        table.add(birthdayDatePicker, 1, 4);



        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(birthdayHeadLbl, HPos.CENTER);
        GridPane.setValignment(birthdayHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(birthdayDatePicker, HPos.CENTER);
        GridPane.setValignment(birthdayDatePicker, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));

        cancelBtn.setOnAction(event -> dialog.close());
        addClientBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String phone = phoneTextField.getText();
            LocalDate birthday = birthdayDatePicker.getValue();

            ClientInfo client = new ClientInfo();

            client.setName(name);
            client.setSurname(surname);
            client.setPatronymic(patronymic);
            client.setPhone(phone);
            client.setBirthday(birthday);

            Response checkInfo = client.checkInfo();

            if(checkInfo.getCode() == -1){
                errorMsg.setText(checkInfo.getMsg());
                return;
            }

            Response addingClientResponse = AddNewClient.post(token, client);
            if(addingClientResponse.getCode() == 200) {
                AdminInterface.loadDayInfoWindow(node, date);
                dialog.close();
            }
            else{errorMsg.setText(addingClientResponse.getMsg());}
        });


        btnsBox.getChildren().addAll(cancelBtn, addClientBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);



        Scene dialogScene = new Scene(root, 500, 500);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
