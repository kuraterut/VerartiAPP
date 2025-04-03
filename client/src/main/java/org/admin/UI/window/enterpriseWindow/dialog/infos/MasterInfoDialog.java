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
import org.admin.connection.deleteRequests.DeleteOption;
import org.admin.connection.getRequests.GetOption;
import org.admin.connection.postRequests.AddOptionToMaster;
import org.admin.UI.components.searchingStrings.SearchingStringOptions;
import org.admin.model.Response;
import org.admin.model.Option;
import org.admin.model.User;
import org.admin.utils.HelpFuncs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MasterInfoDialog extends Main {
    public static void show(Long id, Node node){
        User master = GetUser.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о Мастере");

        VBox root = new VBox();
        Label messageLabel = new Label("");

        Button cancelButton = new Button("Отмена");
        Button deleteMasterButton = new Button("Удалить мастера");
        Button addServiceButton = new Button("Добавить услугу");

        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(50);

        GridPane masterInfoTabel = new GridPane();
        Label idLabel           = new Label("ID");
        Label nameLabel         = new Label("Имя: ");
        Label surnameLabel      = new Label("Фамилия: ");
        Label patronymicLabel   = new Label("Отчество: ");
        Label phoneLabel        = new Label("Телефон: ");
        Label bioLabel          = new Label("Биография: ");
        Label servicesLabel     = new Label("Услуги: ");

        Label masterIdLabel             = new Label(master.getId().toString());
        TextField nameTextField         = new TextField(master.getName());
        TextField surnameTextField      = new TextField(master.getSurname());
        TextField patronymicTextField   = new TextField(master.getPatronymic());
        TextField phoneTextField        = new TextField(master.getPhone());
        TextArea bioTextArea            = new TextArea(master.getBio());

        masterInfoTabel.getColumnConstraints().add(new ColumnConstraints(150));
        masterInfoTabel.getColumnConstraints().add(new ColumnConstraints(200));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(50));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(50));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(50));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(50));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(50));
        masterInfoTabel.getRowConstraints().add(new RowConstraints(100));

        masterInfoTabel.addColumn(0,
                idLabel, nameLabel, surnameLabel, patronymicLabel, phoneLabel, bioLabel);

        masterInfoTabel.addColumn(1,
                masterIdLabel, nameTextField, surnameTextField,
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
        GridPane.setHalignment(masterIdLabel, HPos.CENTER);
        GridPane.setValignment(masterIdLabel, VPos.CENTER);
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

        masterInfoTabel.setAlignment(Pos.CENTER);



        ScrollPane servicesScrollPane = new ScrollPane();
        servicesScrollPane.setFitToWidth(true);
        servicesScrollPane.setFitToHeight(true);
        servicesScrollPane.setPrefViewportHeight(300);
        servicesScrollPane.setPrefViewportWidth(500);
        buildServiceTable(servicesScrollPane, master, dialog, node, messageLabel);


        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        btns.getChildren().addAll(cancelButton, deleteMasterButton, addServiceButton);
        root.getChildren().addAll(masterInfoTabel, servicesLabel, servicesScrollPane, messageLabel, btns);


        cancelButton.setOnAction(event -> dialog.close());

        deleteMasterButton.setOnAction(event -> {
            if(!showDeleteMasterConfirmation()) return;
            Response response = DeleteUser.deleteById(token, master.getId());
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

        addServiceButton.setOnAction(event -> {
            List<Option> allOptions = GetOption.getAll(token);
            List<Option> masterOptions = GetOption.getListByMasterId(token, master.getId());
            List<Option> notMasterOptions = new ArrayList<>();
            for(Option option : allOptions){
                if(!masterOptions.contains(option)){
                    notMasterOptions.add(option);
                }
            }
            showChooseServiceDialog(notMasterOptions, master, servicesScrollPane, node, messageLabel);
        });

        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private static void showChooseServiceDialog(List<Option> options, User master, ScrollPane servicesScrollPane, Node node, Label messageLabel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Выберите услугу");

        VBox root = new VBox();
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);
        Label headerLabel = new Label("Выберите услугу");
        Button cancelButton = new Button("Отмена");

        VBox searchingStringServices = SearchingStringOptions.build(options, service->{
            Response response = AddOptionToMaster.post(token, master.getId(), service.getId());
            if(response.getCode() == 200){
                buildServiceTable(servicesScrollPane, master, dialog, node, messageLabel);
                dialog.close();
            }
            if(response.getCode() == 401){
                dialog.close();
                AdminController.loadAuthorizationWindow(node);
            }
            else{
                dialog.close();
                messageLabel.setText(response.getMsg());
            }
        });
        searchingStringServices.setMaxWidth(500);

        cancelButton.setOnAction(event -> dialog.close());
        root.getChildren().addAll(headerLabel, searchingStringServices, messageLabel, cancelButton);

        Scene dialogScene = new Scene(root, 800, 500);

        dialog.setScene(dialogScene);
        dialog.showAndWait();

    }

    private static void buildServiceTable(ScrollPane servicesScrollPane, User master, Stage dialog, Node node, Label errorMsg){
        //TODO Удаление услуги у мастера
        GridPane servicesTable = new GridPane();
        Label[] servicesTableHeaders = new Label[4];
        servicesTableHeaders[0] = new Label("ID услуги");
        servicesTableHeaders[1] = new Label("Услуга");
        servicesTableHeaders[2] = new Label("Прайс");
        servicesTableHeaders[3] = new Label("Длительность");

        servicesTable.addRow(0, servicesTableHeaders);
        GridPane.setHalignment(servicesTableHeaders[0], HPos.CENTER);
        GridPane.setValignment(servicesTableHeaders[0], VPos.CENTER);
        GridPane.setHalignment(servicesTableHeaders[1], HPos.CENTER);
        GridPane.setValignment(servicesTableHeaders[1], VPos.CENTER);
        GridPane.setHalignment(servicesTableHeaders[2], HPos.CENTER);
        GridPane.setValignment(servicesTableHeaders[2], VPos.CENTER);
        GridPane.setHalignment(servicesTableHeaders[3], HPos.CENTER);
        GridPane.setValignment(servicesTableHeaders[3], VPos.CENTER);

        List<Option> masterOptions = GetOption.getListByMasterId(token, master.getId());
        int index = 1;
        for(Option option : masterOptions){
            Label serviceIdLabel = new Label(option.getId().toString());
            Label serviceNameLabel = new Label(option.getName());
            Label servicePriceLabel = new Label(option.getPrice().toString());
            Label serviceDurationLabel = new Label(HelpFuncs.localTimeToString(option.getDuration(), "HH:mm"));
            Button deleteService = new Button("Удалить");

            deleteService.setOnAction(event -> {
                Response response = DeleteOption.deleteByMasterId(token, option.getId(), master.getId());
                if(response.getCode() == 200){buildServiceTable(servicesScrollPane, master, dialog, node, errorMsg);}
                if(response.getCode() == 401){
                    dialog.close();
                    AdminController.loadAuthorizationWindow(node);
                }
                errorMsg.setText(response.getMsg());
            });

            servicesTable.addRow(index, serviceIdLabel, serviceNameLabel, servicePriceLabel, serviceDurationLabel, deleteService);
            GridPane.setValignment(serviceIdLabel, VPos.CENTER);
            GridPane.setHalignment(serviceIdLabel, HPos.CENTER);
            GridPane.setValignment(serviceNameLabel, VPos.CENTER);
            GridPane.setHalignment(serviceNameLabel, HPos.CENTER);
            GridPane.setValignment(servicePriceLabel, VPos.CENTER);
            GridPane.setHalignment(servicePriceLabel, HPos.CENTER);
            GridPane.setValignment(serviceDurationLabel, VPos.CENTER);
            GridPane.setHalignment(serviceDurationLabel, HPos.CENTER);
            GridPane.setValignment(deleteService, VPos.CENTER);
            GridPane.setHalignment(deleteService, HPos.CENTER);

            index++;
        }
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(200));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.setGridLinesVisible(true);
        servicesTable.setAlignment(Pos.CENTER);

        servicesScrollPane.setContent(servicesTable);
        ScrollPane root = new ScrollPane(servicesTable);
        root.setFitToHeight(true);
        root.setFitToWidth(true);
    }

    public static boolean showDeleteMasterConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Удаление мастера");
        alert.setContentText("Вы уверены, что хотите безвозвратно удалить мастера?");

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
