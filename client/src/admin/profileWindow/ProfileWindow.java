package src.admin.profileWindow;

import src.Main;
import src.admin.AdminInterface;
import src.admin.connection.*;
import src.admin.connection.profile.*;
import src.admin.sideMenu.SideMenu;
import src.admin.utils.*;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.control.Alert.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.animation.*;
import javafx.collections.*;
import javafx.util.*;

import java.io.*;
import java.util.*;

public class ProfileWindow extends Main{
	public static BorderPane loadChangeProfileWindow(){
        BorderPane root                 = new BorderPane();
        StackPane sideMenuStack         = SideMenu.buildSideMenu(2);
        GridPane table                  = new GridPane();

        VBox rightBox                   = new VBox();
        VBox centerBox                  = new VBox();

        HBox btnsBox                    = new HBox();

        TextField nameField             = new TextField();
        TextField surnameField          = new TextField();
        TextField patronymicField       = new TextField();
        TextField emailField            = new TextField();
        TextField phoneField            = new TextField();
        TextField bioField              = new TextField();

        Label title                     = new Label(); 
        Label nameLbl                   = new Label();
        Label surnameLbl                = new Label();
        Label patronymicLbl             = new Label();
        Label emailLbl                  = new Label();
        Label phoneLbl                  = new Label();
        Label bioLbl                    = new Label();
        Label errorMsg                  = new Label();

        Button cancel                   = new Button();
        Button saveChanges              = new Button();

        
        title.setText("Изменение информации профиля");
        nameLbl.setText("Имя");
        surnameLbl.setText("Фамилия");
        patronymicLbl.setText("Отчество");
        emailLbl.setText("Email");
        phoneLbl.setText("Телефон");
        bioLbl.setText("Биография");
        errorMsg.setText("");

        cancel.setText("Отмена");
        saveChanges.setText("Сохранить изменения");

        rightBox.setPrefWidth(MENU_WIDTH);

        AdminInfo admin = (AdminInfo) ConnectionProfile.getProfileInfo(token);
        if(admin.getCode() == 200){
            nameField.setText(admin.getName());
            surnameField.setText(admin.getSurname());
            patronymicField.setText(admin.getPatronymic());
            emailField.setText(admin.getEmail());
            phoneField.setText(admin.getPhone());
            bioField.setText(admin.getBio());
        }

        

        table.add(nameLbl, 0, 0);
        table.add(nameField, 1, 0);
        GridPane.setHalignment(nameLbl, HPos.CENTER);
        GridPane.setValignment(nameLbl, VPos.CENTER);
        GridPane.setHalignment(nameField, HPos.CENTER);
        GridPane.setValignment(nameField, VPos.CENTER);

        table.add(surnameLbl, 0, 1);
        table.add(surnameField, 1, 1);
        GridPane.setHalignment(surnameLbl, HPos.CENTER);
        GridPane.setValignment(surnameLbl, VPos.CENTER);
        GridPane.setHalignment(surnameField, HPos.CENTER);
        GridPane.setValignment(surnameField, VPos.CENTER);

        table.add(patronymicLbl, 0, 2);
        table.add(patronymicField, 1, 2);
        GridPane.setHalignment(patronymicLbl, HPos.CENTER);
        GridPane.setValignment(patronymicLbl, VPos.CENTER);
        GridPane.setHalignment(patronymicField, HPos.CENTER);
        GridPane.setValignment(patronymicField, VPos.CENTER);

        table.add(emailLbl, 0, 3);
        table.add(emailField, 1, 3);
        GridPane.setHalignment(emailLbl, HPos.CENTER);
        GridPane.setValignment(emailLbl, VPos.CENTER);
        GridPane.setHalignment(emailField, HPos.CENTER);
        GridPane.setValignment(emailField, VPos.CENTER);

        table.add(phoneLbl, 0, 4);
        table.add(phoneField, 1, 4);
        GridPane.setHalignment(phoneLbl, HPos.CENTER);
        GridPane.setValignment(phoneLbl, VPos.CENTER);
        GridPane.setHalignment(phoneField, HPos.CENTER);
        GridPane.setValignment(phoneField, VPos.CENTER);
        
        table.add(bioLbl, 0, 5);
        table.add(bioField, 1, 5);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);
        GridPane.setHalignment(bioField, HPos.CENTER);
        GridPane.setValignment(bioField, VPos.CENTER);

        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setAlignment(Pos.TOP_CENTER);
        btnsBox.setAlignment(Pos.CENTER);

        btnsBox.setSpacing(100);
        centerBox.setSpacing(50);

        cancel.setOnAction(event -> AdminInterface.loadProfileWindow(cancel));
        
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newName          = nameField.getText();
                String newSurname       = surnameField.getText();
                String newPatronymic    = patronymicField.getText();
                String newEmail         = emailField.getText();
                String newPhone         = phoneField.getText();
                String newBio           = bioField.getText();

                AdminInfo newAdmin = new AdminInfo();
                newAdmin.setName(newName);
                newAdmin.setSurname(newSurname);
                newAdmin.setPatronymic(newPatronymic);
                newAdmin.setEmail(newEmail);
                newAdmin.setPhone(newPhone);
                newAdmin.setBio(newBio);

                Response response = ConnectionProfile.changeProfileInfo(token, newAdmin);
                if(response.getCode() == 200){AdminInterface.loadProfileWindow(cancel);}
                else {errorMsg.setText(response.getMsg());}
            }
        });


        btnsBox.getChildren().addAll(cancel, saveChanges);
        centerBox.getChildren().addAll(title, table, errorMsg, btnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public static BorderPane loadChangeProfilePasswordWindow(){
        BorderPane root                 = new BorderPane();
        StackPane sideMenuStack         = SideMenu.buildSideMenu(2);
        GridPane table                  = new GridPane();

        VBox rightBox                   = new VBox();
        VBox centerBox                  = new VBox();

        HBox btnsBox                    = new HBox();

        TextField oldPasswordField      = new TextField();
        TextField newPasswordField      = new TextField();
        
        Label title                     = new Label(); 
        Label oldPasswordLbl            = new Label();
        Label newPasswordLbl            = new Label();
        Label errorMsg                  = new Label();
        
        Button cancel                   = new Button();
        Button saveChanges              = new Button();

        
        title.setText("Изменение пароля профиля");
        errorMsg.setText("");
        oldPasswordLbl.setText("Старый пароль");
        newPasswordLbl.setText("Новый пароль");
        
        cancel.setText("Отмена");
        saveChanges.setText("Сохранить изменения");

        rightBox.setPrefWidth(MENU_WIDTH);

        table.add(oldPasswordLbl, 0, 0);
        table.add(oldPasswordField, 1, 0);
        GridPane.setHalignment(oldPasswordLbl, HPos.CENTER);
        GridPane.setValignment(oldPasswordLbl, VPos.CENTER);
        GridPane.setHalignment(oldPasswordField, HPos.CENTER);
        GridPane.setValignment(oldPasswordField, VPos.CENTER);

        table.add(newPasswordLbl, 0, 1);
        table.add(newPasswordField, 1, 1);
        GridPane.setHalignment(newPasswordLbl, HPos.CENTER);
        GridPane.setValignment(newPasswordLbl, VPos.CENTER);
        GridPane.setHalignment(newPasswordField, HPos.CENTER);
        GridPane.setValignment(newPasswordField, VPos.CENTER);

        table.setAlignment(Pos.CENTER);
        table.setVgap(15);
        table.setHgap(30);

        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setAlignment(Pos.TOP_CENTER);
        btnsBox.setAlignment(Pos.CENTER);

        btnsBox.setSpacing(100);
        centerBox.setSpacing(50);

        cancel.setOnAction(event -> AdminInterface.loadProfileWindow(cancel));
        
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String oldPassword      = oldPasswordField.getText();
                String newPassword       = newPasswordField.getText();
                
                Response response = ConnectionProfile.changeProfilePassword(token, oldPassword, newPassword);
                if(response.getCode() == 200){AdminInterface.loadProfileWindow(cancel);}
                else {errorMsg.setText(response.getMsg());}
            }
        });

        btnsBox.getChildren().addAll(cancel, saveChanges);
        centerBox.getChildren().addAll(title, table, errorMsg, btnsBox);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public static BorderPane loadProfileWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(2);
        GridPane table              = new GridPane();
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();
        VBox avatarBox              = new VBox();

        HBox profileInfoBox         = new HBox();
        HBox changeInfoBtnsBox      = new HBox();

        Button changeAvatarBtn      = new Button();
        Button changePasswordBtn    = new Button();
        Button changeInfoBtn        = new Button();

        Label nameLbl               = new Label();
        Label surnameLbl            = new Label();
        Label patronymicLbl         = new Label();
        Label birthdayLbl           = new Label();
        Label phoneLbl              = new Label();
        Label emailLbl              = new Label();
        Label bioLbl                = new Label();
        Label roleLbl               = new Label();
        Label curSalaryLbl          = new Label();

        Label name                  = new Label();
        Label surname               = new Label();
        Label patronymic            = new Label();
        Label birthday              = new Label();
        Label phone                 = new Label();
        Label email                 = new Label();
        Label bio                   = new Label();
        Label role                  = new Label();
       
        Label title                 = new Label();

        
        title.setText("Профиль");
        nameLbl.setText("Имя");
        surnameLbl.setText("Фамилия");
        patronymicLbl.setText("Отчество");
        birthdayLbl.setText("Дата рождения");
        phoneLbl.setText("Телефон");
        emailLbl.setText("Email");
        bioLbl.setText("Биография");
        roleLbl.setText("Роль");

        AdminInfo adminInfo = (AdminInfo)ConnectionProfile.getProfileInfo(token);
        
        Image avatarImage = null;
        
        if(adminInfo.getCode() != 200) {
            avatarImage = ConnectionProfile.getProfilePhoto(null);
            name.setText(adminInfo.getMsg());
            surname.setText(adminInfo.getMsg());
            patronymic.setText(adminInfo.getMsg());
            phone.setText(adminInfo.getMsg());
            email.setText(adminInfo.getMsg());
            bio.setText(adminInfo.getMsg());
            role.setText(adminInfo.getMsg());
        }
        else{
            avatarImage = ConnectionProfile.getProfilePhoto(adminInfo.getPhotoURL());
            name.setText(adminInfo.getName());
            surname.setText(adminInfo.getSurname());
            patronymic.setText(adminInfo.getPatronymic());
            phone.setText(adminInfo.getPhone());
            email.setText(adminInfo.getEmail());
            bio.setText(adminInfo.getBio());
            role.setText("Администратор");
        }
        
        ImageView avatarImageView = new ImageView(avatarImage);
        Circle circle = new Circle(avatarImageView.getImage().getHeight()/20);
        avatarImageView.setFitHeight(avatarImageView.getImage().getHeight()/10);
        avatarImageView.setFitWidth(avatarImageView.getImage().getWidth()/10);
        avatarImageView.setPreserveRatio(true);
        avatarImageView.setClip(circle);
        circle.setCenterX(avatarImageView.getImage().getWidth()/20);
        circle.setCenterY(avatarImageView.getImage().getHeight()/20);

        table.add(nameLbl, 0, 0);
        table.add(name, 1, 0);
        GridPane.setHalignment(nameLbl, HPos.CENTER);
        GridPane.setValignment(nameLbl, VPos.CENTER);
        GridPane.setHalignment(name, HPos.CENTER);
        GridPane.setValignment(name, VPos.CENTER);

        table.add(surnameLbl, 0, 1);
        table.add(surname, 1, 1);
        GridPane.setHalignment(surnameLbl, HPos.CENTER);
        GridPane.setValignment(surnameLbl, VPos.CENTER);
        GridPane.setHalignment(surname, HPos.CENTER);
        GridPane.setValignment(surname, VPos.CENTER);

        table.add(patronymicLbl, 0, 2);
        table.add(patronymic, 1, 2);
        GridPane.setHalignment(patronymicLbl, HPos.CENTER);
        GridPane.setValignment(patronymicLbl, VPos.CENTER);
        GridPane.setHalignment(patronymic, HPos.CENTER);
        GridPane.setValignment(patronymic, VPos.CENTER);

        table.add(birthdayLbl, 0, 3);
        table.add(birthday, 1, 3);
        GridPane.setHalignment(birthdayLbl, HPos.CENTER);
        GridPane.setValignment(birthdayLbl, VPos.CENTER);
        GridPane.setHalignment(birthday, HPos.CENTER);
        GridPane.setValignment(birthday, VPos.CENTER);

        table.add(phoneLbl, 0, 4);
        table.add(phone, 1, 4);
        GridPane.setHalignment(phoneLbl, HPos.CENTER);
        GridPane.setValignment(phoneLbl, VPos.CENTER);
        GridPane.setHalignment(phone, HPos.CENTER);
        GridPane.setValignment(phone, VPos.CENTER);

        table.add(emailLbl, 0, 5);
        table.add(email, 1, 5);
        GridPane.setHalignment(emailLbl, HPos.CENTER);
        GridPane.setValignment(emailLbl, VPos.CENTER);
        GridPane.setHalignment(email, HPos.CENTER);
        GridPane.setValignment(email, VPos.CENTER);
        
        table.add(bioLbl, 0, 6);
        table.add(bio, 1, 6);
        GridPane.setHalignment(bioLbl, HPos.CENTER);
        GridPane.setValignment(bioLbl, VPos.CENTER);
        GridPane.setHalignment(bio, HPos.CENTER);
        GridPane.setValignment(bio, VPos.CENTER);

        table.add(roleLbl, 0, 7);
        table.add(role, 1, 7);
        GridPane.setHalignment(roleLbl, HPos.CENTER);
        GridPane.setValignment(roleLbl, VPos.CENTER);
        GridPane.setHalignment(role, HPos.CENTER);
        GridPane.setValignment(role, VPos.CENTER);


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


        changeInfoBtn.setOnAction(event -> AdminInterface.loadChangeProfileInfoWindow(changeInfoBtn));
        changePasswordBtn.setOnAction(event -> AdminInterface.loadChangeProfilePasswordWindow(changePasswordBtn));

        changeAvatarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                
                fileChooser.setTitle("Выберите файл .jpg");
                
                FileChooser.ExtensionFilter extFilter1 = 
                    new FileChooser.ExtensionFilter("Картинки (*.jpg)", "*.jpg");
                
                fileChooser.getExtensionFilters().add(extFilter1);
                fileChooser.setInitialDirectory(new File("/home/"));
                File file = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());
                if (file != null) {
                    System.out.println("Выбранный файл: " + file.getAbsolutePath());
                    Response status = ConnectionProfile.changeProfilePhoto(token, file);
                }
                AdminInterface.loadProfileWindow(changeAvatarBtn);
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