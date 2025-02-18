package org.admin.enterpriseWindow.infos;

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
import org.admin.connection.deleteRequests.DeleteService;
import org.admin.connection.getRequests.GetMaster;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;

public class MasterInfoDialog extends Main {
    public static void show(Long id){
        MasterInfo master = GetMaster.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о Мастере");

        VBox root = new VBox();

        Button cancelButton = new Button("Отмена");
        Button addServiceButton = new Button("Добавить услугу");
        Button saveButton = new Button("Сохранить");
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
        masterInfoTabel.getRowConstraints().add(new RowConstraints(150));

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


        VBox tableVBox = new VBox();
        tableVBox.setSpacing(20);
        tableVBox.setAlignment(Pos.CENTER);

        tableVBox.getChildren().addAll(servicesLabel, buildServiceTable(master, tableVBox));

        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        btns.getChildren().addAll(cancelButton, addServiceButton, saveButton);
        root.getChildren().addAll(masterInfoTabel, tableVBox, btns);

        Scene dialogScene = new Scene(root, 1500, 800);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private static ScrollPane buildServiceTable(MasterInfo master, VBox tableVBox){
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

        int index = 1;
        for(ServiceInfo service: master.getServices()){
            Label serviceIdLabel = new Label(service.getId().toString());
            Label serviceNameLabel = new Label(service.getName());
            Label servicePriceLabel = new Label(service.getPrice().toString());
            Label serviceDurationLabel = new Label(service.getDurationString());
            Button deleteService = new Button("Удалить");

            deleteService.setOnAction(event -> {
                DeleteService.deleteByMasterId(token, service.getId(), master.getId());
                tableVBox.getChildren().removeAll();
                Label servicesHeadLabel = new Label("Услуги: ");
                tableVBox.getChildren().addAll(servicesHeadLabel, buildServiceTable(master, tableVBox));
            });

            servicesTable.addRow(index, serviceIdLabel, serviceNameLabel, servicePriceLabel, serviceDurationLabel, deleteService);
            index++;
        }
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(200));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.getColumnConstraints().add(new ColumnConstraints(100));
        servicesTable.setGridLinesVisible(true);

        ScrollPane root = new ScrollPane(servicesTable);
        root.setFitToHeight(true);
        root.setFitToWidth(true);
        root.setPrefViewportHeight(300);
        root.setPrefViewportWidth(500);
        servicesTable.setAlignment(Pos.CENTER);
        return root;
    }
}
