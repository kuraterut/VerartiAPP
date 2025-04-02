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
import org.admin.model.Response;
import org.admin.model.User;

public class ChangePasswordDialog extends Main {
    public static void show(User admin, Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Смена пароля");

        Label title = new Label("Новый пароль");
        Label messageLabel = new Label("");

        GridPane table = new GridPane();
        table.setAlignment(Pos.CENTER);

        Label oldPasswordHeadLabel = new Label("Старый пароль: ");
        Label newPasswordHeadLabel = new Label("Новый пароль: ");
        TextField oldPasswordTextField = new TextField();
        TextField newPasswordTextField = new TextField();

        table.add(oldPasswordHeadLabel, 0, 0);
        table.add(oldPasswordTextField, 1, 0);
        table.add(newPasswordHeadLabel, 0, 1);
        table.add(newPasswordTextField, 1, 1);

        table.getColumnConstraints().add(new ColumnConstraints(100));
        table.getColumnConstraints().add(new ColumnConstraints(200));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));

        GridPane.setValignment(oldPasswordHeadLabel, VPos.CENTER);
        GridPane.setHalignment(oldPasswordHeadLabel, HPos.CENTER);
        GridPane.setValignment(oldPasswordTextField, VPos.CENTER);
        GridPane.setHalignment(oldPasswordTextField, HPos.CENTER);
        GridPane.setValignment(newPasswordHeadLabel, VPos.CENTER);
        GridPane.setHalignment(newPasswordHeadLabel, HPos.CENTER);
        GridPane.setValignment(newPasswordTextField, VPos.CENTER);
        GridPane.setHalignment(newPasswordTextField, HPos.CENTER);

        Button cancelBtn = new Button("Отмена");
        Button confirmedBtn = new Button("Подтвердить");

        cancelBtn.setOnAction(e -> dialog.close());

        confirmedBtn.setOnAction(e -> {
            String oldPassword = oldPasswordTextField.getText();
            String newPassword = newPasswordTextField.getText();
            Response response = UpdateProfile.updatePassword(token, oldPassword, newPassword);
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

        Scene dialogScene = new Scene(root, 600, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
