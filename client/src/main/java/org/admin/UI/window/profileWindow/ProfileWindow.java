package org.admin.UI.window.profileWindow;

import org.Main;
import org.admin.UI.window.profileWindow.dialog.ChangePasswordDialog;
import org.admin.UI.window.profileWindow.dialog.ChangeProfileInfoDialog;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.getRequests.GetPhoto;
import org.admin.connection.putRequests.UpdateProfile;
import org.admin.controller.AdminController;
import org.admin.model.Response;
import org.admin.UI.components.sideMenu.SideMenu;

import javafx.stage.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.geometry.*;
import javafx.event.*;
import org.admin.model.Admin;

import java.io.*;

public class ProfileWindow extends Main{
    public static BorderPane loadProfileWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(3);
        GridPane table              = new GridPane();
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();
        VBox avatarBox              = new VBox();

        HBox profileInfoBox         = new HBox();
        HBox changeInfoBtnsBox      = new HBox();

        Button changeAvatarBtn      = new Button();
        Button changePasswordBtn    = new Button();
        Button changeInfoBtn        = new Button();

        Label nameHeadLbl               = new Label();
        Label surnameHeadLbl            = new Label();
        Label patronymicHeadLbl         = new Label();
        Label phoneHeadLbl              = new Label();
        Label bioHeadLbl                = new Label();
        Label roleHeadLbl               = new Label();

        Label nameLbl                 = new Label();
        Label surnameLbl               = new Label();
        Label patronymicLbl            = new Label();
        Label phoneLbl                 = new Label();
        Label bioLbl                   = new Label();
        Label roleLbl                  = new Label();
       
        Label title                 = new Label();

        
        title.setText("Профиль");
        nameHeadLbl.setText("Имя");
        surnameHeadLbl.setText("Фамилия");
        patronymicHeadLbl.setText("Отчество");
        phoneHeadLbl.setText("Телефон");
        bioHeadLbl.setText("Биография");
        roleHeadLbl.setText("Роль");

        Admin admin = GetAdmin.getByPhone(token, Main.login);
        Image avatarImage = GetPhoto.getProfilePhoto(admin.getPhotoURL(), properties);

        nameLbl.setText(admin.getName());
        surnameLbl.setText(admin.getSurname());
        patronymicLbl.setText(admin.getPatronymic());
        phoneLbl.setText(admin.getPhone());
        bioLbl.setText(admin.getBio());
        roleLbl.setText("Администратор");
        
        ImageView avatarImageView = new ImageView(avatarImage);
        Circle circle = new Circle(avatarImageView.getImage().getHeight()/20);
        avatarImageView.setFitHeight(avatarImageView.getImage().getHeight()/10);
        avatarImageView.setFitWidth(avatarImageView.getImage().getWidth()/10);
        avatarImageView.setPreserveRatio(true);
        avatarImageView.setClip(circle);
        circle.setCenterX(avatarImageView.getImage().getWidth()/20);
        circle.setCenterY(avatarImageView.getImage().getHeight()/20);

        table.add(nameHeadLbl, 0, 0);
        table.add(nameLbl, 1, 0);
        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(nameLbl, HPos.CENTER);
        GridPane.setValignment(nameLbl, VPos.CENTER);

        table.add(surnameHeadLbl, 0, 1);
        table.add(surnameLbl, 1, 1);
        GridPane.setHalignment(surnameHeadLbl, HPos.CENTER);
        GridPane.setValignment(surnameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(surnameLbl, HPos.CENTER);
        GridPane.setValignment(surnameLbl, VPos.CENTER);

        table.add(patronymicHeadLbl, 0, 2);
        table.add(patronymicLbl, 1, 2);
        GridPane.setHalignment(patronymicHeadLbl, HPos.CENTER);
        GridPane.setValignment(patronymicHeadLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicLbl, HPos.CENTER);
        GridPane.setValignment(patronymicLbl, VPos.CENTER);


        table.add(phoneHeadLbl, 0, 3);
        table.add(phoneLbl, 1, 3);
        GridPane.setHalignment(phoneHeadLbl, HPos.CENTER);
        GridPane.setValignment(phoneHeadLbl, VPos.CENTER);
        GridPane.setHalignment(phoneLbl, HPos.CENTER);
        GridPane.setValignment(phoneLbl, VPos.CENTER);
        
        table.add(bioHeadLbl, 0, 4);
        table.add(bioLbl, 1, 4);
        GridPane.setHalignment(bioHeadLbl, HPos.CENTER);
        GridPane.setValignment(bioHeadLbl, VPos.CENTER);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);

        table.add(roleHeadLbl, 0, 5);
        table.add(roleLbl, 1, 5);
        GridPane.setHalignment(roleHeadLbl, HPos.CENTER);
        GridPane.setValignment(roleHeadLbl, VPos.CENTER);
        GridPane.setHalignment(roleLbl, HPos.CENTER);
        GridPane.setValignment(roleLbl, VPos.CENTER);


        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        changeAvatarBtn.setText("Сменить фото");
        changePasswordBtn.setText("Сменить пароль");
        changeInfoBtn.setText("Изменить данные");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);
        avatarBox.setSpacing(30);
        profileInfoBox.setSpacing(100);
        changeInfoBtnsBox.setSpacing(50);

        avatarBox.setAlignment(Pos.CENTER);
        centerBox.setAlignment(Pos.TOP_CENTER);
        table.setAlignment(Pos.CENTER);
        profileInfoBox.setAlignment(Pos.CENTER);
        changeInfoBtnsBox.setAlignment(Pos.CENTER);

        changeInfoBtn.setOnAction(event -> ChangeProfileInfoDialog.show(admin, changeInfoBtn));
        changePasswordBtn.setOnAction(event -> ChangePasswordDialog.show(admin, changePasswordBtn));

        changeAvatarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                
                fileChooser.setTitle("Выберите файл .jpg");
                
                FileChooser.ExtensionFilter extFilter1 = 
                    new FileChooser.ExtensionFilter("Картинки (*.jpg)", "*.jpg");
                
                fileChooser.getExtensionFilters().add(extFilter1);
                fileChooser.setInitialDirectory(new File("C:/"));
                File file = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());
                if (file != null) {
                    System.out.println("Выбранный файл: " + file.getAbsolutePath());
                    Response status = UpdateProfile.updateProfilePhoto(token, file);
                    //TODO Проверять response и сделать messageLabel
                }
                AdminController.loadProfileWindow(changeAvatarBtn);
            }
        });

        avatarBox.getChildren().addAll(avatarImageView, changeAvatarBtn);
        changeInfoBtnsBox.getChildren().addAll(changePasswordBtn, changeInfoBtn);
        profileInfoBox.getChildren().addAll(avatarBox, table);
        centerBox.getChildren().addAll(title, profileInfoBox, changeInfoBtnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }
}