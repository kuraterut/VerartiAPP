package org.admin.UI.window.enterpriseWindow.dialog.creation;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.postRequests.CreateUser;
import org.admin.controller.AdminController;
import org.admin.model.Response;
import org.admin.model.User;
import org.admin.utils.UserRole;
import org.admin.utils.validation.PhoneNumberValidation;

import java.util.HashSet;

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
        Label phoneHeadLbl = new Label("Телефон: ");
        Label passwordHeadLbl = new Label("Пароль: ");
        Label isAdminLbl = new Label("Является Админ: ");
        Label bioLbl = new Label("Биография: ");

        TextField nameTextField = new TextField();
        TextField surnameTextField = new TextField();
        TextField patronymicTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField passwordTextField = new TextField();
        RadioButton isAdminYesRadioButton = new RadioButton("ДА");
        RadioButton isAdminNoRadioButton = new RadioButton("НЕТ");
        ToggleGroup isAdminGroup = new ToggleGroup();
        isAdminYesRadioButton.setToggleGroup(isAdminGroup);
        isAdminNoRadioButton.setToggleGroup(isAdminGroup);
        HBox isAdminBox = new HBox(20);
        isAdminBox.getChildren().addAll(isAdminYesRadioButton, isAdminNoRadioButton);
        isAdminBox.setAlignment(Pos.CENTER);

        TextArea bioTextArea = new TextArea();
        bioTextArea.setWrapText(true);
        bioTextArea.setScrollLeft(Double.MAX_VALUE);
        bioTextArea.setMinWidth(200);
        bioTextArea.setMinHeight(100);
        bioTextArea.setMaxWidth(200);
        bioTextArea.setMaxHeight(100);

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

        table.addColumn(0, nameHeadLbl, surnameHeadLbl, patronymicHeadLbl,  phoneHeadLbl, passwordHeadLbl, isAdminLbl, bioLbl);
        table.addColumn(1, nameTextField, surnameTextField, patronymicTextField, phoneTextField, passwordTextField, isAdminBox, bioTextArea);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(passwordHeadLbl, HPos.CENTER);
        GridPane.setValignment(passwordHeadLbl, VPos.CENTER);
        GridPane.setHalignment(isAdminLbl, HPos.CENTER);
        GridPane.setValignment(isAdminLbl, VPos.CENTER);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(surnameTextField, HPos.CENTER);
        GridPane.setValignment(surnameTextField, VPos.CENTER);
        GridPane.setHalignment(patronymicTextField, HPos.CENTER);
        GridPane.setValignment(patronymicTextField, VPos.CENTER);
        GridPane.setHalignment(phoneTextField, HPos.CENTER);
        GridPane.setValignment(phoneTextField, VPos.CENTER);
        GridPane.setHalignment(passwordTextField, HPos.CENTER);
        GridPane.setValignment(passwordTextField, VPos.CENTER);
        GridPane.setHalignment(isAdminBox, HPos.CENTER);
        GridPane.setValignment(isAdminBox, VPos.CENTER);
        GridPane.setHalignment(bioTextArea, HPos.CENTER);
        GridPane.setValignment(bioTextArea, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(100));
        table.getRowConstraints().add(new RowConstraints(100));

        cancelBtn.setOnAction(event -> dialog.close());
        addBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patronymic = patronymicTextField.getText();
            String phone = phoneTextField.getText();
            String password = passwordTextField.getText();
            String bio = bioTextArea.getText();

            if(name.isEmpty()) {errorMsg.setText("Имя не может быть пустым"); return;}
            if(surname.isEmpty()) {errorMsg.setText("Фамилия не может быть пустой"); return;}
            if(!new PhoneNumberValidation(phone).validate()) {errorMsg.setText("Некорректный формат номера телефона(+7...)"); return;}

            User master = new User();

            master.setName(name);
            master.setSurname(surname);
            master.setPatronymic(patronymic);
            master.setPhone(phone);
            master.setPassword(password);
            master.setRoles(new HashSet<>());
            master.addRole(UserRole.MASTER);
            if(isAdminYesRadioButton.isSelected()) master.addRole(UserRole.ADMIN);
            master.setBio(bio);

            Response response = CreateUser.post(token, master);
            if(response.getCode() == 200) {
                dialog.close();
                AdminController.loadEnterpriseWindow(node);
            }
            if(response.getCode() == 409){
                errorMsg.setText("Пользователь с таким номером уже существует");
                return;
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
