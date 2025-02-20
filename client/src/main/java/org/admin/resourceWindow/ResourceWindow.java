package org.admin.resourceWindow;

import org.Main;

import org.admin.connection.resources.ConnectionResources;
import org.admin.sideMenu.SideMenu;
import org.admin.utils.*;

import javafx.stage.*;

import javafx.scene.*;


import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import org.admin.utils.entities.Resource;
import org.admin.utils.entities.ResourceRequest;

import java.util.*;

public class ResourceWindow extends Main{
    public static BorderPane loadResourcesListWindow(){
        ArrayList<Resource> resourceList = ConnectionResources.getResourcesList(token);
        
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableResourceList = new GridPane();
        
        Label idHead = new Label("ID ресурса");
        Label nameHead = new Label("Название ресурса");
        Label descriptionHead = new Label("Описание ресурса");

        tableResourceList.addRow(0, idHead, nameHead, descriptionHead);
        GridPane.setHalignment(idHead, HPos.CENTER);
        GridPane.setValignment(idHead, VPos.CENTER);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(descriptionHead, HPos.CENTER);
        GridPane.setValignment(descriptionHead, VPos.CENTER);

        if(resourceList == null){
            Resource resource = new Resource();
            resource.setId(Long.valueOf(-1));
            resource.setName("Ошибка");
            resource.setDescription("Ошибка");
            resourceList.add(resource);
        }

        for (int i = 0; i < resourceList.size(); i++){
            Resource resource = resourceList.get(i);

            Label id = new Label(resource.getId()+"");
            Label name = new Label(resource.getName());
            Label description = new Label(resource.getDescription());
            Button order = new Button("Заказать");
            
            order.setOnAction(event -> loadResourceOrderDialog(resource.getId()));

            if(resource.getId() == -1){
                tableResourceList.addRow(i+1, name, description);    
            }
            else{
                tableResourceList.addRow(i+1, id, name, description, order);
            }
            GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(id, VPos.CENTER);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(description, HPos.CENTER);
            GridPane.setValignment(description, VPos.CENTER);
            GridPane.setHalignment(order, HPos.CENTER);
            GridPane.setValignment(order, VPos.CENTER);
        }

        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));
        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));

        tableResourceList.setAlignment(Pos.CENTER);
        tableResourceList.setGridLinesVisible(true);

        scrollTable.setPrefViewportHeight(200);
        scrollTable.setPrefViewportWidth(100);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableResourceList);
        

        title.setText("Список Ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title, scrollTable);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public static void loadResourceOrderDialog(Long id) {    
        Resource resource = (Resource)ConnectionResources.getResourceById(token, id);
        if(resource.getCode() != 200){
            resource.setName(resource.getMsg());
            resource.setDescription(resource.getMsg());
        }

        String resourceName = resource.getName();
        String resourceDescription = resource.getDescription();

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Заказать ресурс");
        
        Label idLbl = new Label(id+"");
        Label nameLbl = new Label(resourceName);
        Label descriptionLbl = new Label(resourceDescription);
        Label countLbl = new Label("Укажите количество:");
        TextField countField = new TextField();
        Label error = new Label("");

        Button button1 = new Button("Назад");
        button1.setOnAction(e -> {
            dialog.close();
        });

        Button button2 = new Button("Заказать");
        button2.setOnAction(e -> {
            String countStr = countField.getText();
            try{
                int count = Integer.parseInt(countStr);
                Response response = ConnectionResources.createResourceRequest(token, id, count);
                if(response.getCode() == 200){dialog.close();}
                else{error.setText(response.getMsg());}
            }
            catch(Exception ex){
                error.setText("Введите число");
            }
        });

        VBox dialogLayout = new VBox(100);
        dialogLayout.setAlignment(Pos.CENTER);
        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(50);
        btns.getChildren().addAll(button1, button2);
        dialogLayout.getChildren().addAll(idLbl, nameLbl, descriptionLbl, countLbl, btns);
        Scene dialogScene = new Scene(dialogLayout, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static BorderPane loadResourcesRequestsWindow(){
        ArrayList<ResourceRequest> resourceRequestsList = ConnectionResources.getResourcesRequestsList(token);
        
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableResourceRequestList = new GridPane();
        
        Label idHead = new Label("ID ресурса");
        Label nameHead = new Label("Название ресурса");
        Label countHead = new Label("Количество");
        Label statusHead = new Label("Статус");

        tableResourceRequestList.addRow(0, idHead, nameHead, countHead, statusHead);
        GridPane.setHalignment(idHead, HPos.CENTER);
        GridPane.setValignment(idHead, VPos.CENTER);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(countHead, HPos.CENTER);
        GridPane.setValignment(countHead, VPos.CENTER);
        GridPane.setHalignment(statusHead, HPos.CENTER);
        GridPane.setValignment(statusHead, VPos.CENTER);

        if(resourceRequestsList == null){
            resourceRequestsList = new ArrayList<>();
        }

        for (int i = 0; i < resourceRequestsList.size(); i++){
            HBox statusBox = new HBox();
            statusBox.setSpacing(20);

            ResourceRequest request = resourceRequestsList.get(i);
            Label id = new Label(request.getId()+"");
            Label name = new Label(request.getResource().getName());
            Label count = new Label(request.getCount()+"");
            Label status = new Label(request.getStatusDescription());

            statusBox.getChildren().add(status);
            if(request.getStatus() == 3){
                Button statusBtn = new Button("Получено");
                statusBox.getChildren().add(statusBtn);
            }
            
            tableResourceRequestList.addRow(i+1, id, name, count, statusBox);
            GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(id, VPos.CENTER);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(count, HPos.CENTER);
            GridPane.setValignment(count, VPos.CENTER);
            GridPane.setHalignment(statusBox, HPos.CENTER);
            GridPane.setValignment(statusBox, VPos.CENTER);
        }

        tableResourceRequestList.getColumnConstraints().add(new ColumnConstraints(500));
        tableResourceRequestList.getColumnConstraints().add(new ColumnConstraints(500));

        tableResourceRequestList.setAlignment(Pos.CENTER);
        tableResourceRequestList.setGridLinesVisible(true);

        scrollTable.setPrefViewportHeight(200);
        scrollTable.setPrefViewportWidth(100);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableResourceRequestList);
        

        title.setText("Запросы ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title, scrollTable);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }
}